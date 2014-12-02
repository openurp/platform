package org.openurp.platform.util

import java.{ util => ju }
import javax.script.ScriptEngineManager
import org.beangle.commons.collection.Properties
import javax.script.Bindings

//FIXME generalize it.
object JSON {
  def parse(string: String): Any = {
    val sem = new ScriptEngineManager
    val engine = sem.getEngineByName("javascript")
    val result = new Properties

    engine.eval("result =" + string) match {
      case d: String => d
      case n: Number => n
      case b: ju.Map[_, _] =>
        val iter = b.asInstanceOf[ju.Map[_, _]].entrySet().iterator()
        while (iter.hasNext) {
          val one = iter.next.asInstanceOf[ju.Map.Entry[_, AnyRef]]
          val value = one.getValue match {
            case d: java.lang.Double =>
              if (java.lang.Double.compare(d, d.intValue) > 0) d.toString
              else String.valueOf(d.intValue)
            case a: Any => a.toString
          }
          result.put(one.getKey.toString, value)
        }
        result
    }

  }
}