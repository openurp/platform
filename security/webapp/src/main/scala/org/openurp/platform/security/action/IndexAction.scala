package org.openurp.platform.security.action

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.security.model.Menu
import org.openurp.platform.kernel.model.App
import org.openurp.platform.api.app.AppConfig
import org.beangle.webmvc.api.context.ActionContextHolder
import org.openurp.platform.security.helper.AppHelper

class IndexAction extends ActionSupport {
  var entityDao: EntityDao = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", AppConfig.appName).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))
    val apps = entityDao.getAll(classOf[App])
    put("appName",AppConfig.appName)
    put("apps", apps)
    if (!apps.isEmpty) {
      AppHelper.setAppId(get("app.id", classOf[Integer]).getOrElse(apps.head.id))
      put("appId", AppHelper.getAppId())
    }
    forward()
  }

  def menus(@param("indexno") indexno: String): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", AppConfig.appName)
    query.where("menu.indexno = :indexno ", indexno)
    val menus = entityDao.search(query)
    put("menus", menus)
    forward()
  }
}