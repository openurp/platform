package org.openurp.platform.api.web

import org.beangle.commons.cdi.bind.AbstractBindModule
import org.openurp.platform.api.web.tag.UrpTagLibrary

class DefaultModule extends AbstractBindModule{
  
  protected override def binding(){
    bind("mvc.Taglibrary.urp",classOf[UrpTagLibrary])
  }
}