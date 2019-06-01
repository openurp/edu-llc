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
