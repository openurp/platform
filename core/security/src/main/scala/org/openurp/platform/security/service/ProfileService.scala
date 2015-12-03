package org.openurp.platform.security.service

import org.openurp.platform.user.model.{ Dimension, Profile, User }
import org.openurp.platform.security.model.FuncResource


trait ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile]

  def getDimensionValues(field: Dimension, keys: Any*): Seq[Any]

  def getDimension(fieldName: String): Dimension

  def get(id: java.lang.Long): Profile

}
