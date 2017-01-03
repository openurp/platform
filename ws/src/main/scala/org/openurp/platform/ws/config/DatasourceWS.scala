package org.openurp.platform.ws.config

import org.beangle.commons.collection.Properties
import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.openurp.platform.config.model.{ App, DataSource }

class DatasourceWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[DataSource] {

  @mapping(value = "{app}/{name}")
  @response
  def index(@param("app") app: String, @param("name") name: String): AnyRef = {
    val secret = get("secret", "")
    val apps = entityDao.findBy(classOf[App], "name", List(app))
    if (apps.isEmpty) return "error:error_app_name"
    val exist = apps.head
    if (exist.secret != secret) return "error:error_secret"

    val query = OqlBuilder.from(classOf[DataSource], "ds")
    query.where("ds.app=:app and ds.name=:key", exist, name)
    val set = entityDao.search(query)
    if (set != null && set.size > 0) {
      val rs = set.head
      val ds = new Properties
      ds.put("user", rs.username)
      ds.put("password", rs.password)
      ds.put("driver", rs.db.driver)
      if (rs.db.url != null) {
        ds.put("url", rs.db.url)
      } else {
        ds.put("serverName", rs.db.serverName)
        ds.put("databaseName", rs.db.databaseName)
        ds.put("portNumber", rs.db.portNumber)
      }
      ds.put("maximumPoolSize", rs.maxActive)
      ds
    } else "error:error_resource_key"
  }

}