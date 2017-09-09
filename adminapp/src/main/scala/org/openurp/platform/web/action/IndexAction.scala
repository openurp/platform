package org.openurp.platform.web.action

import org.beangle.security.context.SecurityContext
import org.beangle.security.realm.cas.CasConfig
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.view.View
import org.openurp.app.{ Urp, UrpApp }
import org.openurp.app.security.RemoteService
import org.openurp.platform.config.model.Org

class IndexAction extends ActionSupport with ServletSupport {
  var casConfig: CasConfig = _

  def index(): View = {
    put("appsJson", RemoteService.getAppsJson())
    put("menusJson", RemoteService.getMenusJson())
    put("appName", UrpApp.name)
    put("URP", Urp)
    put("org", RemoteService.getOrg)
    put("user", SecurityContext.session.principal)
    put("casConfig", casConfig)
    forward()
  }

  def logout(): View = {
    redirect(to(casConfig.casServer + "/logout"), null)
  }
}
