package org.openurp.app.ws.nav

import org.beangle.commons.collection.Properties
import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.security.blueprint.Menu
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.response
import org.beangle.webmvc.entity.helper.QueryHelper

class MenuWS extends ActionSupport {

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
  def index(): Seq[Any] = {
    val rs = entityDao.search(getQueryBuilder())
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
        children += convert(child, flat)
      }
      menu.put("children", children)
    }
    menu
  }

}