package org.openurp.platform.security.app

import org.beangle.security.authz.Authorizer
import org.beangle.commons.security.Request
import org.openurp.platform.api.app.AppConfig
import org.beangle.security.context.SecurityContext
import org.openurp.platform.api.ws.ServiceConfig
import org.beangle.security.authc.Account
import org.beangle.commons.cache.CacheManager
import org.beangle.commons.io.IOs
import org.beangle.commons.cache.Cache
import java.net.URL
import org.openurp.platform.api.util.JSON
import java.{ util => ju }
import org.beangle.commons.collection.Properties
import org.beangle.commons.lang.JDouble
import org.beangle.security.session.Session
import org.beangle.security.authz.Scopes

class RemoteAuthorizer(val cacheManager: CacheManager) extends Authorizer {
  var unknownIsPublic = true
  var resources: Cache[String, Resource] = cacheManager.getCache("remote.appauthorizer-resources")
  var roleAuthorities: Cache[Int, Set[Int]] = cacheManager.getCache("dao-authorizer-cache")

  override def isPermitted(session: Option[Session], request: Request): Boolean = {
    val resourceName = request.resource.toString
    val resource = resources.get(resourceName).orElse(RemoteAppService.getResource(resourceName))

    resource match {
      case None => unknownIsPublic
      case Some(res) =>
        res.scope match {
          case 0 => true
          case 1 => None != session
          case _ => None != session && isAuthorized(session.get.principal.asInstanceOf[Account], res.id)
        }
    }
  }

  private def isAuthorized(account: Account, resourceId: Integer): Boolean = {
    account.authorities.asInstanceOf[Iterable[Integer]].exists { roleId =>
      roleAuthorities.get(roleId) match {
        case Some(actions) => actions.contains(resourceId)
        case None =>
          val newActions = RemoteAppService.getRolePermissions(roleId)
          roleAuthorities.put(roleId, newActions)
          newActions.contains(resourceId)
      }
    }
  }

}


