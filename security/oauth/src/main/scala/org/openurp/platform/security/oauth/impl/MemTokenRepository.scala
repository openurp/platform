package org.openurp.platform.security.oauth.impl

import java.util.Calendar
import org.beangle.commons.cache.CacheManager
import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.kernel.model.App
import org.openurp.platform.security.oauth.AccessToken
import org.openurp.platform.security.oauth.TokenRepository

class MemTokenRepository(val cacheManager: CacheManager) extends TokenRepository {

  val tokens = cacheManager.getCache[String, AccessToken]("app.token")

  override def get(token: String): Option[AccessToken] = {
    tokens.get(token)
  }

  override def put(token: AccessToken): Unit = {
    tokens.put(token.id, token)
  }
  
  override def remove(token:String):Unit={
    tokens.evict(token)
  }
}