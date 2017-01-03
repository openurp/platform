package org.openurp.platform.ws.security.func

import org.beangle.commons.collection.Properties
import org.beangle.commons.dao.OqlBuilder
import org.beangle.commons.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.FuncResource
import org.beangle.security.authz.Scopes
import org.openurp.platform.security.model.FuncPermission
import org.beangle.commons.collection.Collections
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.security.model.FuncPermission
import org.openurp.platform.security.model.FuncPermission

/**
 * 系统功能资源web服务
 */
class ResourceWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[FuncResource] {

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app)
    get("scope") match {
      case Some("Private") => query.where("fr.scope = :scope", Scopes.Private)
      case Some("Protected") => query.where("fr.scope = :scope", Scopes.Protected)
      case Some("Public") => query.where("fr.scope = :scope", Scopes.Public)
      case _ =>
    }
    val resources = entityDao.search(query)
    val permissionQuery = OqlBuilder.from[Array[Object]](classOf[FuncPermission].getName, "fp")
      .where("fp.resource.app.name = :appName", app).
      where("fp.endAt is null  or fp.endAt < :now)", new java.util.Date).select("fp.resource.id,fp.role.id")

    val permissions = Collections.newMap[Number, collection.mutable.Set[Number]]
    entityDao.search(permissionQuery) foreach { p =>
      permissions.getOrElseUpdate(p(0).asInstanceOf[Number], new collection.mutable.HashSet[Number]) += p(1).asInstanceOf[Number]
    }
    resources map { r =>
      val p = new Properties(r, "id", "name", "title", "scope")
      p.put("roles", permissions.getOrElse(r.id, Set.empty).toArray)
      p
    }
  }

  @response
  def info(@param("app") app: String, @param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:app", app)
    query.where("fr.name=:name", name)
    val resources = entityDao.search(query)
    if (!resources.isEmpty) {
      val roleQuery = OqlBuilder.from[Integer](classOf[FuncPermission].getName, "fp")
        .where("fp.resource.app.name = :appName", app).where("fp.resource.name =:resourceName", name)
        .where("fp.endAt is null  or fp.endAt < :now)", new java.util.Date).select("fp.role.id")
      val p = new Properties(resources.head, "id", "name", "title", "scope")
      p.put("roles", entityDao.search(roleQuery).toArray)
      p
    } else {
      new Properties
    }
  }

  @response
  @mapping("public")
  def pub(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app).where("fr.scope=:scope", Scopes.Public)
    entityDao.search(query)
  }

}
