package org.openurp.platform.api.cas

import org.beangle.security.session.profile.ProfileProvider
import org.beangle.security.session.profile.SessionProfile
import org.beangle.security.authc.Account
import org.beangle.security.session.profile.DefaultSessionProfile

/**
 * @author chaostone
 */
class RemoteProfileProvider extends ProfileProvider {
//  def getProfile(auth: Account): SessionProfile = {
//    null
//  }
//
//  def getProfiles(): Iterable[SessionProfile] = {
//    null
//  }
   private val defaultProfile = DefaultSessionProfile

  def getProfile(auth: Account): SessionProfile = {
    defaultProfile
  }
  
  def getProfiles(): Iterable[SessionProfile] = {
    List(defaultProfile)
  }
}