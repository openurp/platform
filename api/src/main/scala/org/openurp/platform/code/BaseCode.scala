package org.openurp.platform.code

import org.beangle.data.model.Coded
import org.beangle.data.model.IntIdEntity
import org.beangle.data.model.Named

trait BaseCode extends IntIdEntity with Named with Coded {

  def enName: String
}