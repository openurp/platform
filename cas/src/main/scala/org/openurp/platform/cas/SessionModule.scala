/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.cas

import org.beangle.cache.caffeine.CaffeineCacheManager
import org.beangle.cdi.bind.BindModule
import org.beangle.commons.lang.Strings
import org.beangle.data.jdbc.ds.DataSourceFactory
import org.beangle.security.authc.DefaultAccount
import org.beangle.security.session.Session
import org.beangle.security.session.DefaultSession
import org.beangle.security.session.jdbc.DBSessionRegistry
import org.beangle.security.session.protobuf.{ AccountSerializer, AgentSerializer, SessionSerializer }
import org.beangle.serializer.protobuf.ProtobufSerializer
import org.openurp.app.{ Urp, UrpApp }
import org.openurp.platform.cas.service.DefaultUrpSessionIdPolicy

class SessionModule extends BindModule {
  override def binding() {
    bind("cache.Caffeine", classOf[CaffeineCacheManager]).constructor(true)
    //inner usage
    bind("DataSource.session#", classOf[DataSourceFactory])
      .property("name", "session")
      .property("url", UrpApp.getUrpAppFile.get.getAbsolutePath)

    val protobuf = new ProtobufSerializer
    protobuf.register(classOf[DefaultSession], SessionSerializer)
    protobuf.register(classOf[DefaultAccount], AccountSerializer)
    protobuf.register(classOf[Session.Agent], AgentSerializer)

    bind("Serializer.protobuf", protobuf)
    bind("security.SessionRegistry.db", classOf[DBSessionRegistry])
      .constructor(ref("DataSource.session#"), ref("cache.Caffeine"), protobuf)
      .property("sessionTable", "session.session_infoes")

    bind("security.SessionIdPolicy.urp", classOf[DefaultUrpSessionIdPolicy])
      .property("path", "/").property("domain", Strings.substringAfter(Urp.base, "//"))
  }
}
