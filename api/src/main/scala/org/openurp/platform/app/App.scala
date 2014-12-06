package org.openurp.platform.app

import java.net.URL

import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, SystemInfo }
import org.beangle.commons.logging.Logging
import org.openurp.platform.resource.{ Config, RemoteConfig }
import org.openurp.platform.ws.ServiceConfig

object App extends Logging {
  private val location = "META-INF/openurp/app.properties"
  val properties = readProperties

  def name: String = properties("name")

  def secret: String = {
    SystemInfo.properties.get("openurp.app.secret") match {
      case Some(b) => b
      case None => properties("secret")
    }
  }

  private def readProperties: Map[String, String] = {
    val configs = ClassLoaders.getResources(location)
    if (configs.isEmpty) {
      warn(s"Cannot find $location")
      Map.empty
    } else {
      IOs.readJavaProperties(configs.head)
    }
  }

  def getDatasourceConfig(resourceKey: String): Config = {
    new RemoteConfig(new URL(ServiceConfig.dsBase + "/app/" + name + "/resource/datasources/" + resourceKey + ".json?secret=" + secret))
  }
}