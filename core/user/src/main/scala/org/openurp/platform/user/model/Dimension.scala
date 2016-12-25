package org.openurp.platform.user.model

import org.beangle.commons.collection.Collections
import org.beangle.commons.model.{ IntId, Named }
import org.openurp.platform.config.model.Domain

/**
 * @author chaostone
 */
class Dimension extends IntId with Named {
  var title: String = _
  var source: String = _
  var multiple: Boolean = _
  var required: Boolean = _
  var typeName: String = _
  var keyName: String = _
  var properties: String = _
  var domains: collection.mutable.Seq[Domain] = Collections.newBuffer[Domain]
}
