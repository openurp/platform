package org.openurp.kernel.security.model

import java.{ util => ju }
import org.beangle.commons.lang.Strings
import org.beangle.data.model.bean.{ CodedBean, EnabledBean, HierarchicalBean, IntIdBean, LongIdBean, NamedBean, StringIdBean, TemporalOnBean, UpdatedBean }
import org.beangle.security.blueprint.{ Field, Profile, Role, User }
import org.beangle.security.session.SessionProfile
import org.openurp.kernel.app.App
import org.openurp.platform.code.BaseCodeBean
import org.beangle.data.model.annotation.code

object Member {
  object Ship extends Enumeration(1) {
    type Ship = Value
    val IsMember = Value("Member")
    val IsGranter = Value("Granter")
    val IsManager = Value("Manager")
  }
}

class FieldBean extends IntIdBean with NamedBean with Field {
  var title: String = _
  var source: String = _
  var multiple: Boolean = _
  var required: Boolean = _
  var typeName: String = _
  var keyName: String = _
  var properties: String = _
}

class UserProfileBean extends LongIdBean with Profile {
  var user: User = _
  var properties = new collection.mutable.HashMap[Field, String]
}

class UrpRoleBean extends IntIdBean with NamedBean with UpdatedBean with EnabledBean
  with HierarchicalBean[org.beangle.security.blueprint.Role] with Profile with Role {

  var app: App = _
  var properties: collection.mutable.Map[Field, String] = new collection.mutable.HashMap[Field, String]
  var creator: User = _
  var members: collection.mutable.Seq[Member] = new collection.mutable.ListBuffer[Member]
  var remark: String = _
  override def getName: String = {
    name
  }

  def index: Int = {
    if (Strings.isEmpty(indexno)) return 1;
    val lastPart = Strings.substringAfterLast(indexno, ".")
    if (lastPart.isEmpty) Integer.valueOf(indexno) else Integer.valueOf(lastPart)
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
class UserCategoryBean extends BaseCodeBean

class UrpUserBean extends LongIdBean with CodedBean with NamedBean with UpdatedBean with TemporalOnBean with EnabledBean with User {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: ju.Date = _
  var remark: String = _
  var members: collection.mutable.Buffer[Member] = new collection.mutable.ListBuffer[Member]
  var profiles: collection.mutable.Buffer[Profile] = new collection.mutable.ListBuffer[Profile]
  var category: UserCategoryBean = _
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

class Member extends LongIdBean with UpdatedBean {
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

  import Member.Ship._
  def is(ship: Ship): Boolean = {
    ship match {
      case IsMember => member
      case IsManager => manager
      case IsGranter => granter
      case _ => false
    }
  }
}

class SessionProfileBean extends StringIdBean with SessionProfile {
  var category: String = _
  var capacity: Int = _
  var maxSession: Int = _
  var timeout: Short = _
}