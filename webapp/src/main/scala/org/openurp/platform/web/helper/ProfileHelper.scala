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
package org.openurp.platform.web.helper

import org.beangle.commons.bean.Properties
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.context.{ ActionContext, Params }
import org.openurp.platform.api.security.Securities
import org.openurp.platform.config.model.App
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.user.service.impl.CsvDataResolver
import org.openurp.platform.security.service.ProfileService
import org.openurp.platform.user.service.DataResolver
import org.openurp.platform.user.model.Dimension
import org.openurp.platform.user.model.User
import org.openurp.platform.user.model.Profile
import org.openurp.platform.user.model.UserProfile
import org.openurp.platform.config.model.Domain

class ProfileHelper(entityDao: EntityDao, profileService: ProfileService) {
  var dataResolver: DataResolver = CsvDataResolver
  /**
   * 查看限制资源界面
   */
  def populateInfo(profiles: Seq[_ <: Profile]) {
    val fieldMaps = new collection.mutable.HashMap[String, Map[String, AnyRef]]
    for (profile <- profiles) {
      val aoDimensions = new collection.mutable.HashMap[String, AnyRef]
      for ((field, value) <- profile.properties) {
        val fieldName = field.name
        if (Strings.isNotEmpty(value)) {
          if (null == field.source) {
            aoDimensions.put(fieldName, value)
          } else if (value.equals("*")) {
            aoDimensions.put(fieldName, "不限")
          } else {
            aoDimensions.put(fieldName, getProperty(profile, field))
          }
        }
      }
      fieldMaps.put(Properties.get[Any](profile, "id").toString, aoDimensions.toMap)
    }
    ActionContext.current.attribute("profiles", profiles)
    ActionContext.current.attribute("fieldMaps", fieldMaps)
  }

  def fillEditInfo(profile: Profile, isAdmin: Boolean, domain: Domain): Unit = {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val mngDimensions = new collection.mutable.HashMap[String, Object]
    val userDimensions = new collection.mutable.HashMap[String, Object]

    val myProfiles = entityDao.findBy(classOf[UserProfile], "user.code", List(Securities.user))
    val ignores = getIgnoreDimensions(myProfiles)
    ActionContext.current.attribute("ignoreDimensions", ignores)
    val userIgnoreDimensions = new collection.mutable.HashSet[Dimension]
    ActionContext.current.attribute("userIgnoreDimensions", userIgnoreDimensions)
    val fields = getDimensions(domain)
    ActionContext.current.attribute("fields", fields)
    for (field <- fields) {
      var mngDimensionValues = new collection.mutable.ListBuffer[Any]
      mngDimensionValues ++= profileService.getDimensionValues(field)
      if (!isAdmin) {
        mngDimensionValues --= (mngDimensionValues -- getMyProfileValues(myProfiles, field))
      } else ignores += field

      var fieldValue = ""
      val property = profile.getProperty(field) foreach { p => fieldValue = p }
      if ("*".equals(fieldValue)) userIgnoreDimensions.add(field)

      mngDimensions.put(field.name, mngDimensionValues)
      if (null == field.source) {
        userDimensions.put(field.name, fieldValue)
      } else {
        val p = getProperty(profile, field)
        if (null != p) userDimensions.put(field.name, p)
      }
    }
    ActionContext.current.attribute("mngDimensions", mngDimensions)
    ActionContext.current.attribute("userDimensions", userDimensions)
    ActionContext.current.attribute("profile", profile)
  }

  private def getMyProfileValues(profiles: Seq[Profile], field: Dimension): Seq[AnyRef] = {
    val values = new collection.mutable.ListBuffer[AnyRef]
    for (profile <- profiles) {
      profile.getProperty(field) foreach { value =>
        if (field.multiple) {
          values ++= getProperty(profile, field).asInstanceOf[Seq[AnyRef]]
        } else {
          values += getProperty(profile, field)
        }
      }
    }
    return values
  }

  private def getIgnoreDimensions(profiles: Seq[Profile]): collection.mutable.Set[Dimension] = {
    val ignores = new collection.mutable.HashSet[Dimension]
    for (profile <- profiles) {
      for ((field, value) <- profile.properties) {
        if ("*".equals(value)) ignores.add(field)
      }
    }
    ignores
  }

  private def getProperty(profile: Profile, field: Dimension): AnyRef = {
    profile.getProperty(field) match {
      case Some(p) =>
        if ("*" == p) {
          profileService.getDimensionValues(field)
        } else {
          if (field.keyName == null) {
            Strings.split(p, ",").toList
          } else {
            var values = profileService.getDimensionValues(field)
            val myValues = Strings.split(p, ",").toSet
            values = values.filter { v =>
              myValues.contains(Properties.get(v, field.keyName))
            }
            values
          }
        }
      case None => null
    }
  }
  private def getDimensions(domain: Domain): Seq[Dimension] = {
    entityDao.search(OqlBuilder.from(classOf[Dimension], "dim").where(":app in elements(dim.domains)", domain))
  }

  def populateSaveInfo(profile: Profile, isAdmin: Boolean, domain: Domain) {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val myProfiles = entityDao.findBy(classOf[UserProfile], "user.code", List(Securities.user))
    val ignoreDimensions = getIgnoreDimensions(myProfiles)
    for (field <- getDimensions(domain)) {
      val values = Params.getAll(field.name).asInstanceOf[Iterable[String]]
      if ((ignoreDimensions.contains(field) || isAdmin) && Params.getBoolean("ignoreDimension" + field.id).getOrElse(false)) {
        profile.setProperty(field, "*")
      } else {
        if (null == values || values.size == 0) {
          profile.setProperty(field, null)
        } else {
          profile.setProperty(field, Strings.join(values.toSeq: _*))
        }
      }
    }
  }
}
