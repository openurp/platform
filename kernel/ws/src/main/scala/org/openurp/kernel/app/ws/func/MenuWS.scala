package org.openurp.kernel.app.ws.func

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.EntityActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param, response }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.kernel.app.func.Menu

class MenuWS extends EntityActionSupport[Menu] {

  var entityDao: EntityDao = _

  def getQueryBuilder(): OqlBuilder[Menu] = {
    val builder = OqlBuilder.from(classOf[Menu], "menu")
    QueryHelper.populateConditions(builder)
    builder.where("menu.enabled=true")
    get("top") foreach { t =>
      if (t == "true") builder.where("menu.parent is null")
    }

    get("indexno") foreach { indexno =>
      builder.where("menu.indexno like :indexno and length(menu.indexno) > :length ", indexno + ".%", indexno.length)

      if (!getBoolean("flat", false))
        builder.where("menu.indexno not like :indexno2", indexno + ".%.%")
    }
    builder.orderBy("menu.indexno").limit(null)
  }

  @response
  @mapping("{profileId}")
  def index(@param("app") app: String, profileId: String): Seq[Any] = {
    val builder = getQueryBuilder()
    builder.where("menu.profile.app.name=:app", app)
    builder.where("menu.profile.id=:profileId", Integer.valueOf(profileId))
    val rs = entityDao.search(builder)
    val menus = new collection.mutable.ListBuffer[Properties]
    val flat = get("top").isDefined || getBoolean("flat", false)
    for (one <- rs) {
      menus += convert(one, flat)
    }
    menus
  }

  private def convert(one: Menu, flat: Boolean): Properties = {
    val menu = new Properties(one, "id", "name", "indexno")
    if (null != one.entry) menu.put("entry", one.entry.name)
    if (!one.children.isEmpty && !flat) {
      val children = new collection.mutable.ListBuffer[Properties]
      for (child <- one.children) {
        children += convert(child.asInstanceOf[Menu], flat)
      }
      menu.put("children", children)
    }
    menu
  }

}