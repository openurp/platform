package org.openurp.platform.kernel.action

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.model.Menu

class IndexAction extends ActionSupport {
  var entityDao: EntityDao = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", AppConfig.name).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))

    val apps = entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.appType='web-app'"));
    put("appName", AppConfig.name)
    put("apps", apps)
    forward()
  }

  def menus(@param("indexno") indexno: String): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", AppConfig.name)
    query.where("menu.indexno = :indexno ", indexno)
    val menus = entityDao.search(query)
    put("menus", menus)
    forward()
  }
}