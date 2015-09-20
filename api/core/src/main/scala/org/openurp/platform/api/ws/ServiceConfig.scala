package org.openurp.platform.api.ws

import org.beangle.commons.lang.ClassLoaders
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.SystemInfo

object ServiceConfig {

  val urphome = SystemInfo.properties.get("OPENURP_HOME").getOrElse(SystemInfo.user.home + "/.openurp")

  val location = urphome + "/service.properties"

  val wsBase = "http://" + readConfig(location, "service", "localhost:8080")

  val dsBase = "http://" + readConfig(location, "data", "localhost:8080")

  def readConfig(location: String, property: String, defaultValue: String): String = {
    val configs = ClassLoaders.getResources(location)
    if (configs.isEmpty) {
      defaultValue
    } else {
      val p = IOs.readJavaProperties(configs.head)
      p.get(property).orElse(p.get("openurp.base").map(v => property + "." + v)).getOrElse(defaultValue)
    }
  }
}
