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
package org.openurp.platform.security.helper

import org.beangle.commons.bean.Properties
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.context.{ ActionContextHolder, Params }
import org.openurp.platform.security.model.{ Dimension, Profile, User }
import org.openurp.platform.security.service.{ DataResolver, ProfileService }
import org.openurp.platform.api.security.Securities
import org.openurp.platform.security.service.impl.CsvDataResolver
import org.openurp.platform.kernel.model.App
import org.beangle.data.dao.OqlBuilder

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
    ActionContextHolder.context.attribute("profiles", profiles)
    ActionContextHolder.context.attribute("fieldMaps", fieldMaps)
  }

  def fillEditInfo(profile: Profile, isAdmin: Boolean, app: App): Unit = {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val mngDimensions = new collection.mutable.HashMap[String, Object]
    val aoDimensions = new collection.mutable.HashMap[String, Object]

    val myProfiles = me.profiles
    val ignores = getIgnoreDimensions(myProfiles)
    ActionContextHolder.context.attribute("ignoreDimensions", ignores)
    val holderIgnoreDimensions = new collection.mutable.HashSet[Dimension]
    ActionContextHolder.context.attribute("holderIgnoreDimensions", holderIgnoreDimensions)
    val fields = getDimensions(app)
    ActionContextHolder.context.attribute("fields", fields)
    for (field <- fields) {
      var mngDimensionValues = new collection.mutable.ListBuffer[Any]
      mngDimensionValues ++= profileService.getDimensionValues(field)
      if (!isAdmin) {
        mngDimensionValues --= (mngDimensionValues -- getMyProfileValues(myProfiles, field))
      } else ignores += field

      var fieldValue = ""
      val property = profile.getProperty(field) foreach { p => fieldValue = p }
      if ("*".equals(fieldValue)) holderIgnoreDimensions.add(field)

      mngDimensions.put(field.name, mngDimensionValues)
      if (null == field.source) {
        aoDimensions.put(field.name, fieldValue)
      } else {
        val p = getProperty(profile, field)
        if (null != p) aoDimensions.put(field.name, p)
      }
    }
    ActionContextHolder.context.attribute("mngDimensions", mngDimensions)
    ActionContextHolder.context.attribute("aoDimensions", aoDimensions)
    ActionContextHolder.context.attribute("profile", profile)
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
        if ("*" == p) profileService.getDimensionValues(field)
        else dataResolver.unmarshal(field, p)
      case None => null
    }
  }
  private def getDimensions(app: App): Seq[Dimension] = {
    entityDao.search(OqlBuilder.from(classOf[Dimension], "dim").where(":app in elements(dim.apps)", app))
  }

  def populateSaveInfo(profile: Profile, isAdmin: Boolean, app: App) {
    val me = entityDao.findBy(classOf[User], "code", List(Securities.user)).head
    val myProfiles = me.profiles
    val ignoreDimensions = getIgnoreDimensions(myProfiles)
    for (field <- getDimensions(app: App)) {
      val values = Params.getAll(field.name).asInstanceOf[Iterable[String]]
      if ((ignoreDimensions.contains(field) || isAdmin) && Params.getBoolean("ignoreDimension" + field.id).getOrElse(false)) {
        profile.setProperty(field, "*")
      } else {
        if (null == values || values.size == 0) {
          profile.setProperty(field, null)
        } else {
          var storedValue: String = null
          if (null != field.keyName) {
            val keys = new collection.mutable.HashSet[String]
            keys ++= values
            val allValues: Seq[_] = profileService.getDimensionValues(field) match {
              case originValues: Seq[_] => originValues
              case singleValue => List(singleValue)
            }
            val filtered = allValues.filter { v =>
              keys.contains(String.valueOf(Properties.get[Any](v, field.keyName)))
            }
            storedValue = dataResolver.marshal(field, filtered)
          } else {
            storedValue = Strings.join(values.toSeq: _*)
          }
          profile.setProperty(field, storedValue)
        }
      }
    }
  }
}
