package org.openurp.platform.api.cas

import org.beangle.cache.CacheManager
import org.beangle.security.session.http.HttpSessionRepo
import org.beangle.security.realm.cas.CasConfig
import org.beangle.commons.io.BinarySerializer

class CasHttpSessionRepo(casConfig: CasConfig, cacheManager: CacheManager, serializer: BinarySerializer)
    extends HttpSessionRepo(cacheManager, serializer) {
  this.geturl = casConfig.casServer + "/session/{id}?format=" + serializer.mediaTypes.head.toString
  this.accessUrl = casConfig.casServer + "/session/{id}/access/{time}"
}