package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UsecaseController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  //link: /usecase
  def usecase = Action{Ok(views.html.usecase.render())}

}
