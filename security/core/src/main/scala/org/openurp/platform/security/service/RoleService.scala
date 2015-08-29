package org.openurp.platform.security.service

import org.openurp.platform.security.model.Role


trait RoleService {

  def get(id: Integer): Role

}