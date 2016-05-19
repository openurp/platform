package org.openurp.platform.api.security

import java.net.URL
import org.beangle.commons.collection.Collections
import org.beangle.commons.io.IOs
import org.openurp.platform.api.Urp
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.JSON
import java.net.HttpURLConnection

/**
 * @author chaostone
 */
object RemoteService {
  def getResource(resourceName: String): Option[Resource] = {
    val url = Urp.platformBase + "/security/func/" + UrpApp.name + "/resources/info.json?name=" + resourceName
    val script = IOs.readString(new URL(url).openStream())
    val r = JSON.parse(script).asInstanceOf[Map[String, _]]
    if (r.contains("id")) {
      Some(Resource(r("id").asInstanceOf[Number].intValue, r("scope").toString, r("roles").asInstanceOf[Seq[Int]].toArray))
    } else {
      None
    }
  }

  def roots: Set[String] = {
    val url = Urp.platformBase + "/user/roots.json?app=" + UrpApp.name
    val resources = Collections.newSet[String]
    resources ++= JSON.parse(readUrl(url)).asInstanceOf[Iterable[String]]
    resources.toSet
  }

  def resources: collection.Map[String, Resource] = {
    val url = Urp.platformBase + "/security/func/" + UrpApp.name + "/resources.json"
    val resources = Collections.newMap[String, Resource]
    val resourceJsons = JSON.parse(readUrl(url)).asInstanceOf[Iterable[Map[String, _]]]
    resourceJsons.map { r =>
      resources += (r("name").toString -> Resource(r("id").asInstanceOf[Number].intValue, r("scope").toString, r("roles").asInstanceOf[Seq[Int]].toArray))
    }
    resources
  }

  def getRolePermissions(roleId: Int): Set[Int] = {
    val url = Urp.platformBase + "/security/func/" + UrpApp.name + "/permissions/role.json?id=" + roleId
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map(n => n.intValue)
    resources.toSet
  }

  def getMenusJson(): String = {
    IOs.readString(new URL(Urp.platformBase + "/security/func/" + UrpApp.name + "/menus/user/" + Securities.user + ".json").openStream())
  }

  def getAppsJson(): String = {
    IOs.readString(new URL(Urp.platformBase + "/user/apps/" + Securities.user + ".json").openStream())
  }

  private def readUrl(url: String): String = {
    val conn = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
    conn.setConnectTimeout(5 * 1000)
    conn.setReadTimeout(5 * 1000)
    conn.setRequestMethod("GET")
    try {
      IOs.readString(conn.getInputStream)
    } catch {
      case e: Exception =>
        println("Cannot connect to " + url)
        ""
    }
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
      if (authorities.contains(role)) return true
      i += 1
    }
    false
  }
}
