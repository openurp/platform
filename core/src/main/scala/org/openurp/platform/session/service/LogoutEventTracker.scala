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
package org.openurp.platform.session.service

import java.time.Instant

import org.beangle.commons.event.{Event, EventListener}
import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.Account
import org.beangle.security.session.{EventTypes, LogoutEvent}
import org.openurp.platform.session.model.SessionEvent

class LogoutEventTracker extends EventListener[LogoutEvent] {
  var entityDao: EntityDao = _

  override def onEvent(event: LogoutEvent): Unit = {
    val logout = new SessionEvent
    logout.eventType = EventTypes.Logout
    logout.updatedAt = Instant.now
    val session = event.session
    logout.principal = session.principal.getName
    logout.username = session.principal.asInstanceOf[Account].description

    logout.name = logout.principal + " " + logout.username + " 退出" + (if (null != event.reason) event.reason else "")

    logout.ip = session.agent.ip
    logout.detail = session.agent.name + " " + session.agent.os
    entityDao.saveOrUpdate(logout)
  }

  override def supportsEventType(eventType: Class[_ <: Event]): Boolean = {
    classOf[LogoutEvent].isAssignableFrom(eventType)
  }

  override def supportsSourceType(sourceType: Class[_]): Boolean = {
    true
  }
}
