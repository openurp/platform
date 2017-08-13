package org.openurp.platform.api.cas

import org.beangle.cache.CacheManager
import org.beangle.security.session.http.HttpSessionRepo
import org.beangle.security.realm.cas.CasConfig

class CasHttpSessionRepo(cacheManager: CacheManager, casConfig: CasConfig)
    extends HttpSessionRepo(cacheManager) {
  this.geturl = casConfig.casServer + "/session/{id}.bin"
  this.accessUrl = casConfig.casServer + "/session/{id}/access/{time}"
}