package org.openurp.platform.security.service.impl

import scala.collection.mutable.Buffer

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.data.model.util.Hierarchicals
import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.security.model.{ FuncPermission, Menu, MenuProfile }
import org.openurp.platform.security.service.MenuService

/**
 * @author chaostone
 */
class MenuServiceImpl(val entityDao: EntityDao) extends MenuService {

  def getTopMenus(app: App, user: User): collection.Map[MenuProfile, Seq[Menu]] = {

    val roles = user.roles.filter(m => m.member).map { m => m.role }
    val query = OqlBuilder.from[Menu](classOf[Menu].getName + " menu," + classOf[FuncPermission].getName + " fp")
      .where("menu.profile.app=:app", app)
      .where("menu.enabled=true")
      .where("fp.role  in (:roles)", roles)
      .where("fp.resource=menu.entry and fp.resource.app=:app", app)
      .select("menu")

    val menuSet = Collections.newSet[Menu]
    entityDao.search(query).foreach { m =>
      menuSet += m
      var p = m.parent
      while (null != p) {
        if (!menuSet.contains(p)) {
          menuSet += p
          p = p.parent
        } else {
          p = null
        }
      }
    }
    def removeOther(menu: Menu, menus: collection.Set[Menu]): Unit = {
      menu.children --= menu.children.filter(f => !menus.contains(f))
      menu.children.foreach { c => removeOther(c, menus) }
    }

    val profile2Menus = Collections.newMap[MenuProfile, Buffer[Menu]]
    menuSet foreach { m =>
      val menus = profile2Menus.getOrElseUpdate(m.profile, Collections.newBuffer[Menu])
      if (m.parent == null) {
        menus += m
        removeOther(m, menuSet)
      }
    }
    profile2Menus map { case (p, menus) => (p, menus.sorted) }

  }

  def getMenus(profile: MenuProfile, user: User): Seq[Menu] = {
    null
  }

  def getMenus(profile: MenuProfile, role: Role): Seq[Menu] = {
    val query = buildMenuQuery(profile, role);
    query.where("menu.enabled= true")
    val menus = Collections.newSet[Menu]
    menus ++= entityDao.search(query)
    addParentMenus(menus)
  }

  private def buildMenuQuery(profile: MenuProfile, role: Role): OqlBuilder[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu]);
    builder.join("menu.resources", "mr");
    builder.where("exists(from " + classOf[FuncPermission].getName
      + " a where a.role=:role and a.resource=mr)", role);
    builder.where("mr=menu.entry");
    if (null != profile) builder.where("menu.profile=:profile", profile);
    builder
  }

  private def addParentMenus(menus: collection.mutable.Set[Menu]): Seq[Menu] = {
    Hierarchicals.addParent(menus);
    menus.toList.sorted
  }

  def getProfiles(user: User): Seq[MenuProfile] = {
    null
  }

  def move(menu: Menu, location: Menu, index: Int): Unit = {
    val nodes =
      if (null != location) Hierarchicals.move(menu, location, index)
      else {
        val builder = OqlBuilder.from(classOf[Menu], "m")
          .where("m.profile.id=:profileId and m.parent is null", menu.profile.id)
          .orderBy("m.indexno")
        Hierarchicals.move(menu, entityDao.search(builder).toBuffer, index)
      }
    entityDao.saveOrUpdate(nodes)
  }
}
