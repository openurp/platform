package org.openurp.platform.security.model

import java.{ util => ju }
import org.beangle.commons.lang.Strings
import org.beangle.data.model.{ Coded, Enabled, Hierarchical, IntId, LongId, Named, StringId, TemporalOn, Updated }
import org.beangle.security.blueprint.{ Field, Profile, Role, User }
import org.beangle.security.session.SessionProfile
import org.openurp.platform.kernel.app.model.App
import org.beangle.data.model.annotation.code
import org.beangle.commons.lang.Numbers

object UrpMember {
  object Ship extends Enumeration(1) {
    type Ship = Value
    val Member = Value("Member")
    val Granter = Value("Granter")
    val Manager = Value("Manager")
  }
}

class UrpField extends IntId with Named with Field {
  var title: String = _
  var source: String = _
  var multiple: Boolean = _
  var required: Boolean = _
  var typeName: String = _
  var keyName: String = _
  var properties: String = _
}

class UrpUserProfile extends LongId with Profile {
  var user: User = _
  var properties = new collection.mutable.HashMap[Field, String]
}

class UrpRole extends IntId with Named with Updated with Enabled
  with Hierarchical[org.beangle.security.blueprint.Role] with Profile with Role {

  var app: App = _
  var properties: collection.mutable.Map[Field, String] = new collection.mutable.HashMap[Field, String]
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
  var category: String = _
  var capacity: Int = _
  var maxSession: Int = _
  var timeout: Short = _
}