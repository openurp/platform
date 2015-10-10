package org.openurp.platform.security.service.impl
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.security.session.SessionBuilder
import org.beangle.data.dao.EntityDao
import org.openurp.platform.kernel.model.App
import org.openurp.platform.api.app.AppConfig
/**
 * @author chaostone
 */
class AppDBSessionRegistry(builder: SessionBuilder, executor: JdbcExecutor) extends DBSessionRegistry(builder, executor) {

  var entityDao: EntityDao = _

  override def init() {
    val app = entityDao.findBy(classOf[App], "name", List(AppConfig.name)).head
    this.sessionTable = "app_" + app.id + "_session_infoes"
    this.statTable = "app_" + app.id + "_session_stats"
    super.init()
  }

}