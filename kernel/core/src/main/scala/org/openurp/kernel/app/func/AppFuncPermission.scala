package org.openurp.kernel.app.func

import org.openurp.kernel.app.App

trait AppFuncPermission {

  def app: App

  def resource: FuncResource

  def actions: String
  
  def restrictions: String
}