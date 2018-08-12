package org.openurp.platform.ws.user

import java.io.{ ByteArrayInputStream, File, FileInputStream }

import org.beangle.data.dao.EntityDao
import org.beangle.webmvc.api.action.ActionSupport
import org.beangle.webmvc.api.annotation.{ mapping, param }
import org.beangle.webmvc.api.view.{ Stream, View }
import org.openurp.platform.user.model.Avatar

class AvatarWS(entityDao: EntityDao) extends ActionSupport {

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

}