package org.openurp.kernel.app.func

import org.openurp.kernel.app.App

trait FuncResource extends org.beangle.security.blueprint.FuncResource {

  def app: App

}