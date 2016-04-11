package org.openurp.platform.security.service

import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.security.model.{ Menu, MenuProfile }

trait MenuService {

  def getTopMenus(app: App, user: User): collection.Map[MenuProfile, Seq[Menu]]

  def getTopMenus(app: App, role: Role): collection.Map[MenuProfile, Seq[Menu]]

  def getMenus(profile: MenuProfile, role: Role): Seq[Menu]

  def getMenus(profile: MenuProfile, user: User): Seq[Menu]

  def move(menu: Menu, location: Menu, index: Int): Unit
}
