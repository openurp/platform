package org.openurp.platform.api.ws

import org.beangle.commons.lang.ClassLoaders
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.SystemInfo

object ServiceConfig {

  val location = "META-INF/openurp/service.properties"

  val wsBase = "http://" + readConfig(location, "service", "localhost:8080")

  val dsBase = "http://" + readConfig(location, "data", "localhost:8080")

  def readConfig(location: String, property: String, defaultValue: String): String = {
    SystemInfo.properties.get("openurp." + property + ".base") match {
      case Some(b) => b
      case None => {
        val configs = ClassLoaders.getResources(location)
        if (configs.isEmpty) {
          SystemInfo.properties.get("openurp.base") match {
            case Some(v) => property + "." + v
            case None => defaultValue
          }
        } else {
          IOs.readJavaProperties(configs.head).getOrElse(property, defaultValue)
        }
      }
    }
  }
}