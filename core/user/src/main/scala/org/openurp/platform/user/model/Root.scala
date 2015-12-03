package org.openurp.platform.user.model

import org.beangle.data.model.IntId
import org.beangle.data.model.Updated
import org.openurp.platform.config.model.App

/**
 * @author chaostone
 */
class Root extends IntId with Updated {
  var app: App = _
  var user: User = _
}
