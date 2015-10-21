package org.openurp.platform.security.model

import java.security.Principal
import java.{ util => ju }

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.{ Numbers, Strings }
import org.beangle.data.model.{ Coded, Enabled, Hierarchical, IntId, LongId, Named, TemporalOn, Updated }
import org.beangle.security.session.profile.SessionProfile
import org.openurp.platform.kernel.model.App

import MemberShip.{ Granter, Manager, Member, Ship }

object Property {
  val All = "*"
}
object MemberShip extends Enumeration(1) {
  type Ship = Value
  val Member = Value("Member")
  val Granter = Value("Granter")
  val Manager = Value("Manager")
}

class Dimension extends IntId with Named {
  var title: String = _
  var source: String = _
  var multiple: Boolean = _
  var required: Boolean = _
  var typeName: String = _
  var keyName: String = _
  var properties: String = _
  var apps = Collections.newBuffer[App]
}

trait Profile {

  def properties: collection.mutable.Map[Dimension, String]

  def setProperty(field: Dimension, value: String): Unit = {
    val property = getProperty(field)
    if (Strings.isNotBlank(value))
      properties.put(field, value)
    else properties -= field
  }

  def getProperty(field: Dimension): Option[String] = {
    properties.get(field)
  }

  def getProperty(name: String): Option[String] = {
    properties.keys.find(k => k.name == name) match {
      case Some(p) => properties.get(p)
      case None => None
    }
  }

  def matches(other: Profile): Boolean = {
    if (other.properties.isEmpty) return true
    other.properties exists {
      case (field, target) =>
        val source = getProperty(field).getOrElse("")
        (source != Property.All) && ((target == Property.All) || (Strings.split(target, ",").toSet -- Strings.split(source, ",")).isEmpty)
    }
  }
}

/**
 * 用户在某个App上的配置
 */
class UserProfile extends LongId with Profile {
  var user: User = _
  var app: App = _
  var properties = Collections.newMap[Dimension, String]
}

class Role extends IntId with Named with Updated with Enabled with Hierarchical[Role] with Profile with Principal {
  var properties: collection.mutable.Map[Dimension, String] = new collection.mutable.HashMap[Dimension, String]
  var creator: User = _
  var members: collection.mutable.Seq[Member] = new collection.mutable.ListBuffer[Member]
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

class Root extends IntId with Updated {
  var app: App = _
  var user: User = _
}
/**
 * 人员分类
 */
class UserCategory extends IntId with Coded with TemporalOn with Named with Updated {
  var enName: String = _
  var remark: String = _
  override def toString = {
    name
  }
}

class User extends LongId with Coded with Named with Updated with TemporalOn with Enabled with Profile with Principal {
  var locked: Boolean = _
  var password: String = _
  var passwordExpiredOn: ju.Date = _
  var remark: String = _
  var members: collection.mutable.Buffer[Member] = new collection.mutable.ListBuffer[Member]
  var profiles: collection.mutable.Buffer[UserProfile] = new collection.mutable.ListBuffer[UserProfile]
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

class Member extends LongId with Updated {
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

  import MemberShip._
  def is(ship: Ship): Boolean = {
    ship match {
      case Member => member
      case Manager => manager
      case Granter => granter
      case _ => false
    }
  }
}

class SessionProfileBean extends IntId with SessionProfile {
  var app: App = _
  var category: UserCategory = _
  var capacity: Int = _
  var maxSession: Int = _
  var timeout: Short = _
}