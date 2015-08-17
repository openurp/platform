package org.openurp.platform.kernel.app.model

import java.{ util => ju }

import org.beangle.data.model.IntId

class AccessToken extends IntId {

  var app: App = _

  var token: String = _

  var expiredAt: ju.Date = _

  def expired: Boolean = {
    (new ju.Date).after(expiredAt)
  }
}