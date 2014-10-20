package org.openurp.platform.resource

import java.{ util => ju }

import org.beangle.commons.bean.{Factory, PropertyUtils}
import org.beangle.commons.lang.ClassLoaders

import com.mchange.v2.c3p0.ComboPooledDataSource

import javax.naming.spi.ObjectFactory
import javax.sql.DataSource

class TomcatDataSourceFactory(val configurer: Configurer) extends Factory[DataSource] {
  val clazz = loadClass[ObjectFactory]

  def result: DataSource = {
    if (null == clazz) throw new RuntimeException("ClassNotFoundException org.apache.tomcat.jdbc.pool.DataSourceFactory")
    val parsePoolProperties = clazz.getMethod("parsePoolProperties", classOf[ju.Properties])
    val configuration = parsePoolProperties.invoke(null, configurer.properties)
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

class C3p0DataSourceFactory(val configurer: Configurer) extends Factory[DataSource] {

  def result: DataSource = {
    val cpds = new ComboPooledDataSource(true)
    val properties = configurer.properties
    if (properties.contains("maxActive")) properties.put("maxPoolSize", properties.remove("maxActive"))
    if (properties.contains("url")) properties.put("jdbcUrl", properties.remove("url"))
    val iter = properties.entrySet.iterator
    while (iter.hasNext) {
      val entry = iter.next()
      PropertyUtils.copyProperty(entry, entry.getKey.toString, entry.getValue)
    }
    cpds
  }

}