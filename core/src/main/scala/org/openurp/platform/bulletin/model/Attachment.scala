package org.openurp.platform.bulletin.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated
import java.io.BufferedOutputStream
import java.io.InputStream
import org.beangle.commons.io.IOs
import java.io.ByteArrayOutputStream
import java.time.Instant

object Attachment {
  def apply(name: String, is: InputStream): Attachment = {
    val a = new Attachment
    a.fileName = name

    val buf = new ByteArrayOutputStream
    IOs.copy(is, buf)
    a.content = buf.toByteArray()
    a.size = a.content.length
    a.updatedAt = Instant.now
    a
  }
}

class Attachment extends LongId with Updated {

  var size: Int = _

  var content: Array[Byte] = _

  var fileName: String = _

  def merge(newer: Attachment): Unit = {
    this.size = newer.size
    this.fileName = newer.fileName
    this.content = newer.content
    this.updatedAt = newer.updatedAt
  }
}