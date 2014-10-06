package org.openurp.platform.server

import org.apache.catalina.startup.Tomcat
import java.io.File
import org.apache.catalina.Context
import org.beangle.commons.web.init.BootstrapListener
import org.apache.catalina.startup.ContextConfig
import org.apache.tomcat.util.descriptor.web.ContextResource

object Startup {
  def main(args: Array[String]): Unit = {
    val tomcat = new Tomcat();
    tomcat.setPort(8080);
    val base = new File(System.getProperty("java.io.tmpdir"))
    tomcat.enableNaming()
    val resource = new ContextResource();
    resource.setName("jdbc/dataSource");
    resource.setAuth("Container");
    resource.setType("javax.sql.DataSource");
    resource.setScope("Sharable");
    resource.setProperty("driverClassName", "org.postgresql.Driver");
    resource.setProperty("username", "postgres");
    resource.setProperty("password", "");
    resource.setProperty("url", "jdbc:postgresql://localhost:5432/postgres");
    val rootCtx = tomcat.addContext("/urp", base.getAbsolutePath());
    rootCtx.getNamingResources().addResource(resource)
    rootCtx.addApplicationListener(classOf[BootstrapListener].getName)

    tomcat.start()
    tomcat.getServer().await()
  }
}