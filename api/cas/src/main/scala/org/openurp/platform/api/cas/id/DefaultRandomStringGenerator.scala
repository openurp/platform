package org.openurp.platform.api.cas.id

import java.security.SecureRandom

object DefaultRandomStringGenerator {
  val Printables = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345679".toCharArray

  def toString(bytes: Array[Byte]): String = {
    val maxlength = bytes.length
    val printableLength = Printables.length
    val output = Array.ofDim[Char](maxlength)
    var i = 0
    while (i < maxlength) {
      val index = Math.abs(bytes(i) % printableLength)
      output(i) = Printables(index)
      i += 1
    }
    new String(output)
  }
}

/**
 * @author chaostone
 */
class DefaultRandomStringGenerator(maxlength: Int) extends RandomStringGenerator {

  private val randomizer = new SecureRandom

  override def nextString: String = {
    val random = Array.ofDim[Byte](maxlength)
    this.randomizer.nextBytes(random)
    DefaultRandomStringGenerator.toString(random)
  }
}