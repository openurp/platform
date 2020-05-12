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
package org.openurp.platform.admin.action.blob

import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.blob.model.Profile
import org.openurp.platform.config.model.App

class ProfileAction extends RestfulAction[Profile] {

  override def editSetting(entity: Profile): Unit = {
    val query=OqlBuilder.from(classOf[App],"app").orderBy("indexno")
    val apps = entityDao.search(query)

    val users = Strings.split(entity.users.getOrElse("")).toSet
    val profileApps = apps.filter(x => users.contains(x.name))
    put("profileApps", profileApps);
    put("alternativeApps", apps.toBuffer.subtractAll(profileApps))
  }

  override def saveAndRedirect(entity: Profile): View = {
    val ids = intIds("app")
    if (ids.isEmpty) {
      entity.users = None
    } else {
      val apps = entityDao.find(classOf[App], ids)
      entity.users = Some(apps.map(_.name).mkString(","))
    }
    super.saveAndRedirect(entity)
  }
}
