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
package org.openurp.security.helper

import org.beangle.commons.bean.PropertyUtils
import org.beangle.commons.lang.Strings
import org.beangle.data.model.dao.EntityDao
import org.beangle.security.blueprint.{Field, Profile, User}
import org.beangle.security.blueprint.service.{DataResolver, ProfileService}
import org.beangle.security.context.SecurityContext
import org.beangle.webmvc.api.context.{ContextHolder, Params}

class ProfileHelper(entityDao: EntityDao, profileService: ProfileService) {
  var dataResolver: DataResolver = _
  /**
   * 查看限制资源界面
   */
  def populateInfo(profiles: Seq[_ <: Profile]) {
    val fieldMaps = new collection.mutable.HashMap[String, Map[String, AnyRef]]
    for (profile <- profiles) {
      val aoFields = new collection.mutable.HashMap[String, AnyRef]
      for ((field, value) <- profile.properties) {
        val fieldName = field.name
        if (Strings.isNotEmpty(value)) {
          if (null == field.source) {
            aoFields.put(fieldName, value)
          } else if (value.equals("*")) {
            aoFields.put(fieldName, "不限")
          } else {
            aoFields.put(fieldName, getProperty(profile, field))
          }
        }
      }
      fieldMaps.put(PropertyUtils.getProperty(profile, "id").toString, aoFields.toMap)
    }
    ContextHolder.context.attribute("profiles", profiles)
    ContextHolder.context.attribute("fieldMaps", fieldMaps)
  }

  def fillEditInfo(profile: Profile, isAdmin: Boolean): Unit = {
    val userId = SecurityContext.session.principal.id.asInstanceOf[java.lang.Long]
    val mngFields = new collection.mutable.HashMap[String, Object]
    val aoFields = new collection.mutable.HashMap[String, Object]

    val myProfiles = entityDao.get(classOf[User], userId).profiles
    val ignores = getIgnoreFields(myProfiles)
    ContextHolder.context.attribute("ignoreFields", ignores)
    val holderIgnoreFields = new collection.mutable.HashSet[Field]
    ContextHolder.context.attribute("holderIgnoreFields", holderIgnoreFields)
    val fields = entityDao.getAll(classOf[Field])
    ContextHolder.context.attribute("fields", fields)
    for (field <- fields) {
      var mngFieldValues = new collection.mutable.ListBuffer[Any]
      mngFieldValues ++= profileService.getFieldValues(field)
      if (!isAdmin) {
        mngFieldValues --= (mngFieldValues -- getMyProfileValues(myProfiles, field))
      } else ignores += field

      var fieldValue = ""
      val property = profile.getProperty(field) foreach { p => fieldValue = p }
      if ("*".equals(fieldValue)) holderIgnoreFields.add(field)

      mngFields.put(field.name, mngFieldValues)
      if (null == field.source) {
        aoFields.put(field.name, fieldValue)
      } else {
        aoFields.put(field.name, getProperty(profile, field))
      }
    }
    ContextHolder.context.attribute("mngFields", mngFields)
    ContextHolder.context.attribute("aoFields", aoFields)
    ContextHolder.context.attribute("profile", profile)
  }

  private def getMyProfileValues(profiles: Seq[Profile], field: Field): Seq[AnyRef] = {
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

  private def getIgnoreFields(profiles: Seq[Profile]): collection.mutable.Set[Field] = {
    val ignores = new collection.mutable.HashSet[Field]
    for (profile <- profiles) {
      for ((field, value) <- profile.properties) {
        if ("*".equals(value)) ignores.add(field)
      }
    }
    ignores
  }

  private def getProperty(profile: Profile, field: Field): AnyRef = {
    profile.getProperty(field) match {
      case Some(p) =>
        if ("*" == p) profileService.getFieldValues(field)
        else dataResolver.unmarshal(field, p)
      case None => null
    }
  }
  def populateSaveInfo(profile: Profile, isAdmin: Boolean) {
    val userId = SecurityContext.session.principal.id.asInstanceOf[java.lang.Long]
    val myProfiles = entityDao.get(classOf[User], userId).profiles
    val ignoreFields = getIgnoreFields(myProfiles)
    for (field <- entityDao.getAll(classOf[Field])) {
      val values = Params.getAll(field.name).asInstanceOf[Array[String]]
      if ((ignoreFields.contains(field) || isAdmin) && Params.getBoolean("ignoreField" + field.id).getOrElse(false)) {
        profile.setProperty(field, "*")
      } else {
        if (null == values || values.length == 0) {
          profile.setProperty(field, null)
        } else {
          var storedValue: String = null
          if (null != field.keyName) {
            val keys = new collection.mutable.HashSet[String]
            keys ++= values
            val allValues: Seq[Object] = profileService.getFieldValues(field) match {
              case originValues: Seq[Object] => originValues
              case singleValue => List(singleValue)
            }
            val filtered = allValues.filter { v =>
              keys.contains(String.valueOf(PropertyUtils.getProperty[Any](v, field.keyName)))
            }
            storedValue = dataResolver.marshal(field, filtered)
          } else {
            storedValue = Strings.join(values: _*)
          }
          profile.setProperty(field, storedValue)
        }
      }
    }
  }
}
