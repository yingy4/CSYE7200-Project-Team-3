package models

case class Position(lat: Double, lon: Double)

import spray.json._


object PositionProtocol extends DefaultJsonProtocol {
  implicit val positionFormat = jsonFormat2(Position.apply)
}

object Position{
  import PositionProtocol._
  def toJSON(lat:String,lon:String) ={
    //Position(lat.toDouble,lon.toDouble).toJson;
    Position(lat.toDouble,lon.toDouble)
  }

  def getCurrentLocation = {
    val url = "http://ip-api.com/json/?callback=?"
    val result = scala.io.Source.fromURL(url).mkString
    val newResult = result.substring(2,(result.length-2))
    import play.api.libs.json._
    val json1: JsValue = Json.parse(newResult)
    val lat = (json1 \ "lat").get
    val lon = (json1 \ "lon").get
    toJSON(lat.toString(),lon.toString())
  }


  def getOtherLocation(keyword: String) = {
    var address:String = "";
    for(str:String<-keyword.split(" ")){
      address = address + str
      address = address + "+"
    }

    //use google geocode api to get position of other location
    val newAddress = address.substring(1,(address.length-1))
    val url:String = "https://maps.googleapis.com/maps/api/geocode/json?address="+newAddress+"&key=YOUR_API_KEY";
    val result = scala.io.Source.fromURL(url).mkString

    import play.api.libs.json._
    val otherLocationJson:JsValue = Json.parse(result)
    val lat = (otherLocationJson \ "results" \ 0 \ "geometry" \ "location" \ "lat").get
    val lng = (otherLocationJson \ "results" \ 0 \ "geometry" \ "location" \ "lng").get
    toJSON(lat.toString(),lng.toString())
  }

  def getFormattedAddress(lat:String, lon:String)= {
    println("lat:"+lat);
    println("lon:"+lon);
    val info = lat+","+lon;
    val url:String = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+info+"&key=YOUR_API_KEY";
    val result = scala.io.Source.fromURL(url).mkString
    import play.api.libs.json._
    val resultJson:JsValue = Json.parse(result)
    val address = (resultJson \"results" \ 0\ "formatted_address").get
    //return value
    address
  }

}


