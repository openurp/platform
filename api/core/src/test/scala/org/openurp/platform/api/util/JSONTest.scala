package org.openurp.platform.api.util

import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

/**
 * @author chaostone
 */
@RunWith(classOf[JUnitRunner])
class JSONTest extends FunSpec with Matchers {
  describe("JSON") {
    it("parse") {
      val result = JSON.parse("""
            {"accountLocked":false,"details":{"isRoot":false},"authorities":[1,2],"accountExpired":false,"description":"duan","principal":"abc","credentialExpired":false,"disabled":false}
            """)
      assert(result.asInstanceOf[Map[String, _]]("authorities").isInstanceOf[collection.Seq[_]])
      val a = """[{"roles":[],"scope":"Protected","name":"/config/home","title":"首页","id":94}]"""
      val data = JSON.parse(a)
      assert(data.isInstanceOf[Iterable[_]])
      assert(data.asInstanceOf[Iterable[_]].head.isInstanceOf[Map[_, _]])
    }
  }
}
