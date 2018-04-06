package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {



  //link:/login
  def login = Action{Ok(views.html.login.render())}


  //link:/login
  def register = Action{Ok(views.html.register.render())}
}
