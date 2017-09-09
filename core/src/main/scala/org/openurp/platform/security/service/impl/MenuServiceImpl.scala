/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.security.service.impl

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.data.model.util.Hierarchicals
import org.openurp.platform.config.model.App
import org.openurp.platform.security.model.{ FuncPermission, Menu }
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
      if (null != location) Hierarchicals.move(menu, location, index)
      else {
        val builder = OqlBuilder.from(classOf[Menu], "m")
          .where("m.app = :app and m.parent is null", menu.app)
          .orderBy("m.indexno")
        if (None != menu.parent) entityDao.evict(menu.parent.get)
        Hierarchicals.move(menu, entityDao.search(builder).toBuffer, index)
      }
    entityDao.saveOrUpdate(nodes)
  }
}
