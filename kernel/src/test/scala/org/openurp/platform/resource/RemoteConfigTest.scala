package org.openurp.platform.resource

import org.junit.runner.RunWith
import org.scalatest.{ FunSpec, Matchers }
import org.scalatest.junit.JUnitRunner
import java.util.Collection
@RunWith(classOf[JUnitRunner])
class RemoteConfigurerTest extends FunSpec with Matchers {
  describe("RemoteConfigurer") {
    it("parse json") {
      val configurer = new RemoteConfig(null)
      val properties = configurer.parse("""{"code":"0","name":"居民身份证","id":0}""")
      val iter = properties.entrySet.iterator
      while (iter.hasNext) {
        val entry = iter.next()
        print(entry.getKey(), entry.getKey().getClass)
        print("->")
        println(entry.getValue(), entry.getValue().getClass)
      }
    }
  }
}