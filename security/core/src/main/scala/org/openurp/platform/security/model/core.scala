package org.openurp.platform.security.model

import java.{ util => ju }

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.model.{ Coded, Enabled, Hierarchical, IntId, LongId, Named, StringId, TemporalOn, Updated }
import org.beangle.security.blueprint.{ Dimension, Profile, Role, User }
import org.beangle.security.session.SessionProfile
import org.openurp.platform.kernel.model.App

import UrpMember.Ship.{ Granter, Manager, Member, Ship }

object UrpMember {
  object Ship extends Enumeration(1) {
    type Ship = Value
    val Member = Value("Member")
    val Granter = Value("Granter")
    val Manager = Value("Manager")
  }
}

class UrpDimension extends IntId with Named with Dimension {
  var title: String = _
  var source: String = _
  var multiple: Boolean = _
  var required: Boolean = _
  var typeName: String = _
  var keyName: String = _
  var properties: String = _
  var apps = Collections.newBuffer[App]
}

/**
 * 用户在某个App上的配置
 */
class UrpUserProfile extends LongId with Profile {
  var user: User = _
  var app: App = _
  var properties = new collection.mutable.HashMap[Dimension, String]
}
class AppDimension {}
class UrpRole extends IntId with Named with Updated with Enabled
    with Hierarchical[org.beangle.security.blueprint.Role] with Profile with Role {
  var app: App = _
  var properties: collection.mutable.Map[Dimension, String] = new collection.mutable.HashMap[Dimension, String]
  var creator: User = _
  var members: collection.mutable.Seq[UrpMember] = new collection.mutable.ListBuffer[UrpMember]
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

/**
 * 人员分类
 */
class UserCategory extends IntId with Coded with TemporalOn with Named with Updated {
  var enName: String = _
  var remark: String = _
}

class UrpUser extends LongId with Coded with Named with Updated with TemporalOn with Enabled with User {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: ju.Date = _
  var remark: String = _
  var members: collection.mutable.Buffer[UrpMember] = new collection.mutable.ListBuffer[UrpMember]
  var profiles: collection.mutable.Buffer[Profile] = new collection.mutable.ListBuffer[Profile]
  var properties = Collections.newMap[Dimension, String]
  var category: UserCategory = _

  def credential = password

  def roles = members.filter(m => m.member).map(m => m.role)

  def accountExpired: Boolean = {
    null != endOn && (new ju.Date).after(endOn)
  }

  def credentialExpired: Boolean = {
    if (null == passwordExpiredOn) false
    else (new ju.Date).after(passwordExpiredOn)
  }
  override def getName: String = {
    name
  }
}

class UrpMember extends LongId with Updated {
  var user: User = _
  var role: Role = _
  var member: Boolean = _
  var granter: Boolean = _
  var manager: Boolean = _

  def this(user: User, role: Role) {
    this()
    this.user = user
    this.role = role
  }

  import UrpMember.Ship._
  def is(ship: Ship): Boolean = {
    ship match {
      case Member => member
      case Manager => manager
      case Granter => granter
      case _ => false
    }
  }
}

class UrpSessionProfile extends StringId with SessionProfile {
  var app: App = _
  var role: Role = _
  var capacity: Int = _
  var maxSession: Int = _
  var timeout: Short = _
}