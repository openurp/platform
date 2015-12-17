package org.openurp.platform.user.model

import org.beangle.data.model.{ LongId, Updated }

import MemberShip.{ Granter, Manager, Member, Ship }

/**
 * @author chaostone
 */
class GroupMember extends LongId with Updated {
  var user: User = _
  var group: Group = _
  var member: Boolean = _
  var granter: Boolean = _
  var manager: Boolean = _

  def this(user: User, group: Group) {
    this()
    this.user = user
    this.group = group
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

