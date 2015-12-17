package org.openurp.platform.user.model

/**
 * @author chaostone
 */
object MemberShip extends Enumeration(1) {
  type Ship = Value
  val Member = Value("Member")
  val Granter = Value("Granter")
  val Manager = Value("Manager")
}