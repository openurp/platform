package org.openurp.platform.api.cas

import org.beangle.security.authz.Authorizer
import org.beangle.commons.security.Request
import org.beangle.security.session.Session
import org.beangle.security.authc.Account
import org.beangle.security.authz.Scopes
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.JSON
import org.openurp.platform.api.Urp
import org.beangle.commons.io.IOs
import org.beangle.commons.collection.Properties
import java.net.URL

/**
 * @author chaostone
 */
class RemoteAuthorizer extends Authorizer {
  var unknownIsProtected = true
  var resources: Map[String, Resource] = Map.empty

  override def isPermitted(session: Option[Session], request: Request): Boolean = {
    val resourceName = request.resource.toString
    val rscOption = resources.get(resourceName)
    rscOption match {
      case None =>
        getResource(resourceName) match {
          case None => if (unknownIsProtected) None != session else false
          case Some(r) =>
            resources += (resourceName -> r)
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
          if (account.details("isRoot").asInstanceOf[Boolean]) true
          else account.authorities.asInstanceOf[Set[Integer]].contains(res.id)
        }
    }
  }

  private def getResource(resourceName: String): Option[Resource] = {
    val url = Urp.platformBase + "/security/" + UrpApp.name + "/func-resources/info.json?name=" + resourceName
    val script = IOs.readString(new URL(url).openStream())
    val r = JSON.parse(script).asInstanceOf[Properties]
    if (r.contains("id")) {
      Some(Resource(Integer.parseInt(r("id").toString), r("scope").toString))
    } else {
      None
    }
  }

}
object Resource {
  def apply(id: Int, scope: String): Resource = {
    val scopes = Map("Private" -> 2, "Protected" -> 1, "Public" -> 0)
    new Resource(id, scopes(scope))
  }
}
class Resource(val id: Int, val scope: Int) {
}
