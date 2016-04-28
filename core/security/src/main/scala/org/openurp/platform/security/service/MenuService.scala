package org.openurp.platform.security.service

import org.openurp.platform.config.model.App
import org.openurp.platform.user.model.{ Role, User }
import org.openurp.platform.security.model.Menu

trait MenuService {

  def getTopMenus(app: App, user: User): Seq[Menu]

  def getTopMenus(app: App, role: Role): Seq[Menu]

  def getMenus(app: App, role: Role): Seq[Menu]

  def getMenus(app: App, user: User): Seq[Menu]

  def getMenus(app: App): Seq[Menu]

  def move(menu: Menu, location: Menu, index: Int): Unit
}
