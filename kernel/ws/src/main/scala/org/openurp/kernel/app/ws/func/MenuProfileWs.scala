package org.openurp.kernel.app.ws.func

import org.beangle.commons.collection.{ Order, Properties }
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.EntityActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.kernel.app.func.model.MenuProfileBean

class MenuProfileWS extends EntityActionSupport[MenuProfileBean] {

  var entityDao: EntityDao = _

  def getQueryBuilder(): OqlBuilder[MenuProfileBean] = {
    val builder = OqlBuilder.from(classOf[MenuProfileBean], "menuProfile")
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
      mp.add("app", one.app, "id", "title")
      mps += mp
    }
    mps
  }

}