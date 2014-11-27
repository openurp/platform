package org.openurp.security.action

import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.entity.action.AbstractEntityAction
import org.beangle.data.jpa.dao.OqlBuilder
import org.openurp.security.model.DataPermissionBean
import org.beangle.security.blueprint.Field
import org.openurp.security.model.Member
import org.beangle.security.blueprint.MenuProfile
import org.beangle.security.blueprint.FuncResource
import org.beangle.security.blueprint.Menu

class IndexAction extends AbstractEntityAction {

  def stat(): String = {
    populateUserStat()
    // state menus
    val menuProfiles = entityDao.getAll(classOf[MenuProfile])
    val menuStats = new collection.mutable.HashMap[Integer, Seq[_]]
    for (profile <- menuProfiles) {
      val menuQuery = OqlBuilder.from(classOf[Menu], "menu")
      menuQuery.where("menu.profile=:profile", profile).select("menu.enabled,count(*)").groupBy("enabled")
      menuStats.put(profile.id, entityDao.search(menuQuery))
    }
    put("menuProfiles", menuProfiles)
    put("menuStats", menuStats)

    // stat resource
    val resourceQuery = OqlBuilder.from(classOf[FuncResource], "resource")
    resourceQuery.select("resource.enabled,count(*)").groupBy("enabled")
    put("resourceStat", entityDao.search(resourceQuery))

    // stat dataPermission and restriction
    put("dataPermissionCnt",
      entityDao.search(OqlBuilder.from(classOf[DataPermissionBean], "p").select("count(*)")))
    put("fieldCnt", entityDao.search(OqlBuilder.from(classOf[Field], "param").select("count(*)")))
    forward()
  }

  private def populateUserStat(): Unit = {
    val userQuery = OqlBuilder.from(classOf[Member], "gm")
    userQuery.select("gm.role.indexno,gm.role.name,gm.user.enabled,count(*)").groupBy(
      "gm.role.indexno,gm.role.name,gm.user.enabled")
    val datas = entityDao.search(userQuery)
    val rs = new collection.mutable.HashMap[String, collection.mutable.Map[Object, Object]]
    for (data <- datas) {
      val roleStat = data.asInstanceOf[Array[Object]]
      val key = roleStat(0) + " " + roleStat(1)
      val statusMap = rs.getOrElseUpdate(key, new collection.mutable.HashMap[Object, Object])
      statusMap.put(roleStat(2), roleStat(3))
    }
    put("userStat", rs)
  }

}
