/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2005, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openurp.platform.web.action.user

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream, File, FileInputStream, FileOutputStream, IOException }
import java.time.LocalDateTime

import org.apache.commons.compress.archivers.zip.ZipFile
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{ Strings, SystemInfo, Throwables }
import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.user.model.{ Avatar, User }

import javax.servlet.http.Part
import org.beangle.commons.activation.MimeTypes
import org.beangle.webmvc.api.view.Status

class AvatarAction extends ActionSupport {

  var entityDao: EntityDao = _

  def index(): View = {
    val query = OqlBuilder.from(classOf[User], "user")
    QueryHelper.populateConditions(query)
    get("user") foreach { u =>
      query.where("user.code like :u or user.name like :u", "%" + u + "%")
    }
    query.where("user.avatarId is not null")
    query.limit(QueryHelper.pageLimit)
    put("users", entityDao.search(query))
    forward()
  }

  def view(): View = {
    forward()
  }

  @mapping("{userId}")
  def info(@param("userId") userId: String): View = {
    val user = entityDao.get(classOf[User], userId.toLong)
    user.avatarId match {
      case None => Status.NotFound
      case Some(avatarId) =>
        loadAvatar(avatarId) match {
          case None => Status.NotFound
          case Some(avatar) =>
            Stream(new ByteArrayInputStream(avatar.image), decideContentType(avatar.fileName), avatar.fileName)
        }
    }
  }

  private def loadImage(avatarId: String): Array[Byte] = {
    val avatar = entityDao.get(classOf[Avatar], avatarId)
    if (null == avatar) {
      val nfile = new File(this.getClass().getClassLoader().getResource("DefaultPhoto.gif").getFile())
      val in = new FileInputStream(nfile)
      new Array[Byte](nfile.length.toInt)
    } else {
      avatar.image
    }
  }

  private def decideContentType(fileName: String): String = {
    MimeTypes.getMimeType(Strings.substringAfterLast(fileName, "."), MimeTypes.ApplicationOctetStream).toString
  }

  private def loadAvatar(avatarId: String): Option[Avatar] = {
    val avatar = entityDao.get(classOf[Avatar], avatarId)
    if (null == avatar) {
      None
    } else {
      Some(avatar)
    }
  }

  def uploadSetting(): View = {
    forward()
  }

  def upload(): View = {
    getAll("zipfile", classOf[Part]) foreach { zipFile =>
      val tmpFile = new File(SystemInfo.tmpDir + "/photo" + System.currentTimeMillis())
      IOs.copy(zipFile.getInputStream, new FileOutputStream(tmpFile))
      put("fileNames", unzip(tmpFile, "GBK"))
    }
    forward()
  }

  def unzip(zipfile: File, encoding: String): List[String] = {
    val file: ZipFile = if (null == encoding) new ZipFile(zipfile)
    else new ZipFile(zipfile, encoding)
    val fileNames = new collection.mutable.ListBuffer[String]
    try {

      val en = file.getEntries()
      var i = 0
      import scala.collection.JavaConversions._
      en.foreach { ze =>
        fileNames += ze.getName()
        println(ze.getName())
        i = i + 1
        if (!ze.isDirectory()) {
          val photoname = if (ze.getName().contains("/")) Strings.substringAfterLast(ze.getName(), "/") else ze.getName()

          if (photoname.indexOf(".") < 1) {
            logger.warn(photoname + " format is error")
          } else {
            val usercode = Strings.substringBeforeLast(photoname, ".")
            val users = entityDao.findBy(classOf[User], "code", List(usercode))
            if (users.isEmpty) {
              println("Cannot find user info of " + usercode);
            } else {
              val user = users.head
              val bos = new ByteArrayOutputStream()
              IOs.copy(file.getInputStream(ze), bos)
              val bytes = bos.toByteArray()
              saveOrUpdate(user, bytes, photoname)
            }
          }
        } else {
          println("dir:" + ze.getName())
        }
      }
      put("total", i)
      file.close()
    } catch {
      case e: IOException => Throwables.propagate(e)
    }
    fileNames.toList
  }

  def saveOrUpdate(user: User, bytes: Array[Byte], fileName: String) {
    val usercode = user.code
    val query = OqlBuilder.from(classOf[Avatar], "avatar")
    query.where("avatar.user.id=:userId", user.id)
    val avatars = entityDao.search(query)

    var avatar: Avatar = null
    if (avatars.isEmpty) {
      avatar = new Avatar(user, bytes)
      avatar.id = Digests.md5Hex(usercode)
    } else {
      avatar = avatars.head
      avatar.image = bytes
    }
    user.avatarId = Some(avatar.id)
    avatar.fileName = fileName
    avatar.updatedAt = LocalDateTime.now
    entityDao.saveOrUpdate(avatar, user)
  }

}
