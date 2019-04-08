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
package org.openurp.edu.llc.web.action.student

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.llc.model.Lecture
import org.beangle.webmvc.api.view.View
import org.beangle.security.Securities
import org.openurp.edu.llc.model.Audience
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Student
import java.time.Instant
import java.time.LocalDate
import org.openurp.edu.base.model.Student
import org.openurp.edu.llc.model.Lecture

class LectureReservationAction extends RestfulAction[Audience] {

  override protected def indexSetting(): Unit = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)
    val audiences = entityDao.findBy(classOf[Audience], "std", students)
    val chooseLectures = audiences.map(_.lecture)

    put("chooseLectures", chooseLectures)
    val lectureBuilder = OqlBuilder.from(classOf[Lecture], "lecture")
    lectureBuilder.where("lecture.date >:now", LocalDate.now())
    put("lectures", entityDao.search(lectureBuilder))
  }

  def lectures(): View = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)
    val audiences = entityDao.findBy(classOf[Audience], "std", students)
    val chooseLectures = audiences.map(_.lecture)

    put("chooseLectures", chooseLectures)
    val lectureBuilder = OqlBuilder.from(classOf[Lecture], "lecture")
    lectureBuilder.where("lecture.date >:now", LocalDate.now())
    put("lectures", entityDao.search(lectureBuilder))
    forward()
  }

  protected def choose(): View = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)

    val lecture = entityDao.find(classOf[Lecture], longId("lecture")).get

    val audienceBuilder = OqlBuilder.from(classOf[Audience], "audience")
    audienceBuilder.where("audience.std =:std", students(0))
    val audiences = entityDao.search(audienceBuilder)
    val chooseLectures = audiences.map(_.lecture)

    val conflict = chooseLectures.exists(x => x.date == lecture.date && lecture.beginAt < x.endAt && x.beginAt < lecture.endAt)
    if (lecture.audiences.size < lecture.capacity) {
      if (!conflict) {
        val audience = populateEntity()
        audience.lecture = lecture
        audience.std = students(0)
        audience.updatedAt = Instant.now()
        audience.lecture.actual = audience.lecture.audiences.size + 1
        saveOrUpdate(audience.lecture)
        entityDao.saveOrUpdate(audience)
        redirect("index", "选择成功")
      } else {
        redirect("index", "与已选课堂活动时间冲突")
      }
    } else {
      redirect("index", "所选课堂活动人数已满")
    }
  }

  protected def unChoose(): View = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)

    val lecture = entityDao.find(classOf[Lecture], longId("lecture")).get
    val audienceBuilder = OqlBuilder.from(classOf[Audience], "audience")
    audienceBuilder.where("audience.std =:std", students(0))
    audienceBuilder.where("audience.lecture =:lecture", lecture)
    val audiences = entityDao.search(audienceBuilder)
    entityDao.remove(audiences)
    lecture.actual = lecture.audiences.size
    entityDao.saveOrUpdate(lecture)

    redirect("index")
  }

  override protected def saveAndRedirect(audience: Audience): View = {
    val a = audience.lecture.actual
    val b = audience.lecture.audiences.size
    audience.lecture.actual = audience.lecture.audiences.size
    saveOrUpdate(audience.lecture)
    super.saveAndRedirect(audience)
  }

}
