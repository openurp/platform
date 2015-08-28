package org.openurp.platform.kernel.ws

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.security.blueprint.Scopes
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.kernel.service.TokenRepository
import org.openurp.platform.kernel.model.AppFuncPermission
import org.beangle.security.blueprint.FuncResource

class FuncResourceWS extends ActionSupport with EntitySupport[FuncResource] {

  var entityDao: EntityDao = _

  var tokenRespository: TokenRepository = _

  @response
  def index(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app)
    entityDao.search(query)
  }

  @response
  def info(@param("app") app: String, @param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:app", app)
    query.where("fr.name=:name", name)
    val resources = entityDao.search(query)
    if (!resources.isEmpty) new Properties(resources.head, "id", "name", "title", "scope")
    else new Properties
  }

  @response
  @mapping("public")
  def pu(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app).where("fr.scope=:scope", Scopes.Public)
    entityDao.search(query)
  }

  @response
  def permission(@param("app") app: String, @param("client") client: String): Seq[Integer] = {
    val query = OqlBuilder.from(classOf[AppFuncPermission], "afp").where("afp.app.name=:client", client)
    query.where("afp.resource.app.name=:app", app)
    query.select("afp.resource.id")
    val rs = entityDao.search(query)
    rs.asInstanceOf[Seq[Integer]]
  }
}