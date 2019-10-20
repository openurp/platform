package org.openurp.platform.session.service

import java.time.Instant

import org.beangle.commons.event.{Event, EventListener}
import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.Account
import org.beangle.security.session.{EventTypes, LoginEvent}
import org.openurp.platform.session.model.SessionEvent

class LoginEventTracker extends EventListener[LoginEvent] {
  var entityDao: EntityDao = _

  override def onEvent(event: LoginEvent): Unit = {
    val login = new SessionEvent
    login.eventType = EventTypes.Login
    login.updatedAt = Instant.now
    val session = event.session

    login.principal = session.principal.getName
    login.username = session.principal.asInstanceOf[Account].description

    login.name = login.principal + " " + login.username + " 登录"

    login.ip = session.agent.ip
    login.detail = session.agent.name + " " + session.agent.os
    entityDao.saveOrUpdate(login)
  }

  override def supportsEventType(eventType: Class[_ <: Event]): Boolean = {
    classOf[LoginEvent].isAssignableFrom(eventType)
  }

  override def supportsSourceType(sourceType: Class[_]): Boolean = {
    true
  }
}
