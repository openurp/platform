package org.openurp.platform.web.action

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.security.model.Menu
import org.openurp.platform.config.model.App
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.web.helper.AppHelper
import org.beangle.security.context.SecurityContext
import org.beangle.security.mgt.SecurityManager
import org.beangle.webmvc.api.view.View
import org.beangle.security.realm.cas.CasConfig
import org.openurp.platform.api.security.Securities

class IndexAction extends ActionSupport {
  var entityDao: EntityDao = _
  var casConfig: CasConfig = _
  var securityManager: SecurityManager = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", UrpApp.name).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))
    val apps = entityDao.search(OqlBuilder.from(classOf[App], "app").where("app.appType='web-app'"));
    put("appName", UrpApp.name)
    put("apps", apps)
    if (!apps.isEmpty) {
      AppHelper.setAppId(get("app.id", classOf[Integer]).getOrElse(apps.head.id))
      put("appId", AppHelper.getAppId())
    }
    put("username", Securities.user)
    put("casConfig", casConfig)
    forward()
  }

  def logout(): View = {
    securityManager.logout(SecurityContext.session)
    redirect(to(casConfig.casServer + "/logout"), null)
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
