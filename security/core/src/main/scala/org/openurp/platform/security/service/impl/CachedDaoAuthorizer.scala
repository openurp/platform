package org.openurp.platform.security.service.impl

import org.beangle.security.context.SecurityContext
import org.beangle.security.authz.Authorizer
import org.beangle.security.authz.Authority
import org.beangle.security.authc.Account
import org.beangle.commons.security.Request
import org.beangle.commons.cache.Cache
import org.beangle.commons.cache.CacheManager
import org.openurp.platform.security.service.FuncPermissionManager
import org.beangle.security.authz.Scopes

class CachedDaoAuthorizer(permissionService: FuncPermissionManager, cacheManager: CacheManager) extends Authorizer {
  var unknownIsPublic = true

  var cache: Cache[Authority, Set[String]] = cacheManager.getCache("dao-authorizer-cache")

  override def isPermitted(principal: Any, request: Request): Boolean = {
    val resourceName = request.resource.toString
    val rscOption = permissionService.getResource(resourceName)
    if (rscOption.isEmpty) return unknownIsPublic
    rscOption.get.scope match {
      case Scopes.Public => true
      case Scopes.Protected => principal != SecurityContext.Anonymous
      case _ => principal != SecurityContext.Anonymous && principal.asInstanceOf[Account].authorities.exists { role => isAuthorized(role, resourceName) }
    }
  }

  //FIXME change resource name to id
  private def isAuthorized(authority: Authority, resource: String): Boolean = {
    cache.get(authority) match {
      case Some(actions) => actions.contains(resource)
      case None =>
        val newActions = permissionService.getResourceNamesByRole(authority.authority.asInstanceOf[Integer])
        cache.put(authority, newActions)
        newActions.contains(resource)
    }
  }

}