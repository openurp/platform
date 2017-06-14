package org.openurp.platform.user.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ Coded, Named, Remark, TemporalOn, Updated }

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
