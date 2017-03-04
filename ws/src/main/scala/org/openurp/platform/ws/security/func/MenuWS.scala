package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.Properties
import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.Menu
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.user.model.{ Role, User }

class MenuWS extends ActionSupport with EntitySupport[Menu] {

  var entityDao: EntityDao = _

  var menuService: MenuService = _

  private def getQueryBuilder(): OqlBuilder[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu], "menu")
    QueryHelper.populateConditions(builder)
    builder.where("menu.enabled=true")

    get("indexno") match {
      case Some(indexno) => builder.where("menu.indexno = :indexno", indexno)
      case None          => builder.where("menu.parent is null")
    }
    builder.orderBy("menu.indexno").limit(null)
  }

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val menus = entityDao.search(OqlBuilder.from(classOf[Menu], "menu").where("menu.app.name=:app and menu.parent = null", app))
    menus map (m => convert(m))
  }

  @response
  @mapping("user/{user}")
  def user(@param("app") appName: String, @param("user") username: String): Iterable[Any] = {
    val apps = entityDao.findBy(classOf[App], "name", List(appName))
    val users = entityDao.findBy(classOf[User], "code", List(username))
    if (apps.isEmpty) {
      List.empty[Menu]
    } else {
      menuService.getTopMenus(apps.head, users.head) map (m => convert(m))
    }
  }

  @response
  @mapping("role/{roleId}")
  def role(@param("app") appName: String, @param("roleId") roleId: Int): Iterable[Any] = {
    val apps = entityDao.findBy(classOf[App], "name", List(appName))
    val roles = entityDao.findBy(classOf[Role], "id", List(roleId))
    val app = apps.head
    menuService.getTopMenus(app, roles.head) map (m => convert(m))
  }

  private def convert(one: Menu): Properties = {
    val menu = new Properties(one, "id", "title", "indexno")
    if (None != one.entry) menu.put("entry", one.entry.get.name + (if (one.params.isDefined) "?" + one.params.get else ""))
    if (!one.children.isEmpty) {
      val children = new collection.mutable.ListBuffer[Properties]
      one.children foreach { child =>
        children += convert(child.asInstanceOf[Menu])
      }
      menu.put("children", children)
    }
    menu
  }

}
