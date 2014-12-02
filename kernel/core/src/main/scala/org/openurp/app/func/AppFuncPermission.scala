package org.openurp.app.func

import org.openurp.app.App

trait AppFuncPermission {

  def app: App

  def resource: FuncResource

  def actions: String
  
  def restrictions: String
}