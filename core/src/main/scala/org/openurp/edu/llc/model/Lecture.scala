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
package org.openurp.edu.llc.model

import java.time.LocalDate

import scala.collection.mutable.Buffer

import org.beangle.commons.collection.Collections
import org.beangle.commons.lang.time.HourMinute
import org.beangle.data.model.LongId
import org.openurp.base.model.Department
import org.openurp.edu.base.model.{ Classroom, Project, Semester }

/**
 * 讲座、报告、活动
 */
class Lecture extends LongId {

  var project: Project = _

  var semester: Semester = _

  var subject: String = _

  var teachers: String = _

  var depart: Department = _

  var date: LocalDate = _

  var beginAt: HourMinute = _

  var endAt: HourMinute = _

  var room: Option[Classroom] = None

  var location: Option[String] = None

  var capacity: Int = _

  var actual: Int = _

  var audiences: Buffer[Audience] = Collections.newBuffer[Audience]

}
