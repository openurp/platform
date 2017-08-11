package org.openurp.platform.api.security

import org.beangle.cache.CacheManager
import org.beangle.commons.bean.Initializing
import org.beangle.commons.security.Request
import org.beangle.security.authc.DefaultAccount
import org.beangle.security.authz.Authorizer
import org.beangle.security.session.Session
import org.beangle.commons.lang.Strings
import org.beangle.commons.collection.Collections

/**
 * @author chaostone
 */
class RemoteAuthorizer(cacheManager: CacheManager) extends Authorizer with Initializing {
  var unknownIsProtected = true
  val resources = cacheManager.getCache("security-resources", classOf[String], classOf[Resource])
  val authorities = cacheManager.getCache("security-authorities", classOf[String], classOf[collection.mutable.Set[Int]])
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

  private def isAuthorized(s: Option[Session], res: Resource): Boolean = {
    res.scope match {
      case 0 => true
      case 1 => None != s
      case _ =>
        s match {
          case Some(session) =>
            val auths = authorities.get(session.id) match {
              case Some(aus) => aus
              case None =>
                val newAuths = Collections.newSet[Int]
                authorities.put(session.id, newAuths)
                newAuths
            }
            if (auths.contains(res.id)) {
              true
            } else {
              val account = session.principal.asInstanceOf[DefaultAccount]
              val rs = res.matches(Strings.splitToInt(account.authorities).toSet) || roots.contains(account.getName)
              if (rs) auths.add(res.id)
              rs
            }
          case None => false
        }
    }
  }

}
