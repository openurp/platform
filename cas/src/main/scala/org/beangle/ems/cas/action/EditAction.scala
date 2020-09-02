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
package org.beangle.ems.cas.action

import java.io.File

import org.beangle.commons.bean.Initializing
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.EntityDao
import org.beangle.data.jdbc.ds.DataSourceUtils
import org.beangle.data.jdbc.query.JdbcExecutor
import org.beangle.ems.app.datasource.AppDataSourceFactory
import org.beangle.ems.app.{Ems, EmsApp}
import org.beangle.ems.core.user.model.User
import org.beangle.ids.cas.ticket.TicketRegistry
import org.beangle.ids.cas.web.helper.SessionHelper
import org.beangle.security.Securities
import org.beangle.security.authc.DBCredentialStore
import org.beangle.security.codec.DefaultPasswordEncoder
import org.beangle.security.session.Session
import org.beangle.security.web.WebSecurityManager
import org.beangle.security.web.session.CookieSessionIdPolicy
import org.beangle.webmvc.api.action.{ActionSupport, ServletSupport}
import org.beangle.webmvc.api.annotation.mapping
import org.beangle.webmvc.api.view.View

class EditAction(secuirtyManager: WebSecurityManager, ticketRegistry: TicketRegistry) extends ActionSupport with ServletSupport with Initializing {

  var entityDao: EntityDao = _

  var credentialStore: DBCredentialStore = _

  var openurpJdbcExecutor: JdbcExecutor = _

  override def init(): Unit = {
    val f = new File(EmsApp.getAppFile.get.getCanonicalPath)
    if (f.exists) {
      val urlAddr = f.toURI.toURL
      var finded = false
      (scala.xml.XML.load(urlAddr.openStream()) \\ "datasource") foreach { elem =>
        val one = DataSourceUtils.parseXml(elem)
        if (one.name == "openurp") {
          finded = true
        }
      }
      if (finded) {
        val df = new AppDataSourceFactory()
        df.name = "openurp"
        df.init()
        openurpJdbcExecutor = new JdbcExecutor(df.result)
        this.logger.info("connect openurp database.")
      }
    }
  }

  @mapping(value = "")
  def index(): View = {
    put("principal", Securities.session.get.principal)
    put("emsapi", Ems.api)
    forward()
  }

  def save(): View = {
    get("password") foreach { p =>
      val users = entityDao.findBy(classOf[User], "code", List(Securities.user))
      if (users.size == 1) {
        val user = users.head
        credentialStore.updatePassword(user.code, DefaultPasswordEncoder.generate(p, null, "sha"))
        if (null != openurpJdbcExecutor) {
          val password = DefaultPasswordEncoder.generate(p, null, "md5")
          openurpJdbcExecutor.update(
            "update base.users set password=? where code=?",
            Strings.substringAfter(password, "{MD5}"), user.code)
        }
      }
    }
    get("service") match {
      case None =>
        put("portal", Ems.portal)
        forward("success")
      case Some(service) => forwardService(service, Securities.session.get)
    }
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
}
