package org.openurp.platform.config.model

import java.security.Principal
import org.beangle.commons.collection.Collections
import org.beangle.data.model.{ Enabled, IntId, Named }
import org.beangle.data.model.Hierarchical

class Domain extends IntId with Named with Hierarchical[Domain] {
  var title: String = _

}
