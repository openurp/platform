package org.openurp.platform.api.datasource

import org.beangle.data.jdbc.ds.DataSourceFactory
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.api.util.AesEncryptor

class AppDataSourceFactory extends DataSourceFactory {

  AppConfig.getFile("/resources.xml").orElse(AppConfig.getFile("/resources.json")) match {
    case Some(file) => this.url = file.getCanonicalPath
    case None => this.url = AppConfig.getDatasourceUrl(if (null == name) "default" else name)
  }

  protected override def postInit(): Unit = {
    if (this.password != null) {
      this.password = new AesEncryptor(AppConfig.secret).decrypt(password)
    }
  }

}