package org.openurp.platform.web.tag

import org.beangle.webmvc.dispatch.ActionUriRender
import org.beangle.webmvc.view.TagLibrary
import org.beangle.webmvc.view.impl.IndexableIdGenerator
import org.beangle.webmvc.view.tag.{ ComponentContext, TemplateEngine }

import javax.servlet.http.{ HttpServletRequest, HttpServletResponse }

class UrpTagLibrary extends TagLibrary {

  var uriRender: ActionUriRender = _
  var templateEngine: TemplateEngine = _

  def getModels(req: HttpServletRequest, res: HttpServletResponse): AnyRef = {
    val queryString = req.getQueryString
    val fullpath = if (null == queryString) req.getRequestURI() else req.getRequestURI() + queryString
    val idGenerator = new IndexableIdGenerator(String.valueOf(Math.abs(fullpath.hashCode)))

    val componentContext = new ComponentContext(uriRender, idGenerator, templateEngine)
    new UrpModels(componentContext, req)
  }

}