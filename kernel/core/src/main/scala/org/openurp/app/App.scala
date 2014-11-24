package org.openurp.app

import org.beangle.data.model.{ IntIdEntity, Named }

trait App extends IntIdEntity with Named {

  def url: String

  def title: String
}