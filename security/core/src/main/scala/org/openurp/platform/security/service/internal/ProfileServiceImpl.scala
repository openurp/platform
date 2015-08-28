package org.openurp.platform.security.service.internal

import org.beangle.security.blueprint.service.ProfileService
import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.User
import org.beangle.security.blueprint.Profile
import org.beangle.security.blueprint.Dimension

class ProfileServiceImpl extends ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile] = {
    Seq.empty
  }

  def getDimensionValues(field: Dimension, keys: Any*): Seq[Any] = {
    Seq.empty
  }

  def getDimension(fieldName: String): Dimension = {
    null
  }

  def get(id: java.lang.Long): Profile = {
    null
  }
}