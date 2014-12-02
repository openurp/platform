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
package org.openurp.kernel.security.action.helper

import java.util.Collection

import org.beangle.commons.text.i18n.TextResource
import org.beangle.commons.transfer.exporter.DefaultPropertyExtractor
import org.beangle.security.blueprint.User

 class UserPropertyExtractor extends DefaultPropertyExtractor {

   UserPropertyExtractor() {
    super()
  }

   UserPropertyExtractor(TextResource textResource) {
    super(textResource)
  }

   Object getPropertyValue(Object target, String property) throws Exception {
    User user = (User) target
    if ("enabled".equals(property)) {
      boolean enabled = user.isEnabled()
      if (enabled) return getText("action.activate")
      else return getText("action.freeze")
    }
    return super.getPropertyValue(target, property)
  }

   static final StringBuilder getUserNames(Collection<User> users) {
    StringBuilder sb = new StringBuilder()
    for (final User user : users) {
      sb.append(user.getFullname())
      sb.append('(').append(user.code).append(')').append(' ')
    }
    if (sb.length() > 0) {
      sb.deleteCharAt(sb.length() - 1)
    }
    return sb
  }
}
