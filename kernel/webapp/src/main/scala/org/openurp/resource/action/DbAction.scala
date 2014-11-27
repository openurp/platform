package org.openurp.resource.action

import java.sql.DriverManager
import collection.mutable.ListBuffer
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.resource.model.DbBean

class DbAction extends RestfulAction[DbBean] {

  override def shortName = "db"
    
  def test(): String = {
    val username = get("username", "")
    val password = get("password", "")
    val entities = getModels[DbBean](entityName, getIds(shortName, entityMetaData.getType(entityName).get.idType))
    val result = new ListBuffer[Tuple2[DbBean, Boolean]]
    for (cfg <- entities) {
      try {
        Class.forName(cfg.driverClassName)
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