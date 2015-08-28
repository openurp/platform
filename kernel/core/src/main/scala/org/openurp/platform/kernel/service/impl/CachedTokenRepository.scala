package org.openurp.platform.kernel.service.impl

import java.util.Calendar
import org.beangle.commons.cache.CacheManager
import org.beangle.data.model.dao.EntityDao
import org.openurp.platform.kernel.model.App
import org.openurp.platform.kernel.service.TokenRepository
import org.openurp.platform.kernel.model.AccessToken

class CachedTokenRepository(val cacheManager: CacheManager, val entityDao: EntityDao) extends TokenRepository {

  val apps = cacheManager.getCache[String, String]("app.token1")

  val tokens = cacheManager.getCache[String, AccessToken]("app.token2")

  override def getApp(token: String): Option[String] = {
    apps.get(token) match {
      case Some(t) => Some(t)
      case None =>
        val rs = entityDao.findBy(classOf[AccessToken], "token", List(token))
        if (rs.isEmpty) None
        else {
          val appName = rs.head.app.name
          apps.put(token, appName)
          tokens.put(appName, rs.head)
          Some(appName)
        }
    }

  }

  override def getToken(app: App): Option[AccessToken] = {
    tokens.get(app.name) match {
      case Some(t) => if (t.expired) None else Some(t)
      case None => {
        val rs = entityDao.findBy(classOf[AccessToken], "app", List(app))
        if (rs.isEmpty) None
        else {
          val appName = rs.head.app.name
          val token = rs.head
          apps.put(token.token, appName)
          tokens.put(appName, token)
          Some(token)
        }
      }
    }
  }

  override def put(app: App, token: String): AccessToken = {
    val expiredAt = Calendar.getInstance
    expiredAt.add(Calendar.MONTH, 12)
    val newToken = new AccessToken
    newToken.token = token
    newToken.app = app
    newToken.expiredAt = expiredAt.getTime
    entityDao.saveOrUpdate(newToken)
    apps.put(token, app.name)
    tokens.put(app.name, newToken)
    newToken
  }
}