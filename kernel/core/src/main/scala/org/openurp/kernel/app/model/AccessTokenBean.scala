package org.openurp.kernel.app.model

import java.{ util => ju }

import org.beangle.data.model.bean.IntIdBean
import org.openurp.kernel.app.App

class AccessTokenBean extends IntIdBean {

  var app: App = _

  var token: String = _

  var expiredAt: ju.Date = _

  def expired: Boolean = {
    (new ju.Date).after(expiredAt)
  }
}