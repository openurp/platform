package org.openurp.platform.session.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Named, Updated}
import org.beangle.security.session.EventTypes

class SessionEvent extends LongId with Updated with Named {

  var eventType: EventTypes.Type = _

  var principal: String = _

  var username: String = _

  var detail: String = _

  var ip: String = _
}
