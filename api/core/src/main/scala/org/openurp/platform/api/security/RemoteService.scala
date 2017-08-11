package org.openurp.platform.api.security

import java.net.{ HttpURLConnection, URL, URLEncoder }

import org.beangle.commons.collection.Collections
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.Strings
import org.openurp.platform.api.Urp
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.JSON
import org.beangle.commons.net.http.HttpUtils

/**
 * @author chaostone
 */
object RemoteService {
  def getResource(resourceName: String): Option[Resource] = {
    val url = Urp.platformBase + "/security/func/" + UrpApp.name +
      "/resources/info.json?name=" +
      URLEncoder.encode(resourceName, "utf-8")
    val script = IOs.readString(new URL(url).openStream())
    val r = JSON.parse(script).asInstanceOf[Map[String, _]]
    if (r.contains("id")) {
      Some(Resource(r("id").asInstanceOf[Number].intValue, r("scope").toString, r("roles").asInstanceOf[Seq[Number]].map(_.intValue).toSet))
    } else {
      None
    }
  }

  def roots: Option[Set[String]] = {
    val url = Urp.platformBase + "/user/roots.json?app=" + UrpApp.name
    HttpUtils.getText(url) map { s =>
      val resources = Collections.newSet[String]
      resources ++= JSON.parse(s).asInstanceOf[Iterable[String]]
      resources.toSet
    }
  }

  def resources: collection.Map[String, Resource] = {
    val url = Urp.platformBase + "/security/func/" + UrpApp.name + "/resources.json"
    val resources = Collections.newMap[String, Resource]
    val resourceJsons = JSON.parse(HttpUtils.getText(url).orNull).asInstanceOf[Iterable[Map[String, _]]]
    resourceJsons.map { r =>
      resources += (r("name").toString ->
        Resource(r("id").asInstanceOf[Number].intValue, r("scope").toString, r("roles").asInstanceOf[Iterable[Number]].map(_.intValue).toSet))
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
    val domain = Strings.substringBefore(UrpApp.name, "-")
    IOs.readString(new URL(Urp.platformBase + "/user/apps/" + Securities.user + ".json?domain=" + domain).openStream())
  }
}

object Resource {
  def apply(id: Int, scope: String, roles: Set[Int]): Resource = {
    val scopes = Map("Private" -> 2, "Protected" -> 1, "Public" -> 0)
    new Resource(id, scopes(scope), roles)
  }
}

class Resource(val id: Int, val scope: Int, roles: Set[Int]) extends Serializable {

  def matches(authorities: collection.Set[Int]): Boolean = {
    roles.exists(authorities.contains(_))
  }
}
