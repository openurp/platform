/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.beangle.ids.cas.web.action

import javax.servlet.http.HttpServletRequest

import org.beangle.commons.codec.binary.Aes
import org.beangle.commons.lang.Strings
import org.beangle.commons.web.url.UrlBuilder
import org.beangle.commons.web.util.RequestUtils

import org.beangle.ids.cas.ticket.TicketRegistry
import org.beangle.ids.cas.web.helper.SessionHelper
import org.beangle.security.Securities
import org.beangle.security.authc.{ AccountExpiredException, AuthenticationException, BadCredentialsException, CredentialsExpiredException, DisabledException, LockedException, UsernameNotFoundException, UsernamePasswordToken }
import org.beangle.security.context.SecurityContext
import org.beangle.security.session.Session
import org.beangle.security.web.WebSecurityManager
import org.beangle.security.web.access.SecurityContextBuilder
import org.beangle.security.web.session.CookieSessionIdPolicy
import org.beangle.webmvc.api.action.{ ActionSupport, ServletSupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, ignore }
import org.beangle.webmvc.api.view.View
import org.beangle.ids.cas.web.helper.CsrfDefender
import org.beangle.ids.cas.LoginConfig
import org.beangle.commons.bean.Initializing
import java.io.ByteArrayInputStream
import org.beangle.webmvc.api.view.{ Stream, Status }
import org.beangle.ids.cas.web.helper.CaptchaHelper

/**
 * @author chaostone
 */
class LoginAction(secuirtyManager: WebSecurityManager, ticketRegistry: TicketRegistry)
  extends ActionSupport with ServletSupport with Initializing {

  private var csrfDefender: CsrfDefender = _

  var config: LoginConfig = _

  var captchaHelper: CaptchaHelper = _

  var securityContextBuilder: SecurityContextBuilder = _

  val messages: Map[Class[_], String] = Map(
    (classOf[AccountExpiredException] -> "账户过期"),
    (classOf[UsernameNotFoundException] -> "找不到该用户"),
    (classOf[BadCredentialsException] -> "密码错误"),
    (classOf[LockedException] -> "账户被锁定"),
    (classOf[DisabledException] -> "账户被禁用"),
    (classOf[CredentialsExpiredException] -> "密码过期"))

  override def init(): Unit = {
    csrfDefender = new CsrfDefender(config.key, config.origin)
  }

  @mapping(value = "")
  def index(@param(value = "service", required = false) service:String): View = {
    Securities.session match {
      case Some(session) =>
        forwardService(service, session)
      case None =>
        val u = get("username")
        val p = get("password")
        if (u.isEmpty || p.isEmpty) {
          toLoginForm()
        } else {
          val isService = getBoolean("isService", false)
          val validCsrf = isService || csrfDefender.valid(request, response)
          if (validCsrf) {
            if (!isService && config.enableCaptcha && !captchaHelper.verify(request, response)) {
              put("error", "错误的验证码")
              toLoginForm()
            } else {
              var password = p.get
              if (password.startsWith("?")) {
                password = Aes.ECB.decodeHex(loginKey, password.substring(1))
              }
              val token = new UsernamePasswordToken(u.get, password)
              try {
                val req = request
                val session = secuirtyManager.login(req, response, token)
                SecurityContext.set(securityContextBuilder.build(req, Some(session)))
                forwardService(service, session)
              } catch {
                case e: AuthenticationException =>
                  val msg = messages.get(e.getClass).getOrElse(e.getMessage())
                  put("error", msg)
                  toLoginForm()
              }
            }
          } else {
            null
          }
        }
    }
  }

  @ignore
  def toLoginForm(): View = {
    if (config.forceHttps && !RequestUtils.isHttps(request)) {
      val req = request
      val builder = new UrlBuilder(req.getContextPath())
      builder.setScheme("https").setServerName(req.getServerName).setPort(443)
        .setContextPath(req.getContextPath).setServletPath("/login")
        .setQueryString(req.getQueryString)
      redirect(to(builder.buildUrl()), "force https")
    } else {
      csrfDefender.addToken(request, response)
      put("config",config)
      put("current_timestamp",System.currentTimeMillis)
      forward("index")
    }
  }

  def success: View = {
    put("logined", Securities.session.isDefined)
    forward()
  }

  private def forwardService(service: String, session: Session): View = {
    if (null == service) {
      redirect("success", null)
    } else {
      val idPolicy = secuirtyManager.sessionIdPolicy.asInstanceOf[CookieSessionIdPolicy]
      val isMember = SessionHelper.isMember(request, service, idPolicy)
      if (isMember) {
        if (SessionHelper.isSameDomain(request, service, idPolicy)) {
          redirect(to(service), null)
        } else {
          val serviceWithSid =
            service + (if (service.contains("?")) "&" else "?") + idPolicy.name + "=" + session.id
          redirect(to(serviceWithSid), null)
        }
      } else {
        val ticket = ticketRegistry.generate(session, service)
        redirect(to(service + (if (service.contains("?")) "&" else "?") + "ticket=" + ticket), null)
      }
    }
  }
  /**
   * 用于加密用户密码的公开key，注意不要更改这里16。
   */
  private def loginKey: String = {
    "202.121.129.1730"
  }

  def captcha: View = {
    if (config.enableCaptcha) {
      Stream(new ByteArrayInputStream(captchaHelper.generate(request, response)), "image/jpeg", "captcha")
    } else {
      Status(404)
    }
  }

}
