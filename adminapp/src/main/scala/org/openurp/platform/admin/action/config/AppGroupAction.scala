package org.openurp.platform.admin.action.config

import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.AppGroup
import org.openurp.platform.config.service.DomainService

class AppGroupAction extends RestfulAction[AppGroup] {

  var domainService: DomainService = _

  @ignore
  override protected def saveAndRedirect(group: AppGroup): View = {
    group.domain = domainService.getDomain
    super.saveAndRedirect(group)
  }

  override protected def getQueryBuilder: OqlBuilder[AppGroup] = {
      super.getQueryBuilder.where("appGroup.domain=:domain", domainService.getDomain)
  }
}
