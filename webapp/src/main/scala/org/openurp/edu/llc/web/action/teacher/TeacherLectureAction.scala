package org.openurp.edu.llc.web.action.teacher

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.llc.model.Lecture
import org.beangle.data.dao.OqlBuilder
import org.beangle.commons.collection.Order
import org.beangle.security.Securities
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.base.model.Project
import org.openurp.base.model.Department
import org.openurp.edu.base.model.Semester
import java.time.LocalDate
import org.beangle.webmvc.api.view.View

class TeacherLectureAction extends RestfulAction[Lecture] {

  override protected def indexSetting(): Unit = {
    put("projects", entityDao.getAll(classOf[Project]))
    put("semesters", entityDao.getAll(classOf[Semester]))
    put("currentSemester", getCurSemester())
    super.indexSetting()
  }

  def getCurSemester(): Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
    builder.where("semester.beginOn <= :date and semester.endOn >= :date", LocalDate.now())
    val semesters = entityDao.search(builder)
    semesters(0)
  }

  override protected def getQueryBuilder(): OqlBuilder[Lecture] = {
    val status = get("status")
    val user = Securities.user
    val teacherBuilder = OqlBuilder.from(classOf[Teacher], "teacher")
    teacherBuilder.where("teacher.user.code =:code ", user)
    val teachers = entityDao.search(teacherBuilder)
    val builder = OqlBuilder.from(classOf[Lecture], "lecture")
    builder.where("lecture.teacher =:teacher", teachers(0))
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
  
  
  def audiences(): View = {
    val lectureId = longId("lecture")
    val lecture = entityDao.get(classOf[Lecture], lectureId)
    put("lecture", lecture)
    put("audiences", lecture.audiences)
    forward()
  }


}