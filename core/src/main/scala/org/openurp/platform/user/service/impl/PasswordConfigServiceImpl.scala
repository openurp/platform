/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
