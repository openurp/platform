package org.openurp.platform.security.ws

import org.beangle.commons.collection.Properties
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.security.model.FuncResource
import org.beangle.security.authz.Scopes

class FuncResourceWS extends ActionSupport with EntitySupport[FuncResource] {

  var entityDao: EntityDao = _

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
  def pub(@param("app") app: String): Seq[Any] = {
    val query = OqlBuilder.from(classOf[FuncResource], "fr").where("fr.app.name=:name", app).where("fr.scope=:scope", Scopes.Public)
    entityDao.search(query)
  }

}