package models

import javax.inject.Inject
import scala.collection.mutable.ArrayBuffer
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

   def getEmail(username:String):String = {
    db.withConnection { implicit connection =>
      val exist:Option[String]= SQL("SELECT email FROM user WHERE name={checkName}").on("checkName"->username).as(scalar[String].singleOpt)
      exist match {
        case Some(str) => println("email:"+str);return str
        case None => println("email not found");return ""
      }
    }
  }


  def getUserId(username:String):Int = {
    db.withConnection { implicit connection =>
      val exist:Option[Int]= SQL("SELECT id FROM user WHERE name={checkName}").on("checkName"->username).as(scalar[Int].singleOpt)
      exist match {
        case Some(str) => println("user id:"+str);return str
        case None => println("email not found");return 0
      }
    }
  }
  def addToFav(place:Place, userId: Int) ={
    db.withConnection { implicit connection =>
      val placeId = place.id;
      val category = place.category;
      val name = place.name;
      val address = place.address;
      val exist:Option[String]= SQL("SELECT name FROM place WHERE place_id={place_id}").on("place_id"->placeId).as(scalar[String].singleOpt)
      exist match {
        case Some(str) =>
        case None => {
          println("I am inserting place")
          val placeTble: Option[Long] =
            SQL("insert into place(place_id, category, name, addres ) values ({place_id}, {category}, {name}, {addres})")
              .on("place_id" -> placeId, "category" -> category, "name" -> name, "addres" -> address).executeInsert();
        }
      }
      val exist1:Option[Int]= SQL("SELECT id FROM fav WHERE place_id={place_id} AND user_id = {user_id}")
        .on("place_id"->placeId, "user_id" -> userId).as(scalar[Int].singleOpt)
      exist1 match{
        case Some(str) =>
        case None =>{
          val favTable: Option[Long] =
            SQL("insert into fav(user_id, place_id) values ({user_id}, {place_id})")
              .on("user_id" -> userId, "place_id" -> placeId).executeInsert();
        }
      }
    }
  }
  def getFavList(userId:Int):Array[Place] = {
    db.withConnection { implicit connection =>
      import anorm.{ SQL, SqlParser }
      val place_Ids:List[String]= SQL("SELECT place_id FROM fav WHERE user_id ={user_id}").on("user_id"->userId).as(SqlParser.str("place_id").+);
      val result_places = ArrayBuffer[Place]();
      for(place_id<-place_Ids){
        SQL("SELECT category FROM place WHERE place_id={place_id}").on("place_id"->place_id).as(scalar[String].singleOpt) match {
          case Some(category) =>{
            SQL("SELECT name FROM place WHERE place_id={place_id}").on("place_id"->place_id).as(scalar[String].singleOpt) match {
              case Some(name) => {
                SQL("SELECT addres FROM place WHERE place_id={place_id}").on("place_id"->place_id).as(scalar[String].singleOpt) match {
                  case Some(address) => result_places += Place.apply(place_id,category,name,address,"fav","location");
                  case None =>
                }
              }
              case None=>
            }
          }
          case None=>
        }
      }
      return result_places.toArray;
    }
  }
  def removeFromFav(userId: Int, placeId:String):Boolean  = {
    db.withConnection { implicit connection =>
      val result: Int = SQL("delete from fav where user_id = {user_id} AND place_id = {place_id}")
        .on("user_id"-> userId, "place_id" -> placeId).executeUpdate();
      result match{
        case 0 => false
        case 1 => true
      }
    }
  }



}