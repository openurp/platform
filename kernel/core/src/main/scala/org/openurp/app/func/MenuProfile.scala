package org.openurp.app.func

import org.openurp.app.App

trait MenuProfile extends org.beangle.security.blueprint.MenuProfile {

  def app: App
}