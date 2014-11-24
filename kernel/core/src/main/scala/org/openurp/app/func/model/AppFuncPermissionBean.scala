package org.openurp.app.func.model

import org.openurp.app.func.AppFuncPermission

import org.openurp.app.App

class AppFuncPermissionBean extends AppFuncPermission {

  var app: App = _
  var resource: String = _
  var actions: String = _
  var restrictions: String = _
}