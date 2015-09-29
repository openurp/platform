package org.openurp.platform.kernel.model

import java.{ util => ju }
import org.beangle.data.model.IntId
import org.beangle.data.model.StringId

class AccessToken extends StringId {

  var appId: Int = _

  var principal: String = _

  var expiredAt: ju.Date = _

  def expired: Boolean = {
    (new ju.Date).after(expiredAt)
  }
}