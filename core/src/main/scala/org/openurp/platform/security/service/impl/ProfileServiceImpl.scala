/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2017, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.security.service.impl

import org.openurp.platform.user.model.{ Dimension, Profile, User }
import org.openurp.platform.security.model.FuncResource
import org.openurp.platform.security.service.ProfileService
import org.beangle.commons.bean.Properties
import org.beangle.commons.lang.Strings
import org.beangle.commons.collection.Collections
import org.openurp.app.util.JSON

class ProfileServiceImpl extends ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile] = {
    Seq.empty
  }

  def getDimensionValues(field: Dimension, keys: Any*): Seq[Any] = {
    val source = field.source
    if (source.startsWith("json:")) {
      val json = source.substring(5)
      val keySet = keys.toSet
      JSON.parse(json).asInstanceOf[Seq[Any]].filter { x => Properties.get(x, field.keyName) }
    } else if (source.startsWith("csv:")) {
      val csv = source.substring(4)
      val lines = Strings.split(csv, "\n")
      val start = (0 until lines.length) find (x => Strings.isNotBlank(lines(x)))
      val heads = Strings.split(lines(start.get), ",")
      val data = Collections.newBuffer[org.beangle.commons.collection.Properties]
      var i = start.get + 1
      while (i < lines.length) {
        if (!Strings.isBlank(lines(i))) {
          val datas = Strings.split(lines(i), ",")
          val p = new org.beangle.commons.collection.Properties
          for (j <- 0 until heads.length) {
            p.put(heads(j), datas(j))
          }
          data += p
        }
        i += 1
      }
      data
    } else {
      Seq.empty
    }
  }

  def getDimension(fieldName: String): Dimension = {
    null
  }

  def get(id: java.lang.Long): Profile = {
    null
  }
}
