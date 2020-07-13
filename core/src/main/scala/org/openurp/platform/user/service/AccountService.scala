package org.openurp.platform.user.service

import org.beangle.security.authc.{CredentialAge, DefaultAccount}
import org.openurp.platform.user.model.{Account, User}

trait AccountService {

  def get(code: String): Option[Account]

  def getAuthAccount(code: String): Option[DefaultAccount]

  def enable(manager: User, accountIds: Iterable[Long], enabled: Boolean): Int

  def getActivePassword(code: String): Option[String]

  def getPasswordAge(code: String): Option[CredentialAge]

  def updatePassword(code: String, rawPassword: String): Unit

  def createAccount(user: User, account: Account): Unit
}
