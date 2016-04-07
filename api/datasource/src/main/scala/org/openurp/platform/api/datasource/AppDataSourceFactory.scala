package org.openurp.platform.api.datasource

import org.beangle.data.jdbc.ds.DataSourceFactory
import org.openurp.platform.api.app.UrpApp
import org.openurp.platform.api.util.AesEncryptor
import org.beangle.commons.io.IOs
import java.io.FileInputStream
import org.openurp.platform.api.Urp

class AppDataSourceFactory extends DataSourceFactory {

  if (UrpApp.getUrpAppFile exists (file => IOs.readString(new FileInputStream(file)).contains("</datasource>"))) {
    this.url = UrpApp.getUrpAppFile.get.getCanonicalPath
  } else {
    this.url = getDatasourceUrl(if (null == name) "default" else name)
  }

  protected override def postInit(): Unit = {
    if (this.password != null) this.password = new AesEncryptor(UrpApp.secret).decrypt(password)
  }

  private def getDatasourceUrl(resourceKey: String): String = {
    Urp.platformBase + "/config/datasources/" + UrpApp.name + "/" + resourceKey + ".json?secret=" + UrpApp.secret
  }
}