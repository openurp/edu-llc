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
package org.openurp.edu.llc.web.action.admin

import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Classroom
import org.openurp.edu.llc.model.Lecture
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.lang.Strings
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Semester
import java.time.LocalDate
import org.beangle.commons.collection.Order
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Classroom
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Teacher

class LectureAction extends RestfulAction[Lecture] {

  override protected def indexSetting(): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("projects", entityDao.getAll(classOf[Project]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("currentSemester", getCurSemester())
    super.indexSetting()
  }

  override protected def getQueryBuilder(): OqlBuilder[Lecture] = {
    val status = get("status")
    val builder = OqlBuilder.from(classOf[Lecture], "lecture")
    status.foreach(
      a => a match {
        case "0" => {
          builder.where("size(lecture.audiences)<lecture.capacity")
        }
        case "1" => {
          builder.where("size(lecture.audiences)=lecture.capacity")
        }
        case "2" =>
      })
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

  override protected def editSetting(entity: Lecture): Unit = {
    put("departments", entityDao.getAll(classOf[Department]))
    put("rooms", entityDao.getAll(classOf[Classroom]))
    put("projects", entityDao.getAll(classOf[Project]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    super.editSetting(entity)
  }

  def getCurSemester(): Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
    builder.where("semester.beginOn <= :date and semester.endOn >= :date", LocalDate.now())
    val semesters = entityDao.search(builder)
    semesters(0)
  }

  def room(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Classroom], "room")
    populateConditions(query)

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(room.name like :name or room.code like :code)", '%' + codeOrName + '%',
        '%' + codeOrName + '%')
    }
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    val rooms = entityDao.search(query)
    put("rooms", entityDao.search(query))
    put("pageLimit", pageLimit)
    forward()
  }

  def teacher(): View = {
    val codeOrName = get("term").orNull
    val query = OqlBuilder.from(classOf[Teacher], "teacher")
    query.where("teacher.project.id=:projectId", getInt("project").get)
    populateConditions(query);

    if (Strings.isNotEmpty(codeOrName)) {
      query.where("(teacher.user.name like :name or teacher.user.code like :code)", '%' + codeOrName + '%',
        '%' + codeOrName + '%');
    }
    val now = LocalDate.now
    query.where(":now1 >= teacher.beginOn and (teacher.endOn is null or :now2 <= teacher.endOn)", now, now)
      .orderBy("teacher.user.name")
    val pageLimit = getPageLimit
    query.limit(pageLimit);
    val a = entityDao.search(query)
    put("teachers", entityDao.search(query));
    put("pageLimit", pageLimit);
    forward()
  }

  override protected def saveAndRedirect(lecture: Lecture): View = {
    val projectId = get("project").get.toInt
    val project = entityDao.get(classOf[Project], projectId)
    lecture.project = project
    val date = get[LocalDate]("lecture.date", classOf[LocalDate])
    val builder = OqlBuilder.from(classOf[Semester], "semester")
    builder.where("semester.beginOn <= :date and semester.endOn >= :date", date.get)
    val semesters = entityDao.search(builder)
    lecture.semester = semesters(0)
    lecture.actual = lecture.audiences.size
    super.saveAndRedirect(lecture)
  }

  def audiences(): View = {
    val lectureId = longId("lecture")
    val lecture = entityDao.get(classOf[Lecture], lectureId)
    put("lecture", lecture)
    put("audiences", lecture.audiences)
    forward()
  }

}
