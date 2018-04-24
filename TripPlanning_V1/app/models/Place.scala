package models

import scala.collection.mutable.ArrayBuffer


case class Place(id:String, category:String, name:String, address:String, fav:String, location:String)

object Place{

  //use text search to get place list
  def GetPlaceList(keyword: String, location:String,limitTime:Int, mode:String): Array[Place] ={

   val keywords = keyword.split(" ");
   var newKeyword = "";
   for(word<-keywords){
     newKeyword+="+";
     newKeyword+=word;
   }


    import scala.io.Source
    val url = "https://maps.googleapis.com/maps/api/place/textsearch/json?" +
      "query="+newKeyword+
      "&radius=1000" +
      "&location="+location+
      "&key=AIzaSyD3e4H1xcvtu3cwsB4iOQ9inDpH9zOVIjQ"


    val result = Source.fromURL(url).mkString

    println(result);


    import play.api.libs.json._
    //get length of list
    val jsonResult: JsValue = Json.parse(result)
    val resultArray  = (jsonResult \"results").as[List[JsValue]]
    var resultNum = resultArray.length;


    //get all place id
    val places = new Array[Place](resultNum)
    for(i <- 0 to (resultNum-1)){


      val id = removeQuotation((jsonResult \"results" \ i \ "place_id").get.toString());
      val category = removeQuotation((jsonResult \"results" \ i \ "types" \ 0).get.toString());
      val name = removeQuotation((jsonResult \"results" \ i \ "name").get.toString());
      val address = removeQuotation((jsonResult \"results" \ i \ "formatted_address").get.toString());
      val location_org = (jsonResult \"results" \ i \ "geometry" \ "location").get.toString();

      //format location org: {"lat":123,"lng":123} to:{lat:123,lng:123}
      val location_split = location_org.split(",");
      val lat = location_split(0).split(":")(1)
      val location_split2 = location_split(1).split(":")(1)
      val lng = location_split2.substring(0, (location_split2.length-1))
      val location = "{lat:"+lat+",lng:"+lng+"}";


      //ad this place to place list
      places(i) = Place.apply(id,category,name,address,"false",location);
    }


    if(limitTime>0){
      //usecase1
      return getPlacesWithLimit(places,location,limitTime,mode)
    }
    else{
      //usecase2
      return places;
    }


  }

  //remove quotation from a string
  def removeQuotation(before:String):String ={
    before.substring(1,before.length-1)
  }




  def getPlacesWithLimit(places:Array[Place], location:String,limitTime:Int, mode:String): Array[Place] ={

    //get duration: Array[String]
    val durations = getDuration(places,location,mode);

    //get index of time satisfied
    val result_places = ArrayBuffer[Place]();

    if(durations.length == places.length){
      for( x <- (0 until durations.length)){

        val regex = """([0-9]+)""".r
        val time = durations(x) match{
          case regex(num) => num
          case _ => "0"
        }
        val resTime = Integer.parseInt(time)

        val limit = limitTime*60
        if(resTime<=limit){
          result_places += places(x);
        }
      }
    }

    result_places.toArray;
  }


  //get durations
  def getDuration(places:Array[Place],location:String,mode:String):Array[String] = {
    import scala.collection.mutable.ArrayBuffer
    val durations = ArrayBuffer[String]()

    var format_id:String = "";
    for(place<-places){
      format_id = format_id + "place_id:";
      format_id = format_id + place.id;
      format_id = format_id + "|";
    }


    val url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric" +
      "&origins="+ location +
      "&destinations="+ format_id +
      "&mode="+ mode +
      "&key=AIzaSyBycYYHkKqe9uu7sQxOf7z4px41mH3FJuE";

    val response = scala.io.Source.fromURL(url).mkString;


    import play.api.libs.json._
    //list of finded result - 20
    val jsonResult: JsValue = Json.parse(response)
    for(i <- 0 to (places.length-1)){
      val duration_str = (jsonResult \ "rows" \ 0 \ "elements" \ i \ "duration" \ "value").get.toString();
      durations += duration_str
    }

    durations.toArray
  }

  def getPlaceDetailById(id:String):Place = {
    val url = "https://maps.googleapis.com/maps/api/place/details/json?" +
      "placeid="+id+
      "&key=AIzaSyD3e4H1xcvtu3cwsB4iOQ9inDpH9zOVIjQ"

    val result = scala.io.Source.fromURL(url).mkString;

    import play.api.libs.json._
    val jsonResult: JsValue = Json.parse(result)

    val name = removeQuotation((jsonResult \"result" \ "name").get.toString());
    val address = removeQuotation((jsonResult \"result" \ "formatted_address").get.toString());
    val category = removeQuotation((jsonResult \"result" \ "types" \ 0).get.toString());;

    Place.apply(id,category,name,address,"fav","location");
  }

}




