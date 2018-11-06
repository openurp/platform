/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.admin.action.config

import java.security.MessageDigest
import java.util.Arrays
import org.beangle.commons.codec.binary.Hex
import org.beangle.webmvc.api.annotation.ignore
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.platform.config.model.{ App, DataSource }
import org.openurp.platform.config.service.DbService
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.openurp.platform.config.model.Domain

class AppAction(dbService: DbService) extends RestfulAction[App] {

  override def simpleEntityName = "app"

  def datasource(): View = {
    put("dataSources", dbService.list())
    forward()
  }

  protected override def editSetting(entity: App): Unit = {
    put("domains", entityDao.getAll(classOf[Domain]))
  }

  @ignore
  override protected def saveAndRedirect(app: App): View = {
    try {
      val sets = app.datasources.asInstanceOf[collection.mutable.Buffer[DataSource]]
      val processed = new collection.mutable.HashSet[Integer]
      val removed = new collection.mutable.HashSet[DataSource]
      val ids = getAll("ds", classOf[Integer]).toSet
      sets foreach { ds =>
        if (ids.contains(ds.db.id)) {
          processed += ds.db.id
          val originPassword = ds.password
          populate(ds, "ds" + ds.db.id)
          ds.password =
            if (null == ds.password) originPassword
            else encrypt(ds.password, app.secret)
        } else {
          removed += ds
        }
      }
      sets --= removed
      for (id <- ids if !processed.contains(id)) {
        val set = populate(classOf[DataSource], "ds" + id)
        set.app = app
        set.password = encrypt(set.password, app.secret)
        sets += set
      }

      saveOrUpdate(app)
      redirect("search", "info.save.success")
    } catch {
      case e: Exception => {
        logger.info("saveAndForwad failure", e)
        redirect("search", "info.save.failure")
      }
    }
  }

  private def encrypt(plainText: String, secretKey: String): String = {
    var key = secretKey.getBytes("UTF-8")
    val sha = MessageDigest.getInstance("SHA-1")
    key = sha.digest(key)
    key = Arrays.copyOf(key, 16) // use only the first 128 bit
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"))
    Hex.encode(cipher.doFinal(plainText.getBytes("UTF-8")))
  }
}
