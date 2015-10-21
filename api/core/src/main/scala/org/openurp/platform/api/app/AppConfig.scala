package org.openurp.platform.api.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, SystemInfo }
import org.beangle.commons.logging.Logging
import org.beangle.commons.lang.Strings
import org.openurp.platform.api.Urp
import java.io.FileInputStream
import java.io.File

object AppConfig extends Logging {

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
    result ++= IOs.readJavaProperties(new File(Urp.home + appPath + "/conf.properties").toURI.toURL)
    result.put("path", appPath)

    val appFile = new File(Urp.home + appPath + ".xml")
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
    val homefile = new File(Urp.home + path + ".xml")
    if (homefile.exists) Some(homefile)
    else None
  }

  def getFile(file: String): Option[File] = {
    val homefile =
      if (file.startsWith("/")) new File(Urp.home + path + file)
      else new File(Urp.home + path + "/" + file)

    if (homefile.exists) Some(homefile)
    else None
  }

  def getDatasourceUrl(resourceKey: String): String = {
    Urp.platformBase + "/kernel/datasource/" + name + "/" + resourceKey + ".json?secret=" + secret
  }
}