package org.openurp.platform.config.model

import java.{ util => ju }
import org.beangle.commons.model.IntId
import org.beangle.commons.model.StringId

class AccessToken extends StringId {

  var appId: Int = _

  var principal: String = _

  var expiredAt: ju.Date = _

  def expired: Boolean = {
    (new ju.Date).after(expiredAt)
  }
}