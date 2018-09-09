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
package org.openurp.platform.security.service.impl

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.data.model.util.Hierarchicals
import org.beangle.security.authz.Scopes
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.{ FuncPermission, FuncResource, Menu }
import org.openurp.platform.security.service.MenuService
import org.openurp.platform.user.model.{ Role, User }

/**
 * @author chaostone
 */
class MenuServiceImpl(val entityDao: EntityDao) extends MenuService {

  def getTopMenus(app: App, user: User): Seq[Menu] = {
    val roles = user.roles.filter(m => m.member).map { m => m.role }
    getTopMenus(app, roles)
  }

  def getTopMenus(app: App, role: Role): Seq[Menu] = {
    getTopMenus(app, List(role))
  }

  private def getTopMenus(app: App, roles: Iterable[Role]): Seq[Menu] = {
    val menuSet = Collections.newSet[Menu]
    roles foreach { role =>
      val query = OqlBuilder.from[Menu](classOf[Menu].getName + " menu," + classOf[FuncPermission].getName + " fp")
        .where("menu.app=:app", app)
        .where("menu.enabled=true")
        .where("fp.role =:role", role)
        .where("fp.resource=menu.entry and fp.resource.app=:app", app)
        .select("menu").cacheable()

      entityDao.search(query).foreach { m =>
        menuSet += m
        var p = m.parent.orNull
        while (null != p) {
          if (!menuSet.contains(p)) {
            menuSet += p
            p = p.parent.orNull
          } else {
            p = null
          }
        }
      }
    }

    val menus = Collections.newBuffer[Menu]
    menuSet foreach { m =>
      if (m.parent.isEmpty) {
        menus += m
        reserveChildren(m, menuSet)
      }
    }
    menus.sorted
  }

  private def reserveChildren(menu: Menu, menus: collection.Set[Menu]): Unit = {
    menu.children --= menu.children.filter(f => !menus.contains(f))
    menu.children.foreach { c => reserveChildren(c, menus) }
  }

  def getMenus(app: App, role: Role): Seq[Menu] = {
    val query = buildMenuQuery(app, role)
    query.where("menu.enabled = true")
    val menus = Collections.newSet[Menu]
    menus ++= entityDao.search(query)
    addParentMenus(menus)
  }

  def getMenus(app: App, user: User): Seq[Menu] = {
    val menus = Collections.newSet[Menu]
    for (rm <- user.roles) {
      if (rm.member) {
        val query = buildMenuQuery(app, rm.role)
        query.where("menu.enabled= true")
        menus ++= entityDao.search(query)
      }
    }
    addParentMenus(menus)
  }

  override def getTopMenus(app: App): Seq[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu]).where("menu.app= :app and menu.parent = null", app).orderBy("menu.indexno").cacheable()
    entityDao.search(builder)
  }

  override def getMenus(app: App): Seq[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu]).where("menu.app= :app", app).orderBy("menu.indexno").cacheable()
    entityDao.search(builder)
  }

  private def buildMenuQuery(app: App, role: Role): OqlBuilder[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu])
    builder.join("menu.resources", "mr")
    builder.where("exists(from " + classOf[FuncPermission].getName
      + " a where a.role=:role and a.resource=mr)", role)
    builder.where("mr=menu.entry")
    builder.where("menu.app = :app", app)
    builder.cacheable()
    builder
  }

  private def addParentMenus(menus: collection.mutable.Set[Menu]): Seq[Menu] = {
    Hierarchicals.addParent(menus)
    menus.toList.sorted
  }

  def move(menu: Menu, location: Menu, index: Int): Unit = {
    val nodes =
      if (null != location) {
        Hierarchicals.move(menu, location, index)
      } else {
        val builder = OqlBuilder.from(classOf[Menu], "m")
          .where("m.app = :app and m.parent is null", menu.app)
          .orderBy("m.indexno")
        if (None != menu.parent) entityDao.evict(menu.parent.get)
        Hierarchicals.move(menu, entityDao.search(builder).toBuffer, index)
      }
    entityDao.saveOrUpdate(nodes)
  }

  def importFrom(app: App, xml: scala.xml.Node): Unit = {
    parseMenu(app, None, xml)
  }

  private def parseMenu(app: App, parent: Option[Menu], xml: scala.xml.Node): Unit = {
    (xml \ "menu") foreach { m =>
      val indexno = (m \ "@indexno").text.trim
      val title = (m \ "@title").text.trim
      val menus = findMenu(app, indexno, title)
      var menu: Menu = null
      if (menus.isEmpty) {
        menu = new Menu
        menu.title = title
        menu.indexno = indexno
        menu.app = app

        val name = (m \ "@name")
        if (name.isEmpty) {
          menu.name = menu.title
        } else {
          menu.name = name.text.trim
        }
        val params = (m \ "@params")
        if (params.isEmpty) {
          menu.params = None
        } else {
          menu.params = Some(params.text.trim)
        }
        val enabled = (m \ "@enabled")
        if (enabled.isEmpty) {
          menu.enabled = true
        } else {
          menu.enabled = enabled.text.trim.toBoolean
        }
        (m \ "resources" \ "resource") foreach { r =>
          val name = (r \ "@name").text.trim
          val title = (r \ "@title").text.trim
          val scope = (r \ "@scope").text.trim
          val enabled = (r \ "@enabled").text.trim
          val fr = findOrCreateFuncResource(app, name, title, scope, enabled)
          menu.resources += fr
        }
        val entry = findFuncResource(app, (m \ "@entry").text.trim)
        menu.entry = entry
        menu.parent = parent
        entityDao.saveOrUpdate(menu)
      } else {
        menu = menus.head
      }
      val children = (m \ "children")
      if (!children.isEmpty) {
        parseMenu(app, Some(menu), children.head)
      }
    }
  }

  private def findMenu(app: App, indexno: String, title: String): Option[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu], "m").where("m.app=:app", app)
    builder.where("m.indexno=:indexno and m.title=:title", indexno, title)
    val res = entityDao.search(builder)
    if (res.size >= 1) {
      Some(res.head)
    } else {
      None
    }
  }
  private def findFuncResource(app: App, name: String): Option[FuncResource] = {
    val builder = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app=:app", app)
    builder.where("fr.name=:name", name)
    val res = entityDao.search(builder)
    if (res.size >= 1) {
      Some(res.head)
    } else {
      None
    }
  }

  private def findOrCreateFuncResource(app: App, name: String, title: String, scope: String, enabled: String): FuncResource = {
    findFuncResource(app, name) match {
      case None =>
        val resource = new FuncResource()
        resource.app = app
        resource.name = name
        resource.title = title
        resource.scope = Scopes.withName(scope).asInstanceOf[Scopes.Scope]
        if (Strings.isEmpty(enabled)) {
          resource.enabled = true
        } else {
          resource.enabled = enabled.toBoolean
        }
        entityDao.saveOrUpdate(resource)
        resource
      case Some(r) => r
    }
  }
}
