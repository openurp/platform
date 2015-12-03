package org.openurp.platform.api.cas

import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.JSON
import org.openurp.platform.api.Urp
import org.beangle.commons.io.IOs
import org.beangle.commons.collection.Properties
import java.net.URL
import org.beangle.commons.collection.Collections

/**
 * @author chaostone
 */
object RemoteService {
  def getResource(resourceName: String): Option[Resource] = {
    val url = Urp.platformBase + "/security/" + UrpApp.name + "/func-resources/info.json?name=" + resourceName
    val script = IOs.readString(new URL(url).openStream())
    val r = JSON.parse(script).asInstanceOf[Properties]
    if (r.contains("id")) {
      Some(Resource(r("id").asInstanceOf[Number].intValue, r("scope").toString, r("roles").asInstanceOf[Seq[Int]].toArray))
    } else {
      None
    }
  }

  def getFuncResources(app: String): collection.Map[String, Resource] = {
    val url = Urp.platformBase + "/security/" + UrpApp.name + "/func-resources.json?app=" + app
    val resources = Collections.newMap[String, Resource]
    //FIXME
    //    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map { n => n.intValue }
    resources
  }

  def getRolePermissions(roleId: Int): Set[Int] = {
    val url = Urp.platformBase + "/security/" + UrpApp.name + "/permission/role.json?id=" + roleId
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map(n => n.intValue)
    resources.toSet
  }
}

object Resource {
  def apply(id: Int, scope: String, roles: Array[Int]): Resource = {
    val scopes = Map("Private" -> 2, "Protected" -> 1, "Public" -> 0)
    new Resource(id, scopes(scope), roles)
  }
}
class Resource(val id: Int, val scope: Int, roles: Array[Int]) extends Serializable {

  def matches(authorities: collection.Set[Integer]): Boolean = {
    var i = 0
    while (i < roles.length) {
      val role = roles(i)
      if (authorities.contains(i)) return true
      i += 1
    }
    false
  }
}