package org.openurp.app.func

import org.openurp.app.App

trait AppFuncPermission {

  def app: App

  def resource: String

  def actions: String
  
  def restrictions: String
}