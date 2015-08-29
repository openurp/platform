package org.openurp.platform.security.service.impl

import org.openurp.platform.security.model.{ Menu, MenuProfile, Profile, Role, User }
import org.openurp.platform.security.service.MenuService

/**
 * @author chaostone
 */
class MenuServiceImpl extends MenuService {

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

}