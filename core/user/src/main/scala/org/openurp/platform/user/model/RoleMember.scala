package org.openurp.platform.user.model

import org.beangle.data.model.{ LongId, Updated }

import MemberShip.{ Granter, Manager, Member, Ship }

/**
 * @author chaostone
 */
class RoleMember extends LongId with Updated {
  var user: User = _
  var role: Role = _
  var member: Boolean = _
  var granter: Boolean = _
  var manager: Boolean = _

  def this(user: User, role: Role) {
    this()
    this.user = user
    this.role = role
  }

  import MemberShip._
  def is(ship: Ship): Boolean = {
    ship match {
      case Member => member
      case Manager => manager
      case Granter => granter
      case _ => false
    }
  }
}
