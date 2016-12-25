package org.openurp.platform.web.action.security

import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.security.model.{ DataPermission, FuncResource, Menu }
import org.openurp.platform.user.model.{ Dimension, RoleMember }
import org.openurp.platform.config.model.App
import org.openurp.platform.config.service.AppService

class DashboardAction extends ActionSupport {

  var entityDao: EntityDao = _

  var appService: AppService = _

  def stat(): String = {
    populateUserStat()
    // state menus
    val apps = appService.getWebapps
    val menuStats = new collection.mutable.HashMap[Integer, Seq[_]]
    for (app <- apps) {
      val menuQuery = OqlBuilder.from(classOf[Menu], "menu")
      menuQuery.where("menu.app=:app", app).select("menu.enabled,count(*)").groupBy("enabled")
      menuStats.put(app.id, entityDao.search(menuQuery))
    }
    put("apps", apps)
    put("menuStats", menuStats)

    // stat resource
    val resourceQuery = OqlBuilder.from(classOf[FuncResource], "resource")
    resourceQuery.select("resource.enabled,count(*)").groupBy("enabled")
    put("resourceStat", entityDao.search(resourceQuery))

    // stat dataPermission and restriction
    put("dataPermissionCnt",
      entityDao.search(OqlBuilder.from(classOf[DataPermission], "p").select("count(*)")))
    put("fieldCnt", entityDao.search(OqlBuilder.from(classOf[Dimension], "param").select("count(*)")))
    forward()
  }

  private def populateUserStat(): Unit = {
    val userQuery = OqlBuilder.from(classOf[RoleMember], "gm")
    userQuery.select("gm.role.indexno,gm.role.name,gm.user.enabled,count(*)").groupBy(
      "gm.role.indexno,gm.role.name,gm.user.enabled")
    val datas = entityDao.search(userQuery)
    val rs = new collection.mutable.HashMap[String, collection.mutable.Map[Object, Object]]
    for (data <- datas) {
      val roleStat = data.asInstanceOf[Array[Object]]
      val key = roleStat(0) + " " + roleStat(1)
      val statusMap = rs.getOrElseUpdate(key, new collection.mutable.HashMap[Object, Object])
      statusMap.put(roleStat(2), roleStat(3))
    }
    put("userStat", rs)
  }

}
