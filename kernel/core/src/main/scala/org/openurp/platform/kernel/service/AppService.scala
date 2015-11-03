package org.openurp.platform.kernel.service

import org.openurp.platform.kernel.model.App
/**
 * @author chaostone
 */
trait AppService {
  def getApp(name: String, secret: String): Option[App]
}