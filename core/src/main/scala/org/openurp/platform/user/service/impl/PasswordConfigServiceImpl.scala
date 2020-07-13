package org.openurp.platform.user.service.impl

import org.beangle.commons.bean.Initializing
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.platform.config.service.DomainService
import org.openurp.platform.user.model.PasswordConfig
import org.openurp.platform.user.service.PasswordConfigService

class PasswordConfigServiceImpl extends PasswordConfigService with Initializing {

  var domainService: DomainService = _

  var entityDao: EntityDao = _
  var defaultConfig: PasswordConfig = _

  override def init(): Unit = {
    defaultConfig = new PasswordConfig
    defaultConfig.maxdays = 180
    defaultConfig.idledays = 10
  }

  override def get(): PasswordConfig = {
    val builder = OqlBuilder.from(classOf[PasswordConfig], "pc")
    builder.where("pc.domain=:domain", domainService.getDomain)
    entityDao.search(builder).headOption.getOrElse(defaultConfig)
  }
}
