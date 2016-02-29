package org.openurp.platform.user.model

import org.beangle.data.model.{ Coded, IntId, Named, TemporalOn, Updated }

/**
 * 用户分类
 * @author chaostone
 */
class UserCategory extends IntId with Coded with TemporalOn with Named with Updated {
  var enName: String = _
  var remark: String = _
  override def toString = {
    name
  }
}
