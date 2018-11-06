/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
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

class MenuWS extends ActionSupport with EntitySupport[Menu] {

  var entityDao: EntityDao = _

  var menuService: MenuService = _

  var appService: AppService = _

  var userService: UserService = _

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
  def index(@param("app") appName: String): Seq[Any] = {
    val menus = appService.getApp(appName) match {
      case Some(app) => menuService.getTopMenus(app)
      case None      => List.empty[Menu]
    }
    menus map (m => convert(m))
  }

  @response
  @mapping("user/{user}")
  def user(@param("app") appName: String, @param("user") username: String): Iterable[Any] = {
    val forDomain = getBoolean("forDomain", false)
    appService.getApp(appName) match {
      case Some(app) =>
        userService.get(username) match {
          case Some(u) =>
            if (forDomain) {
              val menus = menuService.getTopMenus(app.domain, u)
              val domain = new Properties(app.domain, "id", "name", "title")
              val appMenus = menus.groupBy(_.app)
              appMenus map {
                case (app, menus) =>
                  val appProps = new Properties(app, "id", "name", "title", "base", "url", "logoUrl", "embeddable")
                  appProps.put("domain", domain)
                  AppMenu(appProps, menus.map(convert(_)))
              }
            } else {
              menuService.getTopMenus(app, u) map (m => convert(m))
            }
          case None => List.empty[Menu]
        }
      case None => List.empty[Menu]
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

case class AppMenu(app: Properties, menus: Iterable[Properties])