package org.openurp.app.action.datasource

import java.sql.DriverManager
import collection.mutable.ListBuffer
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.resource.model.DataSourceCfgBean

class ConfigAction extends RestfulAction[DataSourceCfgBean] {

  override def shortName = "dataSourceCfg"
    
  def test(): String = {
    val username = get("username", "")
    val password = get("password", "")
    val entities = getModels[DataSourceCfgBean](entityName, getIds(shortName, entityMetaData.getType(entityName).get.idType))
    val result = new ListBuffer[Tuple2[DataSourceCfgBean, Boolean]]
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