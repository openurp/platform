package org.openurp.platform.user.model

import org.beangle.data.model.StringId
import java.time.LocalDate

class Avatar extends StringId {

  var user: User = _

  var updatedAt: LocalDate = _

  var image: Array[Byte] = _

  def this(user: User, image: Array[Byte]) {
    this()
    this.user = user
    this.image = image
  }
}