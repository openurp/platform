package org.openurp.platform.security.app

import org.openurp.platform.api.ws.ServiceConfig
import org.beangle.commons.io.IOs
import org.openurp.platform.api.app.AppConfig
import java.net.URL
import org.openurp.platform.api.util.JSON
import org.beangle.commons.collection.Properties
import org.beangle.security.authc.BadCredentialsException
import org.beangle.security.authc.AuthenticationToken

object RemoteAppService {

  def validate(ticket: String, token: AuthenticationToken): SimpleUser = {
    val url = ServiceConfig.wsBase + "/kernel/" + AppConfig.name + "/validate.json?token=" + ticket
    val result = IOs.readString(new URL(url).openStream())
    val prefix = "principal\":\""
    val descPrefix = "description\":\""
    val start = result.indexOf(prefix)
    val descStart = result.indexOf(descPrefix)

    if (-1 == start || -1 == descStart) {
      throw new BadCredentialsException("Bad credentials :" + ticket, token, null)
    } else {
      new SimpleUser(
        result.substring(start + prefix.length, result.indexOf("\"", start + prefix.length)),
        result.substring(descStart + descPrefix.length, result.indexOf("\"", descStart + descPrefix.length)))
    }

  }

  def getAppPermissions(app: String): Set[Int] = {
    val url = ServiceConfig.wsBase + "/security/" + AppConfig.name + "/func-resources/permission.json?client=" + app
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map { n => n.intValue }
    resources.toSet
  }

  def getRoles(principal: String): Set[Int] = {
    val url = ServiceConfig.wsBase + "/security/" + AppConfig.name + "/func-resources/permission.json?user=" + principal
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map(n => n.intValue)
    resources.toSet
  }

  def getResource(resourceName: String): Option[Resource] = {
    val url = ServiceConfig.wsBase + "/security/" + AppConfig.name + "/func-resources/info.json?name=" + resourceName
    val script = IOs.readString(new URL(url).openStream())
    val r = JSON.parse(script).asInstanceOf[Properties]
    if (!r.isEmpty) {
      Some(new Resource(r("id").asInstanceOf[Number].intValue, r("scope").asInstanceOf[Number].intValue))
    } else {
      None
    }
  }

  def getRolePermissions(roleId: Int): Set[Int] = {
    val url = ServiceConfig.wsBase + "/security/" + AppConfig.name + "/func-resources/permission.json?role.id=" + roleId
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map(n => n.intValue)
    resources.toSet
  }

  def getUser(roleId: Int): Set[Int] = {
    val url = ServiceConfig.wsBase + "/security/" + AppConfig.name + "/func-resources/permission.json?role.id=" + roleId
    val resources = new collection.mutable.HashSet[Int]
    resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map(n => n.intValue)
    resources.toSet
  }
}

class Resource(val id: Int, val scope: Int)

class SimpleUser(val name: String, val description: String)
