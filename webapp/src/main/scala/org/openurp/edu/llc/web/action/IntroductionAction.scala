package org.openurp.edu.llc.web.action

import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.RestfulAction
import org.openurp.edu.llc.model.Intro

class IntroductionAction extends RestfulAction[Intro]{
  
  override def index():View={
    put("intro", entityDao.getAll(classOf[Intro])(0))
    forward()
  }
  
}