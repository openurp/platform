package org.openurp.platform.api.security.id

/**
 * @author chaostone
 */
trait IdGenerator {
  def nextid(): String
}

trait NumericGenerator {
  def nextNumber(): String
}

trait RandomStringGenerator {
  def nextString(): String
}