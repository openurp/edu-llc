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

import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.llc.model.Intro
import org.beangle.webmvc.api.view.View

class IntroAction extends RestfulAction[Intro] {

  override protected def saveAndRedirect(intro: Intro): View = {
    get("intro.intro").foreach(introduction => {
      intro.intro = introduction
    })
    entityDao.saveOrUpdate(intro)
    redirect("search", "info.save.success")
  }

}
