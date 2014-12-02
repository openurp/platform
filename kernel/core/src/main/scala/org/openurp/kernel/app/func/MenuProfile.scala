package org.openurp.kernel.app.func

import org.openurp.kernel.app.App

trait MenuProfile extends org.beangle.security.blueprint.MenuProfile {

  def app: App
}