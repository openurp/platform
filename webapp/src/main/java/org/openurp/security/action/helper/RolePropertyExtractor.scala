/*
 * Beangle, Agile Development Scaffold and Toolkit
 *
 * Copyright (c) 2005-2014, Beangle Software.
 *
 * Beangle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beangle is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Beangle.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.security.action.helper

import org.beangle.commons.text.i18n.TextResource
import org.beangle.commons.transfer.exporter.DefaultPropertyExtractor
import org.beangle.security.blueprint.Role

 class RolePropertyExtractor extends DefaultPropertyExtractor {

   RolePropertyExtractor() {
    super()
  }

   RolePropertyExtractor(TextResource textResource) {
    super(textResource)
  }

   Object getPropertyValue(Object target, String property) throws Exception {
    Role role = (Role) target
    if ("users".equals(property)) {
      return getPropertyIn(role.getMembers(), "user.name")
    } else {
      return super.getPropertyValue(target, property)
    }
  }

}
