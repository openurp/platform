package org.openurp.platform.web.tag

import org.beangle.commons.bean.PropertyUtils
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, Primitives, Strings, SystemInfo }
import org.beangle.webmvc.view.tag.{ AbstractModels, ComponentContext }
import org.beangle.webmvc.view.tag.components.ClosingUIBean
import org.beangle.webmvc.view.tag.freemarker.TagModel

import javax.servlet.http.HttpServletRequest

object UrpService {
  var base: String = readConfig
  def readConfig(): String = {
    SystemInfo.properties.get("urp_service_base") match {
      case Some(b) => b
      case None => {
        val configs = ClassLoaders.getResources("urp_service.properties")
        if (configs.isEmpty) {
          throw new RuntimeException("Cannot find urp_service.properties")
        } else {
          IOs.readJavaProperties(configs.head)("base")
        }
      }
    }
  }
}

class UrpModels(context: ComponentContext, request: HttpServletRequest) extends AbstractModels(context, request) {
  def service(url: String): String = {
    UrpService.base + url + ".json"
  }
}