package org.openurp.platform.web.action.config

import java.sql.DriverManager

import collection.mutable.ListBuffer

import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.Db
import org.beangle.webmvc.api.view.View

class DbAction extends RestfulAction[Db] {

  override def simpleEntityName = "db"

  def test(): View = {
    val username = get("user", "")
    val password = get("password", "")
    val entities = getModels[Db](entityName, ids(simpleEntityName, entityDao.domain.getEntity(entityName).get.id.clazz))
    val result = new ListBuffer[Tuple2[Db, Boolean]]
    for (cfg <- entities) {
      try {
        val conn = DriverManager.getConnection(cfg.url.orNull, username, password)
        conn.close()
        result += cfg -> true
      } catch {
        case t: Throwable =>
          t.printStackTrace()
          result += cfg -> false
      }
    }
    put("result", result)
    forward()
  }

  protected override def editSetting(entity: Db): Unit = {
    val drivers = Map("postgresql" -> "PostgreSQL", "oracle" -> "Oracle", "mysql" -> "MySQL", "db2" -> "DB2", "sqlserver" -> "Microsoft SQL Server")
    put("drivers", drivers)
  }

}