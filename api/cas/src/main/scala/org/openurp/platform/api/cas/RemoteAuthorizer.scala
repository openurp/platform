package org.openurp.platform.api.cas

import org.beangle.commons.cache.CacheManager
import org.beangle.commons.security.Request
import org.beangle.security.authc.Account
import org.beangle.security.authz.Authorizer
import org.beangle.security.session.Session
import org.beangle.commons.bean.Initializing
import org.openurp.platform.api.security.RemoteService
import org.openurp.platform.api.security.Resource

/**
 * @author chaostone
 */
class RemoteAuthorizer(cacheManager: CacheManager) extends Authorizer with Initializing {
  var unknownIsProtected = true
  val resources = cacheManager.getCache("security-resources", classOf[String], classOf[Resource])
  var roots: Set[String] = _
  def init(): Unit = {
    RemoteService.resources foreach {
      case (name, resource) =>
        resources.put(name, resource)
    }
    roots = RemoteService.roots
  }

  override def isPermitted(session: Option[Session], request: Request): Boolean = {
    val resourceName = request.resource.toString
    val rscOption = resources.get(resourceName)
    rscOption match {
      case None =>
        RemoteService.getResource(resourceName) match {
          case None => if (unknownIsProtected) None != session else false
          case Some(r) =>
            resources.put(resourceName, r)
            isAuthorized(session, r)
        }
      case Some(r) => isAuthorized(session, r)
    }
  }

  private def isAuthorized(session: Option[Session], res: Resource): Boolean = {
    res.scope match {
      case 0 => true
      case 1 => None != session
      case _ =>
        if (None == session) false
        else {
          val account = session.get.principal.asInstanceOf[Account]
          res.matches(account.authorities.asInstanceOf[Set[Integer]]) || roots.contains(account.getName)
        }
    }
  }

}
