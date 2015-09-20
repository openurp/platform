package org.openurp.platform.security.service.impl

import org.openurp.platform.security.model.{ Dimension, FuncResource, Profile, User }
import org.openurp.platform.security.service.ProfileService

class ProfileServiceImpl extends ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile] = {
    Seq.empty
  }

  def getDimensionValues(field: Dimension, keys: Any*): Seq[Any] = {
    List(Map("id" -> 1L, "code" -> "001", "name" -> "会计学院"), Map("id" -> 2L, "code" -> "002", "name" -> "国际金融学院"))
    //    Seq.empty
  }

  def getDimension(fieldName: String): Dimension = {
    null
  }

  def get(id: java.lang.Long): Profile = {
    null
  }
}