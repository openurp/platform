package org.openurp.app.action

import org.beangle.commons.inject.bind.AbstractBindModule
import org.openurp.app.service.DataSourceServiceImpl
import org.openurp.app.service.DataSourceServiceImpl
import org.openurp.app.action.datasource.ConfigAction

class DefaultModule extends AbstractBindModule {

  protected override def binding() {
    bind(classOf[ConfigAction], classOf[AppAction])
    bind(classOf[DataSourceServiceImpl])
  }
}