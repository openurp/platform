package org.openurp.platform.api

import org.beangle.commons.io.IOs
import org.beangle.commons.lang.SystemInfo
import org.beangle.commons.lang.Strings
import java.io.File

object Urp {

  val home = SystemInfo.properties.get("OPENURP_HOME").getOrElse(SystemInfo.user.home + "/.openurp")

  val properties = readConfig(home + "/conf.properties")

  val wsBase = "http://" + readBase("openurp.service", "localhost:8080")

  val platformBase = "http://" + readBase("openurp.platform", "localhost:8080")

  private def readConfig(location: String): Map[String, String] = {
    val configFile = new File(location)
    if (!configFile.exists) {
      Map.empty
    } else {
      IOs.readJavaProperties(configFile.toURI().toURL())
    }
  }

  private def readBase(property: String, defaultValue: String): String = {
    if (properties.isEmpty) {
      defaultValue
    } else {
      properties.get(property) match {
        case Some(v) => v
        case None =>
          properties.get("openurp.base") match {
            case Some(base) => Strings.replace(property, "openurp.", "") + "." + base
            case None => Strings.replace(defaultValue, "openurp.", "")
          }
      }
    }
  }
}