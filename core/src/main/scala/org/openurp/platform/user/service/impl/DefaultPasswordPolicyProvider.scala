package org.openurp.platform.user.service.impl

import org.beangle.commons.bean.Initializing
import org.beangle.data.dao.EntityDao
import org.beangle.security.authc.{PasswordPolicy, PasswordPolicyProvider}
import org.openurp.platform.user.model.PasswordConfig

class DefaultPasswordPolicyProvider extends PasswordPolicyProvider with Initializing {
  var entityDao: EntityDao = _
  var policy: PasswordPolicy = _

  override def init(): Unit = {
    val configs = entityDao.getAll(classOf[PasswordConfig])
    policy =
      if (configs.size > 0) {
        configs.head
      } else {
        PasswordPolicy.Medium
      }
  }

  override def getPolicy: PasswordPolicy = policy
}
