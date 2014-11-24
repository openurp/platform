package org.openurp.app.ws.nav

import org.beangle.webmvc.entity.action.RestfulService
import org.beangle.security.blueprint.Menu
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.beangle.webmvc.api.annotation.response
import org.beangle.commons.collection.Properties
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.entity.helper.QueryHelper
import org.beangle.security.blueprint.MenuProfile
import org.openurp.app.nav.model.MenuProfileBean

class MenuProfileWS extends ActionSupport {

  var entityDao: EntityDao = _

  def getQueryBuilder(): OqlBuilder[MenuProfileBean] = {
    val builder = OqlBuilder.from(classOf[MenuProfileBean], "menuProfile")
    QueryHelper.populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(null)
  }

  @response
  def index(): Seq[Any] = {
    val rs = entityDao.search(getQueryBuilder())
    val mps = new collection.mutable.ListBuffer[Properties]
    for (one <- rs) { 
      val mp = new Properties(one, "id", "name")
      mp.add("app", one.app, "id", "title")
      mps += mp
    }
    mps
  }

}