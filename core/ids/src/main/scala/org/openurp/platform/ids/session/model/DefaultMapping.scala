package org.openurp.platform.ids.session.model

import org.beangle.data.orm.MappingModule
import org.beangle.data.orm.IdGenerator

class DefaultMapping extends MappingModule {

  override def binding() {
    defaultIdGenerator(IdGenerator.Assigned)
    bind[SessionInfo]
  }
}