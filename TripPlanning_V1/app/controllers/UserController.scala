package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}


import models._
import play.api.data.Forms._
import play.api.data._
import models.UserRepository
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               userService: UserRepository)(implicit ec: ExecutionContext) extends AbstractController(cc) with play.api.i18n.I18nSupport{


  val userForm_register:Form[User] = Form(
    mapping(
      "id" -> ignored(None: Option[Long]),
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
      "email" -> email
    )(User.apply)(User.unapply)
  )

  val userForm_login:Form[Login] = Form(
    mapping(
      "name" -> nonEmptyText,
      "password"-> nonEmptyText
    )(Login.apply)(Login.unapply)
  )


  //link:/login
  def login = Action{implicit request=>
    Ok(views.html.login(userForm_login))
  }

  def logout = Action{
    Ok(views.html.home.render()).withNewSession
  }

  //POST /login
  def formsPost_login() = Action{ implicit request =>
    userForm_login.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      },
      user => {
        val result = userService.login(user);
        if(result.equals(true)){
          Ok(views.html.usecase(user.name,SearchForm_case1.searchForm_case1,SearchForm_case2.searchForm_case2,""))
            .withSession(
              "current_user" -> user.name)
        }else{
          Ok(views.html.error("Login Failed, Username and password not pair. Try again."))
        }

      }
    )
  }


  //link:/register
  def register = Action{implicit request=>
    Ok(views.html.register(userForm_register))
  }

  //POST /register
  def formsPost_register() = Action{ implicit request =>
    userForm_register.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      user_register => {
        val result = userService.register(user_register);
        if(result.equals(true)){
          Ok(views.html.home.render())
        }else{
          Ok(views.html.error("Register Failed, Username is already exist. Try again."))
        }

      }
    )
  }
}
