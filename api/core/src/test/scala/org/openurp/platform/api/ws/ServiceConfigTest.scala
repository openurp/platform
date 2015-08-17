package org.openurp.platform.api.ws

import org.junit.runner.RunWith
import org.scalatest.{ FunSpec, Matchers }
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ServiceConfigTest extends FunSpec with Matchers {
  describe("ServiceConfig") {
    it("getBase") {
      System.setProperty("openurp.base", "urp.org")
      assert(ServiceConfig.dsBase == "http://data.urp.org")
      assert(ServiceConfig.wsBase == "http://service.urp.org")
    }

  }

}