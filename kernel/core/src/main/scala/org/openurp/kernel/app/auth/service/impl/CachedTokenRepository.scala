package org.openurp.kernel.app.auth.service.impl

import java.util.Calendar

import org.beangle.commons.cache.CacheManager
import org.beangle.data.model.dao.EntityDao
import org.openurp.kernel.app.App
import org.openurp.kernel.app.auth.service.TokenRepository
import org.openurp.kernel.app.model.AccessTokenBean

class CachedTokenRepository(val cacheManager: CacheManager, val entityDao: EntityDao) extends TokenRepository {

  val apps = cacheManager.getCache[String, String]("app.token1")

  val tokens = cacheManager.getCache[String, AccessTokenBean]("app.token2")

  override def getApp(token: String): Option[String] = {
    apps.get(token) match {
      case Some(t) => Some(t)
      case None =>
        val rs = entityDao.findBy(classOf[AccessTokenBean], "token", List(token))
        if (rs.isEmpty) None
        else {
          val appName = rs.head.app.name
          apps.put(token, appName)
          tokens.put(appName, rs.head)
          Some(appName)
        }
    }

  }

  override def getToken(app: App): Option[AccessTokenBean] = {
    tokens.get(app.name) match {
      case Some(t) => if (t.expired) None else Some(t)
      case None => {
        val rs = entityDao.findBy(classOf[AccessTokenBean], "app", List(app))
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

  override def put(app: App, token: String): AccessTokenBean = {
    val expiredAt = Calendar.getInstance
    expiredAt.add(Calendar.MONTH, 12)
    val newToken = new AccessTokenBean
    newToken.token = token
    newToken.app = app
    newToken.expiredAt = expiredAt.getTime
    entityDao.saveOrUpdate(newToken)
    apps.put(token, app.name)
    tokens.put(app.name, newToken)
    newToken
  }
}