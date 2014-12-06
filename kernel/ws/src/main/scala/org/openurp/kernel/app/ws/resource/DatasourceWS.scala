package org.openurp.kernel.app.ws.resource

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ action, mapping, param, response }
import org.openurp.kernel.app.model.{ AppBean, DataSourceBean }
import org.beangle.webmvc.api.action.EntityActionSupport

class DatasourceWS(entityDao: EntityDao) extends EntityActionSupport[DataSourceBean] {

  @mapping(value = "{name}")
  @response
  def index(@param("app") app: String, @param("name") name: String): AnyRef = {
    val secret = get("secret", "")
    val apps = entityDao.findBy(classOf[AppBean], "name", List(app))
    if (apps.isEmpty) return "error:error_app_name"
    val exist = apps.head
    if (exist.secret != secret) return "error:error_secret"

    val query = OqlBuilder.from(classOf[DataSourceBean], "ds")
    query.where("ds.app=:app and ds.name=:key", exist, name)
    val set = entityDao.search(query)
    if (set != null && set.size > 0) {
      val rs = set.head
      val ds = new Properties
      ds.put("username", rs.username)
      ds.put("password", rs.password)
      ds.put("url", rs.db.url)
      ds.put("maxActive", rs.maxActive)
      ds.put("driverClassName", rs.db.driverClassName)
      ds
    } else "error:error_resource_key"
  }

}