package org.openurp.platform.user.model

import java.security.Principal

import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.model.{ Enabled, Hierarchical, IntId, Named, Updated }
import org.beangle.data.model.Remark

/**
 * @author chaostone
 */

class Group extends IntId with Named with Updated with Enabled with Hierarchical[Group] with Profile with Principal with Remark {
  var properties: collection.mutable.Map[Dimension, String] = new collection.mutable.HashMap[Dimension, String]
  var creator: User = _
  var members: collection.mutable.Seq[GroupMember] = new collection.mutable.ListBuffer[GroupMember]

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