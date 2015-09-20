package org.openurp.platform.api.web.tag

import org.beangle.webmvc.dispatch.ActionUriRender
import org.beangle.webmvc.view.TagLibrary
import org.beangle.webmvc.view.impl.IndexableIdGenerator
import org.beangle.webmvc.view.tag.{ ComponentContext, TemplateEngine }
import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }
import org.beangle.webmvc.view.tag.AbstractTagLibrary

class UrpTagLibrary extends AbstractTagLibrary {

  def getModels(req: HttpServletRequest, res: HttpServletResponse): AnyRef = {
    new UrpModels(buildComponentContext(req), req)
  }

}