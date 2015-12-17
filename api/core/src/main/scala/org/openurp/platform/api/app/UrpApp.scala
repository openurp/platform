package org.openurp.platform.api.app

import java.net.URL
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, SystemInfo }
import org.beangle.commons.logging.Logging
import org.beangle.commons.lang.Strings
import org.openurp.platform.api.Urp
import java.io.FileInputStream
import java.io.File
import org.openurp.platform.api.util.JSON
import org.beangle.commons.collection.Properties
import org.beangle.commons.web.util.HttpUtils
import org.beangle.commons.conversion.converter.String2DateConverter

object UrpApp extends Logging {

  val properties: Map[String, Any] = readProperties
  val name: String = properties("name").asInstanceOf[String]
  val path: String = properties("path").asInstanceOf[String]

  private var _token: Token = _

  def secret: String = {
    properties.getOrElse("secret", name).asInstanceOf[String]
  }

  def token: String = {
    if (null != _token) {
      _token.expiredAt < System.currentTimeMillis()
      _token = null
    }

    if (null == _token) {
      val tokenUrl = Urp.platformBase + "/kernel/token/login?app=" + name + "&secret=" + secret
      val token = JSON.parse(HttpUtils.getResponseText(tokenUrl)).asInstanceOf[Properties]
      _token = Token(token("token").asInstanceOf[String], (new String2DateConverter).convert(token("expiredAt"), classOf[java.util.Date]).asInstanceOf[java.util.Date].getTime)
    }
    _token.token
  }

  private def readProperties(): Map[String, Any] = {
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

    //app path starts with /
    var appPath = Strings.replace(name, "-", "/")
    appPath = Strings.replace(appPath, ".", "/")
    appPath = if (appPath.contains("/")) ("/" + Strings.substringAfter(appPath, "/")) else "/" + appPath

    val result = new collection.mutable.HashMap[String, Any]
    result ++= appManifest
    val appconf = new File(Urp.home + appPath + "/conf.properties")
    if (appconf.exists) result ++= IOs.readJavaProperties(appconf.toURI.toURL)
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

  def getUrpAppFile: Option[File] = {
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

}

case class Token(token: String, expiredAt: Long)