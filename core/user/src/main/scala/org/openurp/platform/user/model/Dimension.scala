package org.openurp.platform.user.model

import org.beangle.data.model.{ IntId, Named }
import org.beangle.commons.collection.Collections
import org.openurp.platform.config.model.App

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
  //FIXME
  var apps: collection.mutable.Seq[App] = Collections.newBuffer[App]

}
