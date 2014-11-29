package org.openurp.platform.app.service.impl

import java.net.URL

import org.beangle.commons.io.IOs
import org.openurp.platform.app.App
import org.openurp.platform.app.service.AppService
import org.openurp.platform.ws.ServiceConfig

class RemoteAppServiceImpl extends AppService {

  def isPublic(resource: String): Boolean = {
    true
//    val is = new URL(ServiceConfig.wsBase + "/app/func/public?resource=" + resource).openStream()
//    val result = IOs.readString(is) == "true"
//    is.close()
//    result
  }

  def isPermitted(appId: String, resource: String, method: String): Boolean = {
    true
//    val is = new URL(ServiceConfig.wsBase + "/app/func/permitted?resource=" + resource + "&appId=" + App.name + "&secret=" + App.secret).openStream()
//    val result = IOs.readString(is) == "true"
//    is.close()
//    result
  }

}