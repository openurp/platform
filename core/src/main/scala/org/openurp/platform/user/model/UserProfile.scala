package org.openurp.platform.user.model

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.openurp.platform.config.model.Domain

/**
 * 用户在某个App上的配置
 */
class UserProfile extends LongId with Profile {
  var user: User = _
  var domain: Domain = _
  var properties = Collections.newMap[Dimension, String]
}
