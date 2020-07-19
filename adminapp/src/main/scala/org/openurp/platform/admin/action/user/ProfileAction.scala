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
package org.openurp.platform.admin.action.user

import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.admin.helper.ProfileHelper
import org.openurp.platform.config.service.DomainService
import org.openurp.platform.security.service.ProfileService
import org.openurp.platform.user.model.UserProfile
import org.openurp.platform.user.service.impl.CsvDataResolver
import org.openurp.platform.user.service.{DataResolver, DimensionService, UserService}

/**
 * @author chaostone
 */
class ProfileAction(profileService: ProfileService) extends RestfulAction[UserProfile] {

  var userService: UserService = _
  var dimensionService: DimensionService = _
  var domainService: DomainService = _
  val dataResolver: DataResolver = CsvDataResolver

  protected override def indexSetting(): Unit = {
    val userId = getLong("profile.user.id").get
    val helper = new ProfileHelper(entityDao, profileService, dimensionService)
    val builder = OqlBuilder.from(classOf[UserProfile], "up")
      .where("up.user.id=:userId", userId)
      .where("up.domain=:domain", domainService.getDomain)
    val profiles = entityDao.search(builder)
    helper.populateInfo(profiles)
  }

  def tip(): View = {
    forward()
  }

  @ignore
  protected override def simpleEntityName: String = {
    "profile"
  }

  @ignore
  protected override def saveAndRedirect(profile: UserProfile): View = {
    val helper = new ProfileHelper(entityDao, profileService, dimensionService)
    helper.dataResolver = dataResolver
    //            if (!d.multiple && dvalues.nonEmpty) {
    //              dvalues.head match {
    //                case properties: Properties =>
    //                  if (properties.contains("name")) {
    //                    pname.append(properties("name"))
    //                  } else if (properties.contains("title")) {
    //                    pname.append(properties("title"))
    //                  }
    //                case _ =>
    //              }
    //            }
    //FIXME
    helper.populateSaveInfo(profile, isAdmin = true)
    profile.domain=domainService.getDomain
    if (profile.properties.isEmpty) {
      if (profile.persisted) {
        entityDao.remove(profile)
      }
      redirect("index", s"&profile.user.id=${profile.user.id}", "info.save.success")
    } else {
      entityDao.saveOrUpdate(profile)
      redirect("index", s"&profile.user.id=${profile.user.id}", "info.save.success")
    }
  }

  @ignore
  protected override def removeAndRedirect(entities: Seq[UserProfile]): View = {
    val profile = entities.head
    try {
      //entityDao.remove(entities)
      redirect("index", s"&profile.user.id=${profile.user.id}", "info.remove.success")
    } catch {
      case e: Exception =>
        logger.info("removeAndForwad failure", e)
        redirect("appinfo", s"&profile.user.id=${profile.user.id}", "info.delete.failure")
    }
  }

  protected override def editSetting(profile: UserProfile): Unit = {
    val helper = new ProfileHelper(entityDao, profileService, dimensionService)
    if (null == profile.user) profile.user = userService.get(Securities.user).get
    helper.fillEditInfo(profile, isAdmin = true)
  }

}
