/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.bulletin.model

import java.io.{ByteArrayOutputStream, InputStream}
import java.time.Instant

import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated

object Attachment {
  def apply(name: String, is: InputStream): Attachment = {
    val a = new Attachment
    a.fileName = name

    val buf = new ByteArrayOutputStream
    IOs.copy(is, buf)
    a.content = buf.toByteArray
    a.size = a.content.length
    a.updatedAt = Instant.now
    a
  }
}

class Attachment extends LongId with Updated {

  var size: Int = _

  var content: Array[Byte] = _

  var fileName: String = _

  def ext: String = {
    Strings.substringAfterLast(fileName, ".")
  }

  def merge(newer: Attachment): Unit = {
    this.size = newer.size
    this.fileName = newer.fileName
    this.content = newer.content
    this.updatedAt = newer.updatedAt
  }
}
