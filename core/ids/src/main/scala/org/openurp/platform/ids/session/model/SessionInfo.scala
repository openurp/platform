package org.openurp.platform.ids.session.model

import org.beangle.data.model.StringId
import java.time.Instant
import java.time.Duration

class SessionInfo extends StringId {
  var principal: String = _
  var description: Option[String] = _
  var ip: Option[String] = _
  var agent: Option[String] = _
  var os: Option[String] = _
  var loginAt: Instant = _
  var lastAccessAt: Instant = _
}