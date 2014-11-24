package org.openurp.platform.ws.security

import org.beangle.commons.web.intercept.Interceptor
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.beangle.webmvc.api.context.ContextHolder
import org.beangle.webmvc.context.ActionContextHelper
import org.openurp.platform.app.service.AppService

class AppSecurityInterceptor(val appService: AppService) extends Interceptor {

  def preInvoke(request: HttpServletRequest, response: HttpServletResponse): Boolean = {
    val mapping = ActionContextHelper.getMapping(ContextHolder.context)
    //    val resource = appService.getResource(mapping.action.config.name)
    //    if (resource.isPublic) true
    //    else {
    val appId = request.getParameter("app_id")
    if (null == appId) {
      appService.isPublic(mapping.action.config.name)
    } else {
      appService.isPermitted(appId, mapping.action.config.name, mapping.action.method.getName)
    }
    //    }
  }

  def postInvoke(request: HttpServletRequest, response: HttpServletResponse): Unit = {

  }
}