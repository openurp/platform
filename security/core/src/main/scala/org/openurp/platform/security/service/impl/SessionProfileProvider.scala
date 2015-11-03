package org.openurp.platform.security.service.impl

import org.beangle.security.session.profile.ProfileProvider
import org.beangle.data.dao.EntityDao
import org.beangle.security.session.profile.SessionProfile
import org.beangle.security.authc.Account
import org.openurp.platform.security.model.SessionProfileBean
import org.beangle.data.dao.OqlBuilder
import org.openurp.platform.api.app.UrpApp
import org.beangle.security.session.profile.DefaultSessionProfile
import org.openurp.platform.security.model.User

class SessionProfileProvider(entityDao: EntityDao) extends ProfileProvider {

  def getProfile(auth: Account): SessionProfile = {
    val user = entityDao.findBy(classOf[User],"code", List(auth.getName())).head
    val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
    query.where("sp.category=:category", user.category).where("sp.app.name=:appName", UrpApp.name)
    query.cacheable()
    val sps = entityDao.search(query)
    if (sps.isEmpty) {
      val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
      query.where("sp.app.name=:appName", UrpApp.name).cacheable()
      if (entityDao.search(query).isEmpty) {
        DefaultSessionProfile
      } else {
        throw new RuntimeException(s"Cannot find session profile for ${user.name} with category ${user.category.name} in ${UrpApp.name}")
      }
    } else {
      sps.head
    }
  }

  def getProfiles(): Iterable[SessionProfile] = {
    val query = OqlBuilder.from(classOf[SessionProfileBean], "sp")
    query.where("sp.app.name=:appName", UrpApp.name)
    val rs = entityDao.search(query)
    if (rs.isEmpty) return List(DefaultSessionProfile) else rs
  }
}