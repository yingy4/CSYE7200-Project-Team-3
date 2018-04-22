package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}



@Singleton
class PlaceController  @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

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
}
