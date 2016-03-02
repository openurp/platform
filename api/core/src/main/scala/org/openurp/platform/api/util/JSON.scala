package org.openurp.platform.api.util

import java.{ util => ju }
import javax.script.ScriptEngineManager
import org.beangle.commons.collection.Properties
import javax.script.Bindings
import org.beangle.commons.lang.Chars
import org.beangle.commons.lang.Strings

//FIXME generalize it.
object JSON {
  def parse(string: String): Any = {
    val sem = new ScriptEngineManager
    val engine = sem.getEngineByName("javascript")
    if (Strings.isBlank(string) || "{}".equals(string)) {
      return new Properties()
    }
    engine.eval("result =" + string) match {
      case d: String => d
      case n: Number => n
      case b: ju.Map[_, _] =>
        if (isArray(string)) {
          collection.JavaConversions.collectionAsScalaIterable(b.values())
        } else {
          val iter = b.entrySet().iterator()
          val result = new Properties
          while (iter.hasNext) {
            val one = iter.next
            result.put(one.getKey.toString, convert(one.getValue.asInstanceOf[Object]))
          }
          result
        }
      case l: ju.Collection[_] => collection.JavaConversions.collectionAsScalaIterable(l)
    }
  }

  def convert(value: Object): Object = {
    value match {
      case d: String => d
      case n: Number => n
      case b: ju.Map[_, _] =>
        val iter = b.entrySet().iterator()
        val signature = b.toString
        if (signature.contains("Array")) {
          val result = new collection.mutable.ArrayBuffer[Any]
          while (iter.hasNext) {
            val one = iter.next
            result += convert(one.getValue.asInstanceOf[Object])
          }
          result
        } else {
          val result = new Properties
          while (iter.hasNext) {
            val one = iter.next
            result.put(one.getKey.toString, convert(one.getValue.asInstanceOf[Object]))
          }
          result
        }
      case l: ju.Collection[_] => collection.JavaConversions.collectionAsScalaIterable(l)
      case _ => value
    }
  }
  def isArray(str: String): Boolean = {
    var i = 0;
    while (i < str.length) {
      val c = str.charAt(i)
      if (!Character.isWhitespace(c)) {
        return c == '['
      }
      i += 1
    }
    false
  }
}