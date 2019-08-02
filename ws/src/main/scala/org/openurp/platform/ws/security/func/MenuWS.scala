/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.config.service.AppService
import org.openurp.platform.security.model.Menu
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.user.model.Role
import org.openurp.platform.user.service.UserService
import org.openurp.platform.user.model.User
import org.openurp.platform.config.model.Domain
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer

class MenuWS extends ActionSupport with EntitySupport[Menu] {

  var entityDao: EntityDao = _

  var menuService: MenuService = _

  var appService: AppService = _

  var userService: UserService = _

  private def getQueryBuilder: OqlBuilder[Menu] = {
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
  def index(@param("app") appName: String): collection.Seq[Any] = {
    val menus = appService.getApp(appName) match {
      case Some(app) => menuService.getTopMenus(app)
      case None      => List.empty[Menu]
    }
    menus map (m => convert(m))
  }

  @response
  @mapping("user/{user}")
  def user(@param("app") appName: String, @param("user") username: String): Any = {
    val up = userService.get(username)
    if (up.isEmpty) {
      return "{}"
    }
    val u = up.get
    val app = appService.getApp(appName)
    val forDomain = getBoolean("forDomain", false)
    app match {
      case Some(app) =>
        if (forDomain) {
          getDomainMenus(app.domain, u)
        } else {
          val appProps = new Properties(app, "id", "name", "title", "base", "url", "logoUrl", "navStyle")
          appProps.add("domain", app.domain, "id", "name", "title", "indexno")
          val menus = menuService.getTopMenus(app, u) map (m => convert(m))
          AppMenus(appProps, menus)
        }
      case None =>
        appService.getDomain(appName) match {
          case None    => "{}"
          case Some(d) => getDomainMenus(d, u)
        }
    }
  }

  @response
  @mapping("role/{roleId}")
  def role(@param("app") appName: String, @param("roleId") roleId: Int): Iterable[Any] = {
    appService.getApp(appName) match {
      case Some(app) =>
        val roles = entityDao.findBy(classOf[Role], "id", List(roleId))
        menuService.getTopMenus(app, roles.head) map (m => convert(m))
      case None => List.empty[Menu]
    }
  }

  private def getDomainMenus(dm: Domain, u: User): DomainMenus = {
    val menus = menuService.getTopMenus(Some(dm), u)
    val appsMenus = menus.groupBy(_.app)
    val domainApps = appsMenus.keys.groupBy(_.domain)
    val directMenuMaps = domainApps map {
      case (oned, apps) =>
        val domain = new Properties(oned, "id", "name", "title", "indexno")
        val appMenus = domainApps(oned).toBuffer.sorted map { app =>
          val appProps = new Properties(app, "id", "name", "title", "base", "url", "logoUrl", "navStyle")
          AppMenus(appProps, appsMenus(app).map(convert(_)))
        }
        (oned, DomainMenus(domain, appMenus, new ArrayBuffer[DomainMenus]))
    }

    val domainMenuMaps = new collection.mutable.HashMap[Domain, DomainMenus]
    domainMenuMaps ++= directMenuMaps

    directMenuMaps.keys.toSeq.sorted foreach (addParent(_, domainMenuMaps))
    domainMenuMaps(dm)
  }

  private def addParent(domain: Domain, domainMenuMaps: collection.mutable.HashMap[Domain, DomainMenus]): Unit = {
    domain.parent foreach { pd =>
      val parent = domainMenuMaps.get(pd) match {
        case Some(s) => s
        case None =>
          val s = DomainMenus(new Properties(pd, "id", "name", "title", "indexno"), List.empty, new ArrayBuffer[DomainMenus])
          domainMenuMaps.put(pd, s)
          s
      }
      parent.children += domainMenuMaps(domain)
      pd.parent foreach (addParent(_, domainMenuMaps))
    }
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

case class AppMenus(app: Properties, menus: Iterable[Properties])

case class DomainMenus(domain: Properties, appMenus: Iterable[AppMenus], children: Buffer[DomainMenus])
