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
package org.openurp.platform.admin.action.user

import java.io._
import java.time.Instant

import javax.servlet.http.Part
import org.apache.commons.compress.archivers.zip.ZipFile
import org.beangle.commons.activation.MediaTypes
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.io.IOs
import org.beangle.commons.lang.{Strings, SystemInfo, Throwables}
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{mapping, param}
import org.beangle.webmvc.api.view.{Status, Stream, View}
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.user.model.{Avatar, User}

import scala.jdk.javaapi.CollectionConverters.asScala

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
    query.orderBy("user.code")
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

  private def decideContentType(fileName: String): String = {
    MediaTypes.get(Strings.substringAfterLast(fileName, "."), MediaTypes.ApplicationOctetStream).toString
  }

  private def loadAvatar(avatarId: String): Option[Avatar] = {
    val avatar = entityDao.get(classOf[Avatar], avatarId)
    Option(avatar)
  }

  def uploadSetting(): View = {
    forward()
  }

  def upload(): View = {
    getAll("zipfile", classOf[Part]) foreach { zipFile =>
      val tmpFile = new File(SystemInfo.tmpDir + "/photo" + System.currentTimeMillis())
      IOs.copy(zipFile.getInputStream, new FileOutputStream(tmpFile))
      put("total", processZip(tmpFile, "GBK"))
    }
    get("dirInServer") foreach { dirInServer =>
      put("total", processDir(new File(dirInServer)))
    }
    forward()
  }

  def processDir(dir: File): Int = {
    if (!dir.exists()) return 0
    var i = 0
    dir.list() foreach { name =>
      val file = new File(dir.getAbsolutePath + "/" + name)
      if (name.indexOf(".") < 1) {
        logger.warn(name + " without suffix,skipped")
      } else if (file.isDirectory) {
        logger.info(name + " is dir,skipped")
      } else {
        val usercode = Strings.substringBeforeLast(name, ".")
        val users = entityDao.findBy(classOf[User], "code", List(usercode))
        if (users.isEmpty) {
          logger.warn("Cannot find user info of " + usercode)
        } else {
          i += 1
          val user = users.head
          val bos = new ByteArrayOutputStream()
          IOs.copy(new FileInputStream(dir.getAbsolutePath + "/" + name), bos)
          val bytes = bos.toByteArray
          if (bytes.length <= Avatar.MaxSize) {
            saveOrUpdate(user, bytes, name)
          } else {
            logger.warn("Cannot import photo size greate than 500k")
            i -= 1
          }
        }
      }
    }
    i
  }

  def processZip(zipfile: File, encoding: String): Int = {
    val file: ZipFile = if (null == encoding) new ZipFile(zipfile)
    else new ZipFile(zipfile, encoding)
    var i = 0
    try {
      val en = file.getEntries()
      asScala(en) foreach { ze =>
        i = i + 1
        if (!ze.isDirectory) {
          val photoname = if (ze.getName.contains("/")) Strings.substringAfterLast(ze.getName, "/") else ze.getName
          if (photoname.indexOf(".") < 1) {
            logger.warn(photoname + " format is error")
          } else {
            val usercode = Strings.substringBeforeLast(photoname, ".")
            val users = entityDao.findBy(classOf[User], "code", List(usercode))
            if (users.isEmpty) {
              logger.warn("Cannot find user info of " + usercode)
            } else {
              val user = users.head
              val bos = new ByteArrayOutputStream()
              IOs.copy(file.getInputStream(ze), bos)
              val bytes = bos.toByteArray
              if (bytes.length <= Avatar.MaxSize) {
                saveOrUpdate(user, bytes, photoname)
              } else {
                logger.warn("Cannot import photo size greate than 500k")
                i -= 1
              }
            }
          }
        }
      }
      file.close()
    } catch {
      case e: IOException => Throwables.propagate(e)
    }
    i
  }

  def saveOrUpdate(user: User, bytes: Array[Byte], fileName: String): Unit = {
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
    avatar.updatedAt = Instant.now
    entityDao.saveOrUpdate(avatar, user)
  }

}
