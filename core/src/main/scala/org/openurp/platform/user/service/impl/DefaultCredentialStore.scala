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
package org.openurp.platform.user.service.impl

import java.time.{Instant, LocalDate, ZoneId}

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.security.authc.{CredentialAge, DBCredentialStore, Principals}
import org.openurp.platform.user.model.{Credential, PasswordConfig, User}

class DefaultCredentialStore extends DBCredentialStore {

  var entityDao: EntityDao = _

  override def getPassword(principal: Any): Option[String] = {
    val username = Principals.getName(principal)
    val builder = OqlBuilder.from[String](classOf[Credential].getName, "c")
    builder.where("c.user.code=:code", username)
    builder.where("c.inactiveOn>=:now", LocalDate.now)
    builder.select("c.password")
    entityDao.search(builder).headOption
  }

  override def updatePassword(principal: Any, rawPassword: String): Unit = {
    val username = Principals.getName(principal)

    val configs = entityDao.getAll(classOf[PasswordConfig])
    val config =
      if (configs.nonEmpty) {
        configs.head
      } else {
        val nc = new PasswordConfig
        nc.maxdays = 180
        nc.idledays = 10
        nc
      }

    var credential: Credential = null

    val builder = OqlBuilder.from(classOf[Credential], "c")
    builder.where("c.user.code=:code", username)
    val rs = entityDao.search(builder)
    if (rs.nonEmpty) {
      credential = rs.head
    } else {
      val users = entityDao.findBy(classOf[User], "code", List(username))
      if (users.nonEmpty) {
        credential = new Credential
        credential.user = users.head
      }
    }
    if (null != credential) {
      credential.password = rawPassword
      credential.updatedAt = Instant.now
      val maxdays = if (config.mindays > 10000) 10000 else config.maxdays
      credential.expiredOn = LocalDate.ofInstant(credential.updatedAt, ZoneId.systemDefault()).plusDays(maxdays)
      credential.inactiveOn = credential.expiredOn.plusDays(config.idledays)
      entityDao.saveOrUpdate(credential)
    }
  }

  override def getAge(principal: Any): Option[CredentialAge] = {
    val username = Principals.getName(principal)
    val rs = entityDao.findBy(classOf[Credential], "user.code", List(username))
    if (rs.isEmpty) {
      None
    } else {
      val c = rs.head
      val age = CredentialAge(c.updatedAt, c.expiredOn, c.inactiveOn)
      Some(age)
    }
  }
}
