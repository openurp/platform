package org.openurp.platform.api.app

import org.junit.runner.RunWith
import org.scalatest.{ FunSpec, Matchers }
import org.scalatest.junit.JUnitRunner
import org.openurp.platform.api.Urp

/**
 * @author chaostone
 */
@RunWith(classOf[JUnitRunner])
class UrpAppTest extends FunSpec with Matchers {
  describe("UrpApp") {
    it("init") {
      assert(UrpApp.name == "platform-testwebapp");
      assert(UrpApp.path == "/platform/testwebapp");
    }
  }
}
