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
  val name: String = properties("name")
  val path: String = properties("path")

  def secret: String = {
    properties.getOrElse("secret", name)
  }

  private def readProperties(): Map[String, String] = {
    val configs = ClassLoaders.getResources("META-INF/openurp/app.properties")
    val appManifest = if (configs.isEmpty) {
      Map.empty[String, String]
    } else {
      IOs.readJavaProperties(configs.head)
    }
    val name = appManifest.get("name") match {
      case Some(n) => n
      case None => throw new RuntimeException("cannot find META-INF/openurp/app.properties")
    }

    var appPath = Strings.replace(name, "-", "/")
    appPath = Strings.replace(appPath, ".", "/")
    if (appPath.startsWith("openurp/")) appPath = Strings.substringAfter(appPath, "openurp")
    else appPath = "/" + appPath

    val result = new collection.mutable.HashMap[String, String]
    result ++= appManifest
    result ++= readProperties(new File(urphome + appPath + "/conf.properties"))
    result.put("path", appPath)

    val appFile = new File(urphome + appPath + ".xml")
    if (appFile.exists()) {
      val is = new FileInputStream(appFile)
      scala.xml.XML.load(is) \\ "app" foreach { app =>
        result ++= app.attributes.asAttrMap
      }
      IOs.close(is)
    }
    result.toMap
  }

  def getAppConfigFile: Option[File] = {
    val homefile = new File(urphome + path + ".xml")
    if (homefile.exists) Some(homefile)
    else None
  }

  def getFile(file: String): Option[File] = {
    val homefile =
      if (file.startsWith("/")) new File(urphome + path + file)
      else new File(urphome + path + "/" + file)

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
    ServiceConfig.platformBase + "/service/kernel/datasource/" + name + "/" + resourceKey + ".json?secret=" + secret
  }
}