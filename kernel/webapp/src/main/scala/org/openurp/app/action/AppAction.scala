package org.openurp.app.action

import org.beangle.commons.codec.binary.Des
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.app.service.DataSourceService
import java.security.MessageDigest
import org.beangle.commons.codec.binary.Hex
import javax.crypto.Cipher
import java.util.Arrays
import javax.crypto.spec.SecretKeySpec
import org.openurp.app.resource.model.DataSourceBean
import org.openurp.app.model.AppBean

class AppAction(dataSourceService: DataSourceService) extends RestfulAction[AppBean] {

  override def shortName = "app"

  def datasource(): String = {
    put("dataSources", dataSourceService.findDataSource())
    forward()
  }

  @ignore
  override protected def saveAndRedirect(app: AppBean): View = {
    try {
      val sets = app.datasources.asInstanceOf[collection.mutable.Buffer[DataSourceBean]]
      val processed = new collection.mutable.HashSet[Integer]
      val removed = new collection.mutable.HashSet[DataSourceBean]
      val ids = getAll("ds", classOf[Integer]).toSet
      sets foreach { ds =>
        if (ids.contains(ds.config.id)) {
          processed += ds.config.id
          val originPassword = ds.password
          populate(ds, "ds" + ds.config.id)
          ds.password =
            if (null == ds.password) originPassword
            else encrypt(ds.password)
        } else {
          removed += ds
        }
      }

      for (id <- ids if !processed.contains(id)) {
        val set = populate(classOf[DataSourceBean], "ds" + id)
        set.app = app
        set.password = encrypt(set.password)
        sets += set
      }
      saveOrUpdate(app)
      redirect("search", "info.save.success")
    } catch {
      case e: Exception => {
        info("saveAndForwad failure", e)
        redirect("search", "info.save.failure")
      }
    }
  }

  private def encrypt(plainText: String): String = {
    val secretKey = "changeit"
    var key = secretKey.getBytes("UTF-8")
    val sha = MessageDigest.getInstance("SHA-1")
    key = sha.digest(key)
    key = Arrays.copyOf(key, 16) // use only the first 128 bit
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"))
    Hex.encode(cipher.doFinal(plainText.getBytes("UTF-8")))
  }
}