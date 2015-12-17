package org.openurp.platform.oauth.service.impl

import org.beangle.commons.cache.CacheManager
import org.openurp.platform.config.model.AccessToken
import org.openurp.platform.oauth.service.TokenRepository
import org.openurp.platform.config.model.AccessToken

class MemTokenRepository(val cacheManager: CacheManager) extends TokenRepository {

  val tokens = cacheManager.getCache("app.token", classOf[String], classOf[AccessToken])

  override def get(token: String): Option[AccessToken] = {
    tokens.get(token)
  }

  override def put(token: AccessToken): Unit = {
    tokens.put(token.id, token)
  }

  override def remove(token: String): Unit = {
    tokens.evict(token)
  }
}