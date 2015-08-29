package org.openurp.platform.security.service

import org.openurp.platform.security.model.{ Dimension, FuncResource, Profile, User }

trait ProfileService {

  def getProfiles(user: User, resource: FuncResource): Seq[Profile]

  def getDimensionValues(field: Dimension, keys: Any*): Seq[Any]

  def getDimension(fieldName: String): Dimension

  def get(id: java.lang.Long): Profile

}
