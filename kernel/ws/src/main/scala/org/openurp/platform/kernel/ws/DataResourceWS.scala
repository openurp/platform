package org.openurp.platform.kernel.ws

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.kernel.service.TokenRepository
import org.openurp.platform.kernel.model.AppDataPermission
import org.openurp.platform.kernel.model.DataResource
import org.beangle.security.authz.Scopes

class DataResourceWS extends ActionSupport with EntitySupport[DataResource] {

  var entityDao: EntityDao = _

  var tokenRespository: TokenRepository = _

  @response
  def info(@param("name") name: String): Properties = {
    val query = OqlBuilder.from(classOf[DataResource], "fr").where("fr.name=:name", name)
    val resources = entityDao.search(query)
    if (!resources.isEmpty) new Properties(resources.head, "id", "name", "title", "scope")
    else new Properties
  }

  @response
  def permission(@param("app") app: String, @param("client") client: String): Seq[Integer] = {
    val query = OqlBuilder.from(classOf[AppDataPermission], "adp").where("adp.app.name=:client", client)
    query.where("adp.resource.app.name=:app", app)
    query.select("adp.resource.id")
    val rs = entityDao.search(query)
    rs.asInstanceOf[Seq[Integer]]
  }
}