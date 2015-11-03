package org.openurp.platform.security.action

import org.beangle.webmvc.entity.action.RestfulAction
import org.beangle.security.session.profile.SessionProfile
import org.openurp.platform.security.model.SessionProfileBean
import org.openurp.platform.security.helper.AppHelper
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.model.Entity
import org.beangle.webmvc.api.annotation.ignore
import org.openurp.platform.security.model.UserCategory
import org.openurp.platform.api.app.UrpApp
import org.beangle.webmvc.api.view.View
import org.openurp.platform.security.model.SessionProfileBean
import org.openurp.platform.security.model.SessionProfileBean
import org.openurp.platform.kernel.model.App
/**
 * @author xinzhou
 */
class SessionProfileAction extends RestfulAction[SessionProfileBean] {

  @ignore
  protected override def shortName: String = {
    "profile"
  }

  override def search(): String = {
    put(shortName + "s", entityDao.search(getQueryBuilder()))
    val categories = entityDao.getAll(classOf[UserCategory]).toBuffer
    val builder = OqlBuilder.from(classOf[SessionProfileBean], "profile").
      where("profile.app.id=:appId", getInt("profile.app.id").get).select("distinct profile.category")
    categories --= entityDao.search(builder).asInstanceOf[Seq[UserCategory]]
    put("categories", categories)
    forward()
  }

  protected override def indexSetting(): Unit = {
    put("apps", AppHelper.getApps(entityDao))
  }

  override def save(): View = {
    val profiles = entityDao.getAll(classOf[SessionProfileBean]).toBuffer
    for (profile <- profiles) {
      val categoryId = profile.category.id
      val max = getInt("capacity_" + categoryId)
      val maxSessions = getShort("maxSession_" + categoryId)
      val timeout = getShort("timeout_" + categoryId)
      if (None != max && None != maxSessions && None != timeout) {
        profile.capacity = max.get
        profile.maxSession = maxSessions.get
        profile.timeout = timeout.get
      }
    }
    val categoryId = getInt("categoryId_new")
    val max = getInt("capacity_new")
    val maxSessions = getShort("maxSession_new")
    val timeout = getShort("timeout_new")
    val appId = getInt("profile.app.id").get
    if (None != max && None != maxSessions && None != timeout) {
      val newProfile = new SessionProfileBean()
      newProfile.category = entityDao.get(classOf[UserCategory], categoryId.get)
      newProfile.capacity = max.get
      newProfile.maxSession = maxSessions.get
      newProfile.timeout = timeout.get
      newProfile.app = new App
      newProfile.app.id = appId
      profiles += newProfile
    }
    entityDao.saveOrUpdate(profiles)
    return redirect("search", "&profile.app.id=" + appId, "info.save.success")
  }

}