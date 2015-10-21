package org.openurp.platform.security.service.impl

import org.beangle.commons.cache.{ Cache, CacheManager }
import org.beangle.commons.security.Request
import org.beangle.security.authc.Account
import org.beangle.security.authz.{ Authorizer, Scopes }
import org.beangle.security.session.Session
import org.openurp.platform.security.service.FuncPermissionManager

class CachedDaoAuthorizer(permissionService: FuncPermissionManager, cacheManager: CacheManager) extends Authorizer {
  var unknownIsPublic = true

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
    if (account.details.contains("isRoot")) true
    else account.authorities.asInstanceOf[Set[Integer]].contains(resourceId)
  }

}