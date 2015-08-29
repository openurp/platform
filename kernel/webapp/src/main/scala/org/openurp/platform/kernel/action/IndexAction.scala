package org.openurp.platform.kernel.action

import org.beangle.data.jpa.dao.OqlBuilder
import org.beangle.data.model.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.param
import org.openurp.platform.api.app.App
import org.openurp.platform.security.model.Menu

class IndexAction extends ActionSupport {
  var entityDao: EntityDao = _

  def index(): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", App.name).where("menu.parent is null")
    query.orderBy("menu.indexno")
    put("menus", entityDao.search(query))
    forward()
  }

  def menus(@param("indexno") indexno: String): String = {
    val query = OqlBuilder.from(classOf[Menu], "menu")
    query.where("menu.profile.app.name=:app", App.name)
    query.where("menu.indexno = :indexno ", indexno)
    val menus = entityDao.search(query)
    put("menus", menus)
    forward()
  }
}