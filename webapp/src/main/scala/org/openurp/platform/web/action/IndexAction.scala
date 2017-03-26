package org.openurp.platform.web.action

import org.beangle.commons.dao.{ EntityDao, OqlBuilder }
import org.beangle.security.context.SecurityContext
import org.beangle.security.realm.cas.CasConfig
import org.beangle.security.web.WebSecurityManager
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.annotation.param
import org.beangle.webmvc.api.view.View
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.security.Securities
import org.openurp.platform.config.service.AppService
import org.openurp.platform.security.model.Menu

class IndexAction extends ActionSupport with ServletSupport {
  var entityDao: EntityDao = _
  var casConfig: CasConfig = _
  var securityManager : WebSecurityManager = _
  var appService: AppService = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.app.name=:app", UrpApp.name).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))
    val apps = appService.getWebapps()
    put("appName", UrpApp.name)
    put("apps", apps)
    put("username", Securities.user)
    put("casConfig", casConfig)
    forward()
  }

  def logout(): View = {
    securityManager.logout(request, response, SecurityContext.session)
    redirect(to(casConfig.casServer + "/logout"), null)
  }

  def menus(@param("indexno") indexno: String): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.app.name=:app", UrpApp.name)
    query.where("menu.indexno = :indexno ", indexno)
    val menus = entityDao.search(query)
    put("menus", menus)
    forward()
  }
}
