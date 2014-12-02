/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2014, Beangle Software.
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
package org.openurp.kernel.security.action

import org.beangle.security.blueprint.User
import org.beangle.security.blueprint.service.{ DataResolver, ProfileService }
import org.beangle.security.blueprint.service.impl.CsvDataResolver
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.kernel.security.helper.ProfileHelper
import org.openurp.kernel.security.model.UserProfileBean

/**
 * @author chaostone
 */
class ProfileAction(profileService: ProfileService) extends RestfulAction[UserProfileBean] {

  val dataResolver: DataResolver = CsvDataResolver
  def tip(): String = {
    forward()
  }
  def isAdmin(): Boolean = {
    SecurityContext.session.principal.id.asInstanceOf[java.lang.Long]
    true
  }

  /**
   * 查看限制资源界面
   */
  def info(): String = {
    val helper = new ProfileHelper(entityDao, profileService)
    val profiles = entityDao.get(classOf[User], getLongId("use")).profiles
    helper.populateInfo(profiles)
    return forward()
  }

  protected override def saveAndRedirect(profile: UserProfileBean): View = {
    val helper = new ProfileHelper(entityDao, profileService)
    helper.dataResolver = dataResolver
    helper.populateSaveInfo(profile, isAdmin())
    if (profile.properties.isEmpty) {
      if (profile.persisted) entityDao.remove(profile)
      redirect("info", "info.save.success")
    } else {
      entityDao.saveOrUpdate(profile)
      redirect("info", "info.save.success")
    }
  }

  protected override def editSetting(profile: UserProfileBean): Unit = {
    if (!profile.persisted) {
      profile.asInstanceOf[UserProfileBean].user = entityDao.get(classOf[User], getLongId("use"))
    }
    val helper = new ProfileHelper(entityDao, profileService)
    helper.fillEditInfo(profile, isAdmin())
  }

}
