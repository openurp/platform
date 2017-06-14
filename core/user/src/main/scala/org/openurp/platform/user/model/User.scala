package org.openurp.platform.user.model

import java.security.Principal
import java.time.LocalDate
import java.{ util => ju }

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{ Coded, Enabled, Named, Remark, TemporalOn, Updated }

/**
 * @author chaostone
 */

class User extends LongId with Coded with Named with Updated with TemporalOn with Enabled with Profile with Principal with Remark {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: Option[ju.Date] = None
  var roles = Collections.newBuffer[RoleMember]
  var groups = Collections.newBuffer[GroupMember]
  var properties = Collections.newMap[Dimension, String]
  var category: UserCategory = _

  def credential = password

  def accountExpired: Boolean = {
    endOn match {
      case Some(e) => LocalDate.now.isAfter(e)
      case None    => false
    }
  }

  def credentialExpired: Boolean = {
    if (None == passwordExpiredOn) false
    else (new ju.Date).after(passwordExpiredOn.get)
  }

  override def getName: String = {
    name
  }
}