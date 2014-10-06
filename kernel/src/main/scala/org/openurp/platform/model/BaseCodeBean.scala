package org.openurp.platform.model

import org.beangle.data.model.bean.{ CodedBean, IntIdBean, NamedBean, TemporalOnBean, UpdatedBean }

object BaseCodeCategory {

  //  基础代码种类
  val Nation = "nation"

  val Ministry = "ministry"

  val School = "school"
}

abstract class BaseCodeBean extends IntIdBean with CodedBean with TemporalOnBean with NamedBean with UpdatedBean {
  var enName: String = _
  var remark: String = _
}