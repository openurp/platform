package org.openurp.platform.security.service.impl

import org.beangle.security.session.profile.ProfileProvider
import org.beangle.data.model.dao.EntityDao
import org.beangle.security.session.profile.SessionProfile
import org.beangle.security.authc.Account
import org.openurp.platform.security.model.SessionProfileBean
import org.beangle.data.jpa.dao.OqlBuilder
import org.openurp.platform.api.app.AppConfig
import org.beangle.security.session.profile.DefaultSessionProfile
import org.openurp.platform.security.model.User

class SessionProfileProvider(entityDao: EntityDao) extends ProfileProvider {

  def getProfile(auth: Account): SessionProfile = {
    val id = auth.id.asInstanceOf[java.lang.Long]
    val user = entityDao.get(classOf[User], id)
    val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
    query.where("sp.category=:category", user.category).where("sp.app.name=:appName", AppConfig.appName)
    query.cacheable()
    val sps = entityDao.search(query)
    if (sps.isEmpty) {
      val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
      query.where("sp.app.name=:appName", AppConfig.appName).cacheable()
      if (entityDao.search(query).isEmpty) {
        DefaultSessionProfile
      } else {
        throw new RuntimeException(s"Cannot find session profile for ${user.name} with category ${user.category.name} in ${AppConfig.appName}")
      }
    } else {
      sps.head
    }
  }

  def getProfiles(): Iterable[SessionProfile] = {
    val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
    query.where("sp.app.name=:appName", AppConfig.appName)
    entityDao.search(query)
  }
}