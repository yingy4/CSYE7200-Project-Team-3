package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}
import play.api.libs.mailer._
import java.io.File
import javax.inject.Inject
import models.UserRepository
import models.MailInfo
import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc._


@Singleton
class PlaceController  @Inject()(mailerClient: MailerClient,userService: UserRepository ,cc: ControllerComponents) extends AbstractController(cc) {

  //link:/case1/placeList
  def case1Search = TODO

  //link:/case2/placeList
  def case2Search = TODO

  //link:/placedetail
  def placedetail(id:Option[String],location:String,lat_from:String,lon_from:String) = Action { implicit request =>
    id match {
      case Some(id) => {
        //lat, lng: position of the specfic place
        val strs:Array[String] = location.split(",");
        val _lat = strs(0);
        val lat = _lat.substring(5,_lat.length)
        val _lng = strs(1);
        val lng = _lng.substring(4,_lng.length-1)

        //lat_from, lon_from: position of the from location
        val fromLocation_org:String = models.Position.getFormattedAddress(lat_from,lon_from).toString()
        val fromLocation:String = fromLocation_org.substring(1,fromLocation_org.length-1)

        Ok(views.html.placedetail(id,lat,lng,fromLocation))
      };
      case None => Ok(views.html.error("Place id not found"));
    }
  }






  def sendToEmail(id:Option[String]) = Action{  implicit request =>
    request.session.get("current_user").map{ user =>
      id match{
        case Some(id) => {
          //get place detail to send email
          val mailInfo = MailInfo.getPlaceInfo(id);

          //send email head

          //add text
          println("name:"+mailInfo.Name);
          println("address:"+mailInfo.Address);
          println("phone:"+mailInfo.phone);
          println("website:"+mailInfo.website);
          var reviewText = "";
          for(review <- mailInfo.reviews) {
            println("review:" + review);
            reviewText += review;
            reviewText += "\n"
          }

          //get user:
          println("user:"+user);
          val email = userService.getEmail(user);
          sendEmail(mailInfo.Name,mailInfo.Address,mailInfo.phone,mailInfo.website,email,mailInfo.reviews);

          Ok(views.html.Message("Email has been send",1));
        }
        case None => Ok(views.html.error("Place id not found"));
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }


  def sendEmail(name:String,addr:String, num:String, website:String, mailaddr: String, review:Array[String]): Unit ={
    var temp = "liyanghusky@gmail.com"
    val cid = "Place Email"
    var reviewText = "";
    for(review <- review) {
      reviewText += review;
      reviewText += "\n"+"" +
        "\n"
    };
    val email = Email(
      "Trval details",
      "Mister FROM <li.yang5@husky.neu.edu>",
      Seq("Miss TO <"+ mailaddr +">"),
      // adds attachment
      // sends text, HTML or both...
      bodyText = Some("Here is your travel destnation Detial!" +"\n"+"\n" +
        "Name: " + name +"\n" +"\n" +
        "Address: "+ addr + "\n" +"\n" +
        "Phone-Number: " + num +"\n" +"\n" +
        "Website: " + website +"\n" +"\n" +
        "\n" +"" +
        "Reviews" + "\n" + reviewText
      )
    )
    mailerClient.send(email)
  }


  def addToFav(id:Option[String]) = Action{  implicit request =>
    request.session.get("current_user").map{ user =>
      id match{
        case Some(id) => {
          val place = models.Place.getPlaceDetailById(id);
          val userId = userService.getUserId(user);
          userService.addToFav(place,userId);
          Ok(views.html.Message("Place has been added to your fav list",1));
        }
        case None => Ok(views.html.error("Place id not found"));
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }
  def removeFromFav(id:Option[String]) = Action{  implicit request =>
    request.session.get("current_user").map{ user =>
      id match{
        case Some(id) => {
          val userId = userService.getUserId(user);
          val result = userService.removeFromFav(userId,id);
          result match{
            case true => Ok(views.html.Message("removeOk",1));
            case false =>  Ok(views.html.error("remove failed, please try again"));
          }
        }
        case None => Ok(views.html.error("Place id not found"));
      }
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }



}
