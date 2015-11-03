package org.openurp.platform.api.util

import org.junit.runner.RunWith
import org.scalatest.Matchers
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.beangle.commons.collection.Properties

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
      assert(result.asInstanceOf[Properties]("authorities").isInstanceOf[collection.Seq[_]])
    }
  }
}