package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.security.model.{ Menu, MenuProfile }
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.MenuProfile
import org.openurp.platform.user.model.User
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.MenuProfile
import org.openurp.platform.user.model.User
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.MenuProfile
import org.openurp.platform.user.model.User
import org.openurp.platform.user.model.Role

class MenuWS extends ActionSupport with EntitySupport[Menu] {

  var entityDao: EntityDao = _

  var menuService: MenuService = _

  private def getQueryBuilder(): OqlBuilder[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu], "menu")
    QueryHelper.populateConditions(builder)
    builder.where("menu.enabled=true")

    get("indexno") match {
      case Some(indexno) => builder.where("menu.indexno = :indexno", indexno)
      case None => builder.where("menu.parent is null")
    }
    builder.orderBy("menu.indexno").limit(null)
  }

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val mps = entityDao.search(OqlBuilder.from(classOf[MenuProfile], "mp").where("mp.app.name=:app", app))
    mps map { p =>
      val pp = new Properties(p, "id", "name")
      pp.put("menus", p.menus.filter(m => m.parent == null) map (m => convert(m)))
      pp
    }
  }

  @response
  @mapping("user/{user}")
  def user(@param("app") appName: String, @param("user") username: String): Iterable[Any] = {
    val apps = entityDao.findBy(classOf[App], "name", List(appName))
    val users = entityDao.findBy(classOf[User], "code", List(username))
    val app = apps.head
    menuService.getTopMenus(app, users.head) map {
      case (p, menus) =>
        val pp = new Properties(p, "id", "name")
        pp.put("menus", menus map (m => convert(m)))
        pp
    }
  }

  @response
  @mapping("role/{roleId}")
  def role(@param("app") appName: String, @param("roleId") roleId: Int): Iterable[Any] = {
    val apps = entityDao.findBy(classOf[App], "name", List(appName))
    val roles = entityDao.findBy(classOf[Role], "id", List(roleId))
    val app = apps.head
    menuService.getTopMenus(app, roles.head) map {
      case (p, menus) =>
        val pp = new Properties(p, "id", "name")
        pp.put("menus", menus map (m => convert(m)))
        pp
    }
  }

  @response
  @mapping("profile/{profileId}")
  def profile(@param("app") app: String, profileId: Int): Seq[Any] = {
    val builder = getQueryBuilder()
    builder.where("menu.profile.app.name=:app", app)
    builder.where("menu.profile.id=:profileId", profileId)
    entityDao.search(builder) map (one => convert(one))
  }

  private def convert(one: Menu): Properties = {
    val menu = new Properties(one, "id", "title", "indexno")
    if (null != one.entry) menu.put("entry", one.entry.name + (if (null != one.params) "?" + one.params else ""))
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
