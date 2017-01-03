package org.openurp.platform.user.model

import org.beangle.commons.model.{ LongId, Updated }

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
  def this(user: User, role: Role, ship: Ship) {
    this(user, role)
    ship match {
      case Member => member = true
      case Manager => manager = true
      case Granter => granter = true
    }
  }

  def is(ship: Ship): Boolean = {
    ship match {
      case Member => member
      case Manager => manager
      case Granter => granter
    }
  }
}
