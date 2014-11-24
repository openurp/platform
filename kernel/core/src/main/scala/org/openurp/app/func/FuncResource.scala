package org.openurp.app.func

import org.openurp.app.App

trait FuncResource extends org.beangle.security.blueprint.FuncResource {

  def app: App

}