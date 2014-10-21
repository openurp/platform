package org.openurp.platform.resource

import java.{ util => ju }

import org.beangle.commons.bean.{ Factory, PropertyUtils }
import org.beangle.commons.lang.ClassLoaders

import com.mchange.v2.c3p0.ComboPooledDataSource

import javax.naming.spi.ObjectFactory
import javax.sql.DataSource
import org.openurp.platform.app.App

class TomcatDataSourceFactory(val resourceKey: String) extends Factory[DataSource] {
  val clazz = loadClass[ObjectFactory]

  def result: DataSource = {
    if (null == clazz) throw new RuntimeException("ClassNotFoundException org.apache.tomcat.jdbc.pool.DataSourceFactory")
    val parsePoolProperties = clazz.getMethod("parsePoolProperties", classOf[ju.Properties])
    val configuration = parsePoolProperties.invoke(null, App.getResourceConfig(resourceKey).properties)
    val dataSourceClass = ClassLoaders.loadClass("org.apache.tomcat.jdbc.pool.DataSource")
    val constructor = dataSourceClass.getConstructor(ClassLoaders.loadClass("org.apache.tomcat.jdbc.pool.PoolConfiguration"))
    val dataSource = constructor.newInstance(configuration)
    dataSourceClass.getMethod("createPool").invoke(dataSource)
    dataSource.asInstanceOf[DataSource]
  }

  private def loadClass[T]: Class[T] = {
    try {
      ClassLoaders.loadClass("org.apache.tomcat.jdbc.pool.DataSourceFactory").asInstanceOf[Class[T]]
    } catch {
      case _: Throwable => null.asInstanceOf[Class[T]]
    }
  }
}

class C3p0DataSourceFactory(val resourceKey: String) extends Factory[DataSource] {

  def result: DataSource = {
    val cpds = new ComboPooledDataSource(true)
    val properties = App.getResourceConfig(resourceKey).properties
    if (properties.containsKey("maxActive")) properties.put("maxPoolSize", properties.remove("maxActive"))
    if (properties.containsKey("url")) properties.put("jdbcUrl", properties.remove("url"))
    if (properties.containsKey("driverClassName")) properties.put("driverClass", properties.remove("driverClassName"))
    if (properties.containsKey("username")) properties.put("user", properties.remove("username"))

    val iter = properties.entrySet.iterator
    while (iter.hasNext) {
      val entry = iter.next()
      PropertyUtils.copyProperty(cpds, entry.getKey.toString, entry.getValue)
    }
    cpds
  }

}