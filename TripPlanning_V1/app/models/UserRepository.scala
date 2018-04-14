package models

import javax.inject.Inject

import anorm.SqlParser.{get, scalar}
import anorm._
import play.api.db.DBApi

import scala.concurrent.Future
import scala.util.Failure


case class User (id: Option[Long] = None,
                 name: String,
                 password: String,
                 email:String)
case class Login(name:String,password:String)

object User {
  implicit def toParameters: ToParameterList[User] =
    Macro.toParameters[User]
}

@javax.inject.Singleton
class UserRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext){

  private val db = dbapi.database("default")

  // -- Parsers

  /**
    * Parse a User from a ResultSet
    */
  private val simple = {
    get[Option[Long]]("user.id") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[String]("user.email")map {
      case id ~ name ~ password ~ email =>
        User(id, name, password,email)
    }
  }



  // -- Queries

  /**
    * Insert a new user.
    *
    * @param user The user values.
    */
  def insert(user: User): Future[Option[Long]] = Future {
    db.withConnection { implicit connection =>
      SQL("""
        insert into user (name, password,email) values (
          {name}, {password}, {email}
        )
      """).bind(user).executeInsert()
    }
  }(ec)


  /**
    * check use if exist.
    * exist: true
    * not exist:false
    */
  def check_if_exist(name: String): Boolean =  {
    db.withConnection { implicit connection =>
      val exist:Option[String]= SQL("SELECT name FROM user WHERE name={checkName}").on("checkName"->name).as(scalar[String].singleOpt)
      exist match {
        case Some(str) => true
        case None => false
      }
    }
  }

  def authentication(name:String,password:String):Boolean=  {
    db.withConnection { implicit connection =>
      val exist:Option[String]= SQL("SELECT password FROM user WHERE name={checkName}").on("checkName"->name).as(scalar[String].singleOpt)
      exist match {
        case Some(str) => if(str.equals(password)){println(name+" is login");true}else{println("password not correct");false}
        case None =>false
      }
    }
  }

  //register a user
  def register(user:User):Boolean = {
    import scala.util.Success
    check_if_exist(user.name) match {
      case true => false
      case false => println("user can register");insert(user);true
      case _ =>false
    }

  }

  //login a user
  def login(user:Login):Boolean = {
    import scala.util.Success
    check_if_exist(user.name) match{
      case true =>
      case false => println("user name doesn't exist");false
    }
    authentication(user.name,user.password) match {
      case true => true
      case false => false
      case _ =>false
    }

  }



}