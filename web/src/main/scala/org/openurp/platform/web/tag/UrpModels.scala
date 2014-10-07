package org.openurp.platform.web.tag

import java.{ util => ju }

import org.beangle.commons.bean.PropertyUtils
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ ClassLoaders, Strings }
import org.beangle.webmvc.view.tag.{ AbstractModels, ComponentContext }
import org.beangle.webmvc.view.tag.components.ClosingUIBean
import org.beangle.webmvc.view.tag.freemarker.TagModel

import javax.servlet.http.HttpServletRequest

object UrpService {
  var config = readConfig()
  def readConfig(): Map[String, String] = {
    val configs = ClassLoaders.getResources("urp_service.properties")
    if (configs.isEmpty) {
      throw new RuntimeException("Cannot find urp_service.properties")
    } else {
      IOs.readJavaProperties(configs.head)
    }
  }
}

class UrpModels(context: ComponentContext, request: HttpServletRequest) extends AbstractModels(context, request) {
  def codeSelect: TagModel = get(classOf[CodeSelect])
}

class CodeSelect(context: ComponentContext) extends ClosingUIBean(context) {
  var name: String = _
  var className: String = _
  var keyName: String = "id"
  var valueName: String = "name"
  var value: Object = _
  var label: String = _
  var empty: String = _
  var uri: String = _
  var code: String = _

  override def evaluateParams() {
    uri = UrpService.config("base") + code + ".json"
    if (null == this.id) generateIdIfEmpty()
    if (null == value) value = requestParameter(name)
    if ((value.isInstanceOf[String]) && Strings.isEmpty(value.asInstanceOf[String])) value = null
  }

  def isSelected(obj: Object): Boolean = {
    if (null == value) return false
    else try {
      var nobj = obj
      if (obj.isInstanceOf[Tuple2[_, _]]) nobj = obj.asInstanceOf[Tuple2[Object, _]]._1
      else if (obj.isInstanceOf[ju.Map.Entry[_, _]]) nobj = obj.asInstanceOf[ju.Map.Entry[Object, _]].getKey()
      val rs = value.equals(nobj) || value.equals(PropertyUtils.getProperty(nobj, keyName))
      return rs || value.toString().equals(nobj.toString()) ||
        value.toString().equals(String.valueOf(PropertyUtils.getProperty(nobj, keyName).toString))
    } catch {
      case e: Exception =>
        e.printStackTrace()
        return false
    }
  }
}