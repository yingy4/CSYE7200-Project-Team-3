package controllers

import play.api.data.Form
import play.api.data.Forms._

case class RegisterForm (Name:String, Email:String, Password:String, ConfirmPassword:String, test:String)

object RegisterForm{


  val registerForm:Form[RegisterForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> email,
      "password" -> nonEmptyText,
      "confirmPassword" -> nonEmptyText,
      "test" -> nonEmptyText
    )(RegisterForm.apply)(RegisterForm.unapply)
    verifying("two passwords not same", registerForm => checkPassword(registerForm.Password,registerForm.ConfirmPassword))
  )

  def checkPassword(key1:String, key2:String) = {
    if(key1==key2){
       true
    }else{
       false
    }
  }





}
