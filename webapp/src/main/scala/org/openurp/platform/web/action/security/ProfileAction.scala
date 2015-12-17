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
package org.openurp.platform.web.action.security

import org.beangle.commons.lang.Numbers
import org.beangle.data.dao.{ Operation, OqlBuilder }
import org.beangle.webmvc.api.annotation.{ ignore, param }
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.web.helper.ProfileHelper
import org.openurp.platform.security.model.UserProfile
import org.openurp.platform.user.service.impl.CsvDataResolver
import org.openurp.platform.user.service.UserService
import org.openurp.platform.user.service.DataResolver
import org.openurp.platform.security.service.ProfileService

/**
 * @author chaostone
 */
class ProfileAction(profileService: ProfileService) extends RestfulAction[UserProfile] {

  var userService: UserService = _
  val dataResolver: DataResolver = CsvDataResolver
  def tip(): String = {
    forward()
  }

  @ignore
  protected override def simpleEntityName: String = {
    "profile"
  }
  /**
   * 查看限制资源界面
   */
  def appinfo(@param("profile.user.id") userId: String, @param("profile.app.id") appId: String): String = {
    val helper = new ProfileHelper(entityDao, profileService)
    val builder = OqlBuilder.from(classOf[UserProfile], "up").where("up.user.id=:userId and up.app.id=:appId", Numbers.toLong(userId), Numbers.toInt(appId))
    val profiles = entityDao.search(builder)
    helper.populateInfo(profiles)
    return forward()
  }

  protected override def saveAndRedirect(profile: UserProfile): View = {
    val helper = new ProfileHelper(entityDao, profileService)
    helper.dataResolver = dataResolver
    //FIXME
    helper.populateSaveInfo(profile, true, profile.app)
    if (profile.properties.isEmpty) {
      if (profile.persisted) {
        entityDao.remove(profile)
      }
      redirect("appinfo", s"&profile.app.id=${profile.app.id}&profile.user.id=${profile.user.id}", "info.save.success")
    } else {
      entityDao.saveOrUpdate(profile)
      redirect("appinfo", s"&profile.app.id=${profile.app.id}&profile.user.id=${profile.user.id}", "info.save.success")
    }
  }
  @ignore
  protected override def removeAndRedirect(entities: Seq[UserProfile]): View = {
    val profile = entities.head
    try {
      entityDao.execute(Operation.saveOrUpdate(profile.user).remove(entities))
      //      remove(entities)
      redirect("appinfo", s"&profile.app.id=${profile.app.id}&profile.user.id=${profile.user.id}", "info.remove.success")
    } catch {
      case e: Exception => {
        logger.info("removeAndForwad failure", e)
        redirect("appinfo", s"&profile.app.id=${profile.app.id}&profile.user.id=${profile.user.id}", "info.delete.failure")
      }
    }
  }
  protected override def editSetting(profile: UserProfile): Unit = {
    val helper = new ProfileHelper(entityDao, profileService)
    helper.fillEditInfo(profile, true, profile.app)
  }

}
