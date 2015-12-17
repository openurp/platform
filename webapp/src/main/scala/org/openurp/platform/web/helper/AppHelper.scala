package org.openurp.platform.web.helper
import org.beangle.webmvc.api.context.Params
import org.beangle.webmvc.api.context.ActionContext
import org.beangle.data.dao.EntityDao
import org.openurp.platform.config.model.App
import org.beangle.data.dao.OqlBuilder

object AppHelper {

  def setAppId(id: Integer): Unit = {
    ActionContext.current.request.getSession.setAttribute("app.id", id)
  }

  def getAppId(): Option[Integer] = {
    Params.getInt("app.id") match {
      case Some(id) =>
        setAppId(id); Some(id)
      case None =>
        val appId = ActionContext.current.request.getSession.getAttribute("app.id").asInstanceOf[Integer]
        if (null == appId) None else Some(appId)
    }
  }

  def getApps(entityDao: EntityDao): Seq[App] = {
    val apps = entityDao.search(OqlBuilder.from(classOf[App], "app").orderBy("app.name")).toBuffer
    getAppId foreach { appId =>
      apps.find(p => p.id == appId) foreach { app =>
        apps -= app; apps.insert(0, app)
      }
    }
    apps
  }
}