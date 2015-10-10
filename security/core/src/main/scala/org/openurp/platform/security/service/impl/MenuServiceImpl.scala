package org.openurp.platform.security.service.impl

import org.openurp.platform.security.model.{ Menu, MenuProfile, Profile, Role, User }
import org.openurp.platform.security.service.MenuService
import org.beangle.data.dao.EntityDao
import org.beangle.data.model.util.Hierarchicals
import org.beangle.data.dao.OqlBuilder

/**
 * @author chaostone
 */
class MenuServiceImpl(val entityDao: EntityDao) extends MenuService {

  def getMenus(profile: MenuProfile, role: Role, active: Option[Boolean]): Seq[Menu] = {
    null
  }

  def getMenus(profile: MenuProfile, user: User, profiles: Seq[Profile]): Seq[Menu] = {
    null
  }

  def getProfile(role: Role, profileId: Integer): MenuProfile = {
    null
  }

  def getProfiles(user: User): Seq[MenuProfile] = {
    null
  }

  def getProfiles(role: Role): Seq[MenuProfile] = {
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