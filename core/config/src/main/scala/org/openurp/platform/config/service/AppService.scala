package org.openurp.platform.config.service

import org.openurp.platform.config.model.App
/**
 * @author chaostone
 */
trait AppService {
  def getApp(name: String, secret: String): Option[App]

  def getWebapps(): Seq[App]
}
