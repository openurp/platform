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
package org.openurp.platform.security.action

import org.beangle.commons.lang.Numbers
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.security.helper.ProfileHelper
import org.openurp.platform.security.model.{ User, UserProfile }
import org.openurp.platform.security.service.{ DataResolver, ProfileService }
import org.openurp.platform.security.service.impl.CsvDataResolver
import org.openurp.platform.security.service.UserService

/**
 * @author chaostone
 */
class ProfileAction(profileService: ProfileService) extends RestfulAction[UserProfile] {

  var userService: UserService = _
  val dataResolver: DataResolver = CsvDataResolver
  def tip(): String = {
    forward()
  }

  /**
   * 查看限制资源界面
   */
  @mapping(value = "{id}")
  override def info(@param("userId") userId: String): String = {
    val helper = new ProfileHelper(entityDao, profileService)
    val profiles = entityDao.get(classOf[User], Numbers.toLong(userId)).profiles
    helper.populateInfo(profiles)
    return forward()
  }

  protected override def saveAndRedirect(profile: UserProfile): View = {
    val helper = new ProfileHelper(entityDao, profileService)
    helper.dataResolver = dataResolver
    //FIXME
    helper.populateSaveInfo(profile, true)
    if (profile.properties.isEmpty) {
      if (profile.persisted) entityDao.remove(profile)
      redirect("info", "info.save.success")
    } else {
      entityDao.saveOrUpdate(profile)
      redirect("info", "info.save.success")
    }
  }

  protected override def editSetting(profile: UserProfile): Unit = {
    if (!profile.persisted) {
      profile.asInstanceOf[UserProfile].user = entityDao.get(classOf[User], getLongId("use"))
    }
    val helper = new ProfileHelper(entityDao, profileService)
    //FIXME
    helper.fillEditInfo(profile, true)
  }

}
