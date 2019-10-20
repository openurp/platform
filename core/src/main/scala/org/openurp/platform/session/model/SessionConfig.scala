package org.openurp.platform.session.model

import org.beangle.data.model.LongId
import org.openurp.platform.user.model.UserCategory

class SessionConfig extends LongId {
  var category: UserCategory = _
  var ttiMinutes: Int = _
  var concurrent: Int = _
  var checkConcurrent: Boolean = _
  var checkCapacity: Boolean = _
}
