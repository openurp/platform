package org.openurp.platform.user.model

import java.security.Principal

import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named, Updated }

/**
 * @author chaostone
 */

class Role extends IntId with Named with Updated with Enabled with Hierarchical[Role] with Profile with Principal {
  var properties: collection.mutable.Map[Dimension, String] = new collection.mutable.HashMap[Dimension, String]
  var creator: User = _
  var members: collection.mutable.Seq[RoleMember] = new collection.mutable.ListBuffer[RoleMember]
  var remark: String = _

  override def getName: String = {
    name
  }

  def index: Int = {
    if (Strings.isEmpty(indexno)) return 1;
    val lastPart = Strings.substringAfterLast(indexno, ".")
    if (lastPart.isEmpty) Numbers.toInt(indexno) else Numbers.toInt(lastPart)
  }

  def this(id: Int, name: String) {
    this()
    this.id = id
    this.name = name
  }
}