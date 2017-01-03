package org.openurp.platform.user.model

import org.beangle.commons.model.{ Coded, IntId, Named, TemporalOn, Updated }
import org.beangle.commons.model.Remark

/**
 * 用户分类
 * @author chaostone
 */
class UserCategory extends IntId with Coded with TemporalOn with Named with Updated with Remark {
  var enName: String = _
  override def toString = {
    name
  }
}
