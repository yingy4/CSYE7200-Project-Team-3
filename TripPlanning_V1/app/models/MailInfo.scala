package models

case class MailInfo(Name:String, Address:String, phone:String, website:String, reviews:Array[String])
object MailInfo{
  def getPlaceInfo(id:String):MailInfo = {
    val url = "https://maps.googleapis.com/maps/api/place/details/json?" +
      "placeid="+id+
      "&key=AIzaSyD3e4H1xcvtu3cwsB4iOQ9inDpH9zOVIjQ"
    val result = scala.io.Source.fromURL(url).mkString;
    import play.api.libs.json._
    val jsonResult: JsValue = Json.parse(result)
    val name = removeQuotation((jsonResult \"result" \ "name").get.toString());
    val address = removeQuotation((jsonResult \"result" \ "formatted_address").get.toString());
    val phone = removeQuotation((jsonResult \"result" \ "international_phone_number").get.toString());
    val website = removeQuotation((jsonResult \"result" \ "website").get.toString());
    val reviewArray  = (jsonResult \"result"\"reviews").as[List[JsValue]]
    var reviewNum = reviewArray.length;
    val reviews = new Array[String](reviewNum)
    for(i <- 0 to (reviewNum-1)){
      val review = removeQuotation((jsonResult \"result"\"reviews" \ i \ "text").get.toString());
      reviews(i) = review;
    }
    MailInfo.apply(name,address,phone,website,reviews);
  }
  def removeQuotation(before:String):String ={
    before.substring(1,before.length-1)
  }
}