package org.openurp.platform.resource

import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.net.ssl.HostnameVerifier
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection
import javax.script.ScriptEngineManager
import java.{ util => ju }

trait Configurer {

  def properties: ju.Properties
}

class RemoteConfigurer(val url: URL) extends Configurer {

  def properties: ju.Properties = {
    parse(getResponseText(url))
  }

  protected[resource] def parse(string: String): ju.Properties = {
    val sem = new ScriptEngineManager
    val engine = sem.getEngineByName("javascript")
    val result = new ju.Properties
    val iter = engine.eval("result =" + string).asInstanceOf[ju.Map[_, _]].entrySet().iterator()
    while (iter.hasNext) {
      val one = iter.next.asInstanceOf[ju.Map.Entry[_, AnyRef]]
      result.put(one.getKey.toString, one.getValue.toString())
    }
    result
  }

  private def getResponseText(constructedUrl: URL): String = {
    var conn: HttpURLConnection = null
    try {
      conn = constructedUrl.openConnection().asInstanceOf[HttpURLConnection]
      var in: BufferedReader = null
      in = new BufferedReader(new InputStreamReader(conn.getInputStream, "UTF-8"))
      var line: String = in.readLine()
      val stringBuffer = new StringBuffer(255)
      stringBuffer.synchronized {
        while (line != null) {
          stringBuffer.append(line)
          stringBuffer.append("\n")
          line = in.readLine()
        }
        stringBuffer.toString
      }
    } catch {
      case e: Exception => throw new RuntimeException(e)
    } finally {
      if (conn != null) conn.disconnect()
    }
  }
}