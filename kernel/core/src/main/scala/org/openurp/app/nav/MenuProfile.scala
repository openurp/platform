package org.openurp.app.nav

import org.openurp.app.App

trait MenuProfile extends org.beangle.security.blueprint.MenuProfile {

  def app: App
}