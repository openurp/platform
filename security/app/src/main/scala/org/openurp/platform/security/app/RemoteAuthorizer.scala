package org.openurp.platform.security.app

import org.beangle.commons.cache.{ Cache, CacheManager }
import org.beangle.commons.security.Request
import org.beangle.security.authc.Account
import org.beangle.security.authz.Authorizer
import org.beangle.security.session.Session

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


