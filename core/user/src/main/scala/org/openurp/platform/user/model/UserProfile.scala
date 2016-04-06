package org.openurp.platform.user.model

import java.security.Principal
import java.{ util => ju }
import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.model.{ Coded, Enabled, Hierarchical, IntId, LongId, Named, TemporalOn, Updated }
import org.openurp.platform.config.model.App
import org.openurp.platform.config.model.Domain

/**
 * 用户在某个App上的配置
 */
class UserProfile extends LongId with Profile {
  var user: User = _
  var domain: Domain = _
  var properties = Collections.newMap[Dimension, String]
}
