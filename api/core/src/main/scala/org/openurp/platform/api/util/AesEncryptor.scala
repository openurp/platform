package org.openurp.platform.api.util

import java.security.Key
import java.security.MessageDigest
import javax.crypto.spec.SecretKeySpec
import java.util.Arrays
import javax.crypto.Cipher

object AesEncryptor {
  val defaultSecretKey = "changeit"
  val ALGORITHM = "AES"

  def main(args: Array[String]) {
    if (args.length == 1 || args.length == 2) {
      val secretKey = if (args.length == 2) args(1) else null
      System.out.println(args(0) + ":" + new AesEncryptor(secretKey).encrypt(args(0)));
    } else {
      System.out.println("USAGE: java AesEncryptor string-to-encrypt [secretKey]");
    }
  }
}

class AesEncryptor(initkey: String) {
  val secretKeySpec: Key = generateKey(initkey)

  import AesEncryptor._

  private def generateKey(keyStr: String): Key = {
    val secretKey = if (keyStr == null) defaultSecretKey else keyStr
    var key = secretKey.getBytes("UTF-8")
    val sha = MessageDigest.getInstance("SHA-1")
    key = sha.digest(key)
    key = Arrays.copyOf(key, 16); // use only the first 128 bit
    new SecretKeySpec(key, ALGORITHM)
  }

  def encrypt(plain: String): String = {
    val cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    asHexString(cipher.doFinal(plain.getBytes("UTF-8")))
  }

  def decrypt(encrypted: String): String = {
    val cipher = Cipher.getInstance(ALGORITHM)
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    new String(cipher.doFinal(toByteArray(encrypted)))
  }

  private def toByteArray(hexString: String): Array[Byte] = {
    val arrLength = hexString.length() >> 1
    val buf = new Array[Byte](arrLength)
    (0 until arrLength) foreach { ii =>
      val index = ii << 1
      val l_digit = hexString.substring(index, index + 2)
      buf(ii) = Integer.parseInt(l_digit, 16).asInstanceOf[Byte]
    }
    buf
  }
  private def asHexString(buf: Array[Byte]): String = {
    val strbuf = new StringBuffer(buf.length * 2)
    (0 until buf.length) foreach { i =>
      if ((buf(i) & 0xff) < 0x10) strbuf.append("0");
      strbuf.append(java.lang.Long.toString(buf(i) & 0xff, 16));
    }
    strbuf.toString()
  }

}