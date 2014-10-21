package org.openurp.platform.ws

import org.beangle.commons.lang.ClassLoaders
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.SystemInfo

object ServiceConfig {

  val location = "META-INF/openurp/service.properties"
  val base = readConfig

  def readConfig(): String = {
    SystemInfo.properties.get("openurp.service.base") match {
      case Some(b) => b
      case None => {
        val configs = ClassLoaders.getResources(location)
        if (configs.isEmpty) {
          "http://localhost:8080"
        } else {
          IOs.readJavaProperties(configs.head)("base")

        }
      }
    }
  }
}