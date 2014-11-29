package org.openurp.platform.security.app

import javax.servlet.http.HttpServletResponse
import org.openurp.platform.app.service.AppService
import javax.servlet.http.HttpServletRequest
import org.beangle.security.context.ContextHolder
import org.beangle.webmvc.context.ActionContextHelper
import org.beangle.webmvc.api.context.ContextHolder
import org.beangle.commons.web.intercept.Interceptor

class SecurityInterceptor(val appService: AppService) extends Interceptor {

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