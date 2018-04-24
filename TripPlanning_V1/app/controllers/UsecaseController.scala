package controllers

import javax.inject.{Inject, Singleton}

import models.Place
import play.api.mvc.{AbstractController, ControllerComponents}
import models.{Place, Position, UserRepository}

@Singleton
class UsecaseController @Inject()(cc: ControllerComponents,userService: UserRepository) extends AbstractController(cc) with play.api.i18n.I18nSupport {


  //GET /usecase
  def forms() = Action{ implicit request =>
    request.session.get("current_user").map { user =>
      val show:String = "";
      Ok(views.html.usecase(user,SearchForm_case1.searchForm_case1,SearchForm_case2.searchForm_case2,show))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }

  }

  def myFav() = Action{implicit request =>
    request.session.get("current_user").map { user =>
      val userId = userService.getUserId(user);
      val places = userService.getFavList(userId);

      for(place<-places){
        println(place.name)
        println(place.category)
        println(place.address)
      }

      val currentLocation = Position.getCurrentLocation
      val currentLocationJson:String = currentLocation.lat + ","+currentLocation.lon;
      Ok(views.html.favlist(places,"keyword", currentLocationJson))
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }
  }


  //POST /usecase1_form
  //handel usecase1 requset, with form validation
  def formsPost_case1() = Action{ implicit request =>

    request.session.get("current_user").map { user =>
      SearchForm_case1.searchForm_case1.bindFromRequest.fold(
        formWithErrors => {
          val show:String = "usecase1_form"
          BadRequest(views.html.usecase(user,formWithErrors,SearchForm_case2.searchForm_case2,show))
        },
        formData => {
          import models.Position
          val currentLocation = Position.getCurrentLocation
          val currentLocationJson:String = currentLocation.lat + ","+currentLocation.lon;


          import models.Place
          val places:Array[Place] = Place.GetPlaceList(formData.keyword,currentLocationJson,formData.time,formData.travelModel);


          if(formData.currentLocation == "otherLocation"){
            val otherLocation = Position.getOtherLocation(formData.otherLocation2)
            val otherLocationJson:String = otherLocation.lat + "," +otherLocation.lon;

            Ok(views.html.placelist(places,formData.keyword, otherLocationJson))
          }else{
            Ok(views.html.placelist(places,formData.keyword, currentLocationJson))
          }
        }
      )
    }.getOrElse {
      Ok(views.html.error("Oops, you are not connected"))
    }


  }

  //POST /usecase2_form
  //handel usecase2 requset, with form validation
  def formsPost_case2() = Action{ implicit request =>

    request.session.get("current_user").map { user =>
      SearchForm_case2.searchForm_case2.bindFromRequest.fold(
        formWithErrors => {
          val show:String = "usecase2_form"
          BadRequest(views.html.usecase(user,SearchForm_case1.searchForm_case1,formWithErrors,show))
        },
        formData => {

          import models.Position
          val currentLocation = Position.getCurrentLocation;
          val currentLocationJson:String = currentLocation.lat + ","+currentLocation.lon;

          val places:Array[Place] = Place.GetPlaceList(formData.keyword,currentLocationJson,0,"");

          //if not from current location
          if(formData.currentLocation == "otherLocation"){
            val otherLocation = Position.getOtherLocation(formData.otherLocation2)
            val otherLocationJson:String = otherLocation.lat + "," +otherLocation.lon;
            Ok(views.html.placelist(places,formData.keyword, otherLocationJson))
          }//if from current location
          else{
            Ok(views.html.placelist(places,formData.keyword, currentLocationJson))
          }

        }
      )
    }.getOrElse {
      Unauthorized("Oops, you are not connected")
    }

  }


}

