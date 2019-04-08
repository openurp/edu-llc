package org.openurp.edu.llc.web.action.teacher

import java.time.LocalDate

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.security.Securities
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.base.model.Project
import org.openurp.edu.base.model.Semester
import org.openurp.edu.base.model.Teacher
import org.openurp.edu.llc.model.TutorialActivity
import org.beangle.webmvc.api.view.View

class TeacherTutorialAction extends RestfulAction[TutorialActivity] {

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

  override protected def getQueryBuilder(): OqlBuilder[TutorialActivity] = {
    val status = get("status")
    val user = Securities.user
    val teacherBuilder = OqlBuilder.from(classOf[Teacher], "teacher")
    teacherBuilder.where("teacher.user.code =:code ", user)
    val teachers = entityDao.search(teacherBuilder)
    val builder = OqlBuilder.from(classOf[TutorialActivity], "activity")
    status.foreach(
      a => a match {
        case "0" => {
          builder.where("size(activity.stds)<activity.capacity")
        }
        case "1" => {
          builder.where("size(activity.stds)=activity.capacity")
        }
        case "2" =>
      })
    builder.where("activity.teacher =:teacher", teachers(0))
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }
  
  
  def tutoresStds(): View = {
    val activityId = longId("tutorialActivity")
    val activity = entityDao.get(classOf[TutorialActivity], activityId)
    put("activity", activity)
    put("tutoresStds", activity.stds)
    forward()
  }

}