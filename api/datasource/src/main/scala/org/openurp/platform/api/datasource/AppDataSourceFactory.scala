package org.openurp.platform.api.datasource

import org.beangle.data.jdbc.ds.DataSourceFactory
import org.openurp.platform.api.app.AppConfig
import org.openurp.platform.api.util.AesEncryptor
import org.beangle.commons.io.IOs
import java.io.FileInputStream

class AppDataSourceFactory extends DataSourceFactory {

  if (AppConfig.getAppConfigFile exists (file => IOs.readString(new FileInputStream(file)).contains("</datasource>"))) {
    this.url = AppConfig.getAppConfigFile.get.getCanonicalPath
  } else {
    this.url = AppConfig.getDatasourceUrl(if (null == name) "default" else name)
  }

  protected override def postInit(): Unit = {
    if (this.password != null) this.password = new AesEncryptor(AppConfig.secret).decrypt(password)
  }

}