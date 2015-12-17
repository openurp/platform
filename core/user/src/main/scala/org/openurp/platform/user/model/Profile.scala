package org.openurp.platform.user.model

import org.beangle.commons.lang.Strings

object Property {
  val All = "*"
}

/**
 * @author chaostone
 */
trait Profile {

  def properties: collection.mutable.Map[Dimension, String]

  def setProperty(field: Dimension, value: String): Unit = {
    val property = getProperty(field)
    if (Strings.isNotBlank(value))
      properties.put(field, value)
    else properties -= field
  }

  def getProperty(field: Dimension): Option[String] = {
    properties.get(field)
  }

  def getProperty(name: String): Option[String] = {
    properties.keys.find(k => k.name == name) match {
      case Some(p) => properties.get(p)
      case None => None
    }
  }

  def matches(other: Profile): Boolean = {
    if (other.properties.isEmpty) return true
    other.properties exists {
      case (field, target) =>
        val source = getProperty(field).getOrElse("")
        (source != Property.All) && ((target == Property.All) || (Strings.split(target, ",").toSet -- Strings.split(source, ",")).isEmpty)
    }
  }
}