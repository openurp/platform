package org.openurp.platform.security.service.internal

import org.beangle.security.blueprint.service.ProfileService
import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.User
import org.beangle.security.blueprint.Profile
import org.beangle.security.blueprint.Field

class ProfileServiceImpl extends ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile] = {
    Seq.empty
  }

  def getFieldValues(field: Field, keys: Any*): Seq[Any] = {
    Seq.empty
  }

  def getField(fieldName: String): Field = {
    null
  }

  def get(id: java.lang.Long): Profile = {
    null
  }
}