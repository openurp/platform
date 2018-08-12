package org.openurp.platform.web.action.user

import java.io.{ ByteArrayInputStream, File, FileInputStream }

import org.beangle.data.dao.{ EntityDao, OqlBuilder }
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.{ Stream, View }
import org.beangle.webmvc.entity.helper.QueryHelper
import org.openurp.platform.user.model.{ Avatar, User }
import java.io.FileOutputStream
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.io.IOs
import java.io.IOException
import java.sql.PreparedStatement
import org.beangle.commons.lang.SystemInfo
import javax.servlet.http.Part
import org.beangle.commons.lang.Throwables
import org.apache.commons.compress.archivers.zip.ZipFile
import java.io.ByteArrayOutputStream
import org.beangle.commons.lang.Strings
import org.apache.commons.compress.utils.IOUtils

class AvatarAction extends ActionSupport {

  var entityDao: EntityDao = _

  def index(): View = {
    forward()
  }

  def view(): View = {
    val code = get("code").getOrElse("") + "%"
    val query = OqlBuilder.from(classOf[User], "user")
    QueryHelper.populateConditions(query)
    query.where("user.avatarId is not null")
    put("users", entityDao.search(query))
    forward()
  }

  @mapping("{avatarId}")
  def info(@param("avatarId") avatarId: String): View = {
    Stream(new ByteArrayInputStream(loadImage(avatarId)), "image/jpg", avatarId)
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
              saveOrUpdate(user, bytes)
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

  def saveOrUpdate(user: User, bytes: Array[Byte]) {
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
    entityDao.saveOrUpdate(avatar)
  }

}