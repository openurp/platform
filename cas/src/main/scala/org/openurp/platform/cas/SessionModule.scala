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
package org.openurp.platform.cas

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cdi.bind.BindModule
import org.beangle.security.authc.DefaultAccount
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.session.protobuf.{AccountSerializer, AgentSerializer, SessionSerializer}
import org.beangle.security.session.{DefaultSession, Session}
import org.beangle.serializer.protobuf.ProtobufSerializer
import org.openurp.platform.cas.service.DefaultUrpSessionIdPolicy

class SessionModule extends BindModule {
  override def binding(): Unit = {
    bind("cache.Caffeine", classOf[CaffeineCacheManager]).constructor(true)
    val protobuf = new ProtobufSerializer
    protobuf.register(classOf[DefaultSession], SessionSerializer)
    protobuf.register(classOf[DefaultAccount], AccountSerializer)
    protobuf.register(classOf[Session.Agent], AgentSerializer)

    bind("Serializer.protobuf", protobuf)
    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(?, ref("cache.Caffeine"), protobuf)
      .property("sessionTable", "session.session_infoes")
      .wiredEagerly(false)

    bind("security.SessionIdPolicy.urp", classOf[DefaultUrpSessionIdPolicy])
      .property("base", $("login.origin"))
  }
}
