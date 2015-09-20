package org.openurp.platform.security.service.impl

import org.beangle.commons.cache.{ Cache, CacheManager }
import org.beangle.commons.security.Request
import org.beangle.security.authc.Account
import org.beangle.security.authz.{ Authorizer, Scopes }
import org.beangle.security.session.Session
import org.openurp.platform.security.service.FuncPermissionManager

class CachedDaoAuthorizer(permissionService: FuncPermissionManager, cacheManager: CacheManager) extends Authorizer {
  var unknownIsPublic = true

  var roleAuthorities: Cache[Integer, Set[Integer]] = cacheManager.getCache("dao-authorizer-cache")

  override def isPermitted(session: Option[Session], request: Request): Boolean = {
    val resourceName = request.resource.toString
    val rscOption = permissionService.getResource(resourceName)
    rscOption match {
      case None => unknownIsPublic
      case Some(res) =>
        res.scope match {
          case Scopes.Public => true
          case Scopes.Protected => None != session
          case _ => None != session && isAuthorized(session.get.principal.asInstanceOf[Account], res.id)
        }
    }
  }

  private def isAuthorized(account: Account, resourceId: Integer): Boolean = {
    account.authorities.asInstanceOf[Iterable[Integer]].exists { roleId =>
      roleAuthorities.get(roleId) match {
        case Some(actions) => actions.contains(resourceId)
        case None =>
          val newActions = permissionService.getResourceNamesByRole(roleId)
          roleAuthorities.put(roleId, newActions)
          newActions.contains(resourceId)
      }
    }
  }

}