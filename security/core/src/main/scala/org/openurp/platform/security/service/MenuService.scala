package org.openurp.platform.security.service

import org.openurp.platform.security.model.{ Menu, MenuProfile, Profile, Role, User }
import org.openurp.platform.kernel.model.App

trait MenuService {

  def getTopMenus(app: App, user: User): collection.Map[MenuProfile, Seq[Menu]]

  def getMenus(profile: MenuProfile, user: User): Seq[Menu]

  def getMenus(profile: MenuProfile, role: Role): Seq[Menu]

  def getProfile(role: Role, profileId: Integer): MenuProfile

  def getProfiles(user: User): Seq[MenuProfile]

  def getProfiles(role: Role): Seq[MenuProfile]

  def move(menu: Menu, location: Menu, index: Int): Unit
}