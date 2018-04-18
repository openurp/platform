/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.config.model

import java.security.Principal

import org.beangle.commons.collection.Collections
import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ Enabled, Named, Remark }

class App extends IntId with Named with Enabled with Ordered[App] with Principal with Remark {
  var secret: String = _
  var title: String = _
  var datasources = Collections.newBuffer[DataSource]
  var appType: String = _
  var url: String = _
  var logoUrl: Option[String] = None
  var indexno: String = _
  var domain: Domain = _

  def getName = name

  def fullTitle = domain.title + " " + title

  override def compare(m: App): Int = {
    indexno.compareTo(m.indexno)
  }

}