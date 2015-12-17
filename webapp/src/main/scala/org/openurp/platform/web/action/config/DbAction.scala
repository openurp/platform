package org.openurp.platform.web.action.config

import java.sql.DriverManager

import collection.mutable.ListBuffer

import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.Db

class DbAction extends RestfulAction[Db] {

  override def simpleEntityName = "db"

  def test(): String = {
    val username = get("user", "")
    val password = get("password", "")
    val entities = getModels[Db](entityName, ids(simpleEntityName, entityMetaData.getType(entityName).get.idType))
    val result = new ListBuffer[Tuple2[Db, Boolean]]
    for (cfg <- entities) {
      try {
        val conn = DriverManager.getConnection(cfg.url, username, password)
        conn.close()
        result += cfg -> true
      } catch {
        case t: Throwable =>
          t.printStackTrace();
          result += cfg -> false
      }
    }
    put("result", result)
    forward()
  }

}