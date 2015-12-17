package org.openurp.platform.ws.security

import org.beangle.commons.collection.{ Order, Properties }
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.{ ActionSupport, EntitySupport }
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.security.model.MenuProfile

class MenuProfileWS(entityDao: EntityDao) extends ActionSupport with EntitySupport[MenuProfile] {

  def getQueryBuilder(): OqlBuilder[MenuProfile] = {
    val builder = OqlBuilder.from(classOf[MenuProfile], "menuProfile")
    QueryHelper.populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(null)
  }

  @response
  @mapping("")
  def index(@param("app") app: String): Seq[Any] = {
    val rs = entityDao.search(getQueryBuilder().where("menuProfile.app.name=:name", app))
    val mps = new collection.mutable.ListBuffer[Properties]
    for (one <- rs) {
      val mp = new Properties(one, "id", "name")
      mp.add("app", one.asInstanceOf[MenuProfile].app, "id", "title")
      mps += mp
    }
    mps
  }

}