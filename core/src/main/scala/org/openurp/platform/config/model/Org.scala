package org.openurp.platform.config.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ Named, Remark }

class Org extends IntId with Named with Remark {

  var code: String = _
  var logoUrl: String = _
  var wwwUrl: String = _

}