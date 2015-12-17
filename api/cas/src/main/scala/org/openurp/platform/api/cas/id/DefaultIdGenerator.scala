package org.openurp.platform.api.cas.id

import org.beangle.commons.logging.Logging

/**
 * @author chaostone
 */
class DefaultIdGenerator(numericGenerator: NumericGenerator, randomStringGenerator: RandomStringGenerator, prefix: String)
    extends IdGenerator with Logging {

  private val id = new scala.util.Random(System.currentTimeMillis).nextInt(1000000)

  def this(p: String, maxlength: Int) {
    this(new DefaultLongNumericGenerator(1), new DefaultRandomStringGenerator(maxlength), p)
  }

  override def nextid(): String = {
    val number = numericGenerator.nextNumber()
    val buffer = new StringBuilder(300)
    buffer.append(prefix).append(number).append('-').append(randomStringGenerator.nextString())
    buffer.append(id)
    buffer.toString
  }
}