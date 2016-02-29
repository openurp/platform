package org.openurp.platform.user.model

import java.security.Principal
import java.{ util => ju }

import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Coded, Enabled, LongId, Named, TemporalOn, Updated }
/**
 * @author chaostone
 */

class User extends LongId with Coded with Named with Updated with TemporalOn with Enabled with Profile with Principal {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: ju.Date = _
  var remark: String = _
  var roles = Collections.newBuffer[RoleMember]
  var groups = Collections.newBuffer[GroupMember]
  var properties = Collections.newMap[Dimension, String]
  var category: UserCategory = _

  def credential = password

  def accountExpired: Boolean = {
    null != endOn && (new ju.Date).after(endOn)
  }

  def credentialExpired: Boolean = {
    if (null == passwordExpiredOn) false
    else (new ju.Date).after(passwordExpiredOn)
  }
  override def getName: String = {
    name
  }
}