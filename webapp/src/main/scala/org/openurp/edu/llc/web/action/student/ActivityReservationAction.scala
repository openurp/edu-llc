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
import org.openurp.edu.llc.model.TutoredStd
import java.time.Instant
import org.openurp.edu.llc.model.TutorialActivity
import org.beangle.security.Securities
import org.beangle.data.dao.OqlBuilder
import org.openurp.edu.base.model.Student
import org.beangle.webmvc.api.view.View
import org.openurp.edu.llc.model.TutorialSwitch
import java.time.LocalDate
import org.openurp.edu.base.model.Student
import org.openurp.edu.llc.model.TutorialActivity
import org.openurp.edu.llc.model.TutorialSwitch

class ActivityReservationAction extends RestfulAction[TutoredStd] {

  override protected def indexSetting(): Unit = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)
    val tutoredStds = entityDao.findBy(classOf[TutoredStd], "std", students)
    val chooseAvtivities = tutoredStds.map(_.activity)
    put("chooseAvtivities", chooseAvtivities)
    put("switch", entityDao.getAll(classOf[TutorialSwitch])(0))
    val switchBuilder = OqlBuilder.from(classOf[TutorialSwitch], "switch")
    switchBuilder.where("switch.opened = true and switch.beginAt < :now and switch.endAt > :now", Instant.now())
    put("switches", entityDao.search(switchBuilder))
  }

  def activities(): View = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)
    val tutoredStds = entityDao.findBy(classOf[TutoredStd], "std", students)
    val chooseAvtivities = tutoredStds.map(_.activity)
    put("chooseAvtivities", chooseAvtivities)

    val switchBuilder = OqlBuilder.from(classOf[TutorialSwitch], "switch")
    switchBuilder.where("switch.opened = true and switch.beginAt < :now and switch.endAt > :now", Instant.now())
    put("switches", entityDao.search(switchBuilder))
    val activityBuilder = OqlBuilder.from(classOf[TutorialActivity], "activity")
    activityBuilder.where("activity.date >:now", LocalDate.now())
    put("activities", entityDao.search(activityBuilder))
    forward()
  }

  protected def choose(): View = {
    val user = Securities.user
    val stdBuilder = OqlBuilder.from(classOf[Student], "student")
    stdBuilder.where("student.user.code =:code ", user)
    val students = entityDao.search(stdBuilder)

    val activity = entityDao.find(classOf[TutorialActivity], longId("activity")).get

    val tutoredStds = entityDao.findBy(classOf[TutoredStd], "std", students)
    val chooseAvtivities = tutoredStds.map(_.activity)

    val conflict = chooseAvtivities.exists(x => x.date == activity.date && activity.beginAt < x.endAt && x.beginAt < activity.endAt)
    if (activity.stds.size < activity.capacity) {
      if (!conflict) {
        val tutoredStd = populateEntity()
        tutoredStd.activity = activity
        tutoredStd.std = students(0)
        tutoredStd.updatedAt = Instant.now()
        entityDao.saveOrUpdate(tutoredStd)
        redirect("index", "预约成功")
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

    val activity = entityDao.find(classOf[TutorialActivity], longId("activity"))
    val tutoredStdBuilder = OqlBuilder.from(classOf[TutoredStd], "tutoredStd")
    tutoredStdBuilder.where("tutoredStd.std =:std", students(0))
    tutoredStdBuilder.where("tutoredStd.activity =:activity", activity)
    val tutoredStds = entityDao.search(tutoredStdBuilder)
    entityDao.remove(tutoredStds)

    redirect("index")
  }

}
