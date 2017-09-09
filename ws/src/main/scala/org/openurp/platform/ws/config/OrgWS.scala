package org.openurp.platform.ws.config

import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.openurp.platform.config.model.Org
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.api.annotation.response
import org.beangle.commons.collection.Properties

class OrgWS(entityDao: EntityDao) extends ActionSupport {

  @response
  def index(): Properties = {
    val orgs = entityDao.getAll(classOf[Org])
    if (orgs.isEmpty) {
      new Properties()
    } else {
      new Properties(orgs.head, "id", "code", "name", "shortName", "logoUrl", "wwwUrl")
    }
  }
}