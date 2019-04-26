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