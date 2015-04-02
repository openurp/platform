package org.openurp.platform.security.app

import org.beangle.security.authz.Authorizer
import org.beangle.commons.security.Request
import org.openurp.platform.app.App
import org.beangle.security.context.SecurityContext
import org.openurp.platform.ws.ServiceConfig
import org.beangle.security.authc.Account
import org.beangle.commons.cache.CacheManager
import org.beangle.commons.io.IOs
import org.beangle.commons.cache.Cache
import java.net.URL
import org.openurp.platform.util.JSON
import org.beangle.security.authz.Authority
import java.{ util => ju }
import org.beangle.commons.collection.Properties
import org.beangle.commons.lang.JDouble

class RemoteAppAuthorizer(val cacheManager: CacheManager) extends Authorizer {

  var resources: Cache[String, Resource] = cacheManager.getCache("remote.appauthorizer-resources")
  var permissions: Cache[String, Set[Integer]] = cacheManager.getCache("remote.appauthorizer-permissions")

  override def isPermitted(principal: Any, request: Request): Boolean = {
    val resourceName = request.resource.toString
    resources.get(resourceName) match {
      case Some(resource) =>
        if (resource.scope != "Private") true
        else isAuthorized(principal, resource.id)
      case None =>
        val url = ServiceConfig.wsBase + "/kernel/app/" + App.name + "/func/resources/info.json?name=" + resourceName
        val script = IOs.readString(new URL(url).openStream())
        val r = JSON.parse(script).asInstanceOf[Properties]
        if (!r.isEmpty) {
          val resource = new Resource(Integer.valueOf(r("id").asInstanceOf[Number].intValue), r("scope").toString())
          resources.put(resourceName, resource)
          if (resource.scope != "Private") true
          else isAuthorized(principal, resource.id)
        } else
          false
    }
  }

  private def isAuthorized(principal: Any, resourceId: Integer): Boolean = {
    //FIXME
    if (!principal.isInstanceOf[Account]) return false

    val account = principal.asInstanceOf[Account]
    val name = account.principal.toString
    permissions.get(name) match {
      case Some(actions) => actions.contains(resourceId)
      case None =>
        val url = ServiceConfig.wsBase + "/kernel/app/" + App.name + "/func/resources/permission.json?client=" + name
        val resources = new collection.mutable.HashSet[Integer]
        resources ++= JSON.parse(IOs.readString(new URL(url).openStream())).asInstanceOf[Iterable[Number]].map { n => Integer.valueOf(n.intValue) }
        permissions.put(name, resources.toSet)
        resources.contains(resourceId)
    }
  }

}

class Resource(val id: Integer, val scope: String)