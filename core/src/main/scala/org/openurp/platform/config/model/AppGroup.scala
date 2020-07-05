package org.openurp.platform.config.model

import org.beangle.data.model.IntId
import org.beangle.data.model.pojo.Named

class AppGroup extends IntId with Named with Ordered[AppGroup] {
  var indexno: String = _
  var title: String = _
  var domain: Domain = _

  override def compare(that: AppGroup): Int = {
    this.indexno.compareTo(that.indexno)
  }
}
