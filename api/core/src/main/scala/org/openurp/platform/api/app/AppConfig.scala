package org.openurp.platform.api.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, SystemInfo }
import org.beangle.commons.logging.Logging
import org.openurp.platform.api.ws.ServiceConfig
import java.io.FileInputStream
import org.beangle.commons.lang.Strings
import java.io.File

object AppConfig extends Logging {
  val urphome = SystemInfo.properties.get("OPENURP_HOME").getOrElse(SystemInfo.user.home + "/.openurp")

  val properties: Map[String, String] = readProperties
  val appName: String = properties("appName")
  val appPath: String = properties("appPath")

  def secret: String = {
    properties("secret")
  }

  private def readProperties(): Map[String, String] = {
    val configs = ClassLoaders.getResources("META-INF/openurp/app.properties")
    val appManifest = if (configs.isEmpty) {
      Map.empty[String, String]
    } else {
      IOs.readJavaProperties(configs.head)
    }
    val appName = appManifest.get("name") match {
      case Some(name) => name
      case None => throw new RuntimeException("cannot find META-INF/openurp/app.properties")
    }

    var appPath = Strings.replace(appName, "-", "/")
    appPath = Strings.replace(appPath, ".", "/")
    if (appPath.startsWith("openurp/")) appPath = Strings.substringAfter(appPath, "openurp")
    else appPath = "/" + appPath

    val result = new collection.mutable.HashMap[String, String]
    result ++= appManifest
    result ++= readProperties(new File(urphome + appPath + "/conf.properties"))
    result.put("appName", appName)
    result.put("appPath", appPath)
    result.toMap
  }

  def getFile(path: String): Option[File] = {
    val homefile = new File(urphome + appPath + path)
    if (homefile.exists) Some(homefile)
    else None
  }

  private def readProperties(files: File*): Map[String, String] = {
    val result = new collection.mutable.HashMap[String, String]
    files foreach { file =>
      if (file.exists) result ++= IOs.readJavaProperties(file.toURL())
    }
    result.toMap
  }

  def getDatasourceUrl(resourceKey: String): String = {
    ServiceConfig.dsBase + "/app/" + appName + "/resource/datasources/" + resourceKey + ".json?secret=" + secret
  }
}