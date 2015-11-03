package org.openurp.platform.security.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.security.model.Menu
import org.openurp.platform.kernel.model.App
import org.openurp.platform.api.app.UrpApp
import org.beangle.webmvc.api.context.ActionContextHolder
import org.openurp.platform.security.helper.AppHelper

class IndexAction extends ActionSupport {
  var entityDao: EntityDao = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", UrpApp.name).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))
    val apps = entityDao.search(OqlBuilder.from(classOf[App],"app").where("app.appType='web-app'"));
    put("appName",UrpApp.name)
    put("apps", apps)
    if (!apps.isEmpty) {
      AppHelper.setAppId(get("app.id", classOf[Integer]).getOrElse(apps.head.id))
      put("appId", AppHelper.getAppId())
    }
    forward()
  }

  def menus(@param("indexno") indexno: String): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", UrpApp.name)
    query.where("menu.indexno = :indexno ", indexno)
    val menus = entityDao.search(query)
    put("menus", menus)
    forward()
  }
}