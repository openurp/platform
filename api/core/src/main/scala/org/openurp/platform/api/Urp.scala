package org.openurp.platform.api

import org.beangle.commons.io.IOs
import org.beangle.commons.lang.SystemInfo
import org.beangle.commons.lang.Strings
import java.io.File

object Urp {

  val home = SystemInfo.properties.get("openurp.home").getOrElse(SystemInfo.user.home + "/.openurp")

  val properties = readConfig(home + "/conf.properties")

  val base = readBase("openurp.base", "localhost:8080")

  val wsBase = readBase("openurp.service", "localhost:8080")

  val platformBase = readBase("openurp.platform", "localhost:8080")

  val webappBase = readBase("openurp.webapp", "localhost:8080")

  private def readConfig(location: String): Map[String, String] = {
    val configFile = new File(location)
    if (!configFile.exists) {
      Map.empty
    } else {
      IOs.readJavaProperties(configFile.toURI().toURL())
    }
  }

  private def readBase(property: String, defaultValue: String): String = {
    var result =
      if (properties.isEmpty) {
        defaultValue
      } else {
        properties.get(property) match {
          case Some(v) => v
          case None =>
            properties.get("openurp.base") match {
              case Some(base) => Strings.replace(property, "openurp.", "") + "." + base
              case None       => Strings.replace(defaultValue, "openurp.", "")
            }
        }
      }
    if (result.endsWith("/")) result = result.substring(0, result.length - 1)
    if (result.startsWith("http")) result else "http://" + result
  }
}
