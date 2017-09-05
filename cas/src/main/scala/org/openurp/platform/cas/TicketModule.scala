package org.openurp.platform.cas

import org.beangle.cache.redis.JedisPoolFactory
import org.beangle.cdi.bind.BindModule
import org.beangle.ids.cas.id.impl.DefaultServiceTicketIdGenerator
import org.beangle.ids.cas.ticket.{ DefaultTicketCacheService, DefaultTicketRegistry }

class TicketModule extends BindModule {
  override def binding() {
    bind("jedis.Factory", classOf[JedisPoolFactory]).constructor(Map("host" -> $("redis.host"), "port" -> $("redis.port")))
    bind(classOf[DefaultTicketCacheService]).constructor(ref("jedis.Factory"))
    bind(classOf[DefaultTicketRegistry])
    bind(classOf[DefaultServiceTicketIdGenerator])
  }
}

