package org.openurp.app.ws.func

import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.beangle.data.model.dao.EntityDao
import org.beangle.data.jpa.dao.OqlBuilder
import org.openurp.app.func.model.FuncResourceBean
import org.beangle.security.blueprint.Scopes
import org.openurp.app.func.model.AppFuncPermissionBean
import org.openurp.app.auth.service.TokenRepository
import org.beangle.webmvc.api.action.EntityActionSupport
import org.beangle.commons.collection.Properties
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.annotation.response

class ResourceWS extends EntityActionSupport[FuncResourceBean] {

  var entityDao: EntityDao = _

  var tokenRespository: TokenRepository = _

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResourceBean], "fr").where("fr.app.name=:name", app)
    entityDao.search(query)
  }

  @response
  def info(@param("app") app: String, @param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[FuncResourceBean], "fr").where("fr.app.name=:app", app)
    query.where("fr.name=:name", name)
    val resources = entityDao.search(query)
    if (!resources.isEmpty) new Properties(resources.head, "id", "name", "title", "scope")
    else new Properties
  }

  @response
  def public(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResourceBean], "fr").where("fr.app.name=:name", app).where("fr.scope=:scope", Scopes.Public)
    entityDao.search(query)
  }

  @response
  def permission(@param("app") app: String, @param("client") client: String): Seq[Integer] = {
    val query = OqlBuilder.from(classOf[AppFuncPermissionBean], "afp").where("afp.app.name=:client", client)
    query.where("afp.resource.app.name=:app", app)
    query.select("afp.resource.id")
    val rs = entityDao.search(query)
    rs.asInstanceOf[Seq[Integer]]
  }
}