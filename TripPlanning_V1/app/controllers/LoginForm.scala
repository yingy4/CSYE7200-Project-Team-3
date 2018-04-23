package controllers

import play.api.data.Form
import play.api.data.Forms._
import models.UserRepository
import play.api.libs.mailer._
import java.io.File
import org.apache.commons.mail.EmailAttachment
import javax.inject.Inject


case class LoginForm (name:String, password: String)


object LoginForm{


  val loginForm:Form[LoginForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply)
      verifying("password and username are not match", registerForm => checkPassword(registerForm.name,registerForm.password))
  )

  def checkPassword(username:String, password:String) = {
    true
  }


}



