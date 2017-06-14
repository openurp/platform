package org.openurp.platform.config.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.{ Hierarchical, Named }

class Domain extends IntId with Named with Hierarchical[Domain] {
  var title: String = _

}
