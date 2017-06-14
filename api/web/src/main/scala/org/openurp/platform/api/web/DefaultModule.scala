package org.openurp.platform.api.web

import org.beangle.cdi.bind.BindModule
import org.openurp.platform.api.web.tag.UrpTagLibrary

class DefaultModule extends BindModule{
  
  protected override def binding(){
    bind("mvc.Taglibrary.urp",classOf[UrpTagLibrary])
  }
}