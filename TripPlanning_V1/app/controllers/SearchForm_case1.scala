package controllers
import play.api.data.Form
import play.api.data.Forms._

case class SearchForm_case1 (keyword:String, travelModel: String, time: Int, currentLocation:String, otherLocation2:String)

//tell how to map between a form data and our new case class

object SearchForm_case1 {

  val searchForm_case1: Form[SearchForm_case1] = Form(
    mapping(
      "keyword" -> nonEmptyText,
      "travelModel" -> nonEmptyText,
      "time" -> number(min = 0),
      "currentLocation1" -> nonEmptyText,
      "otherLocation1" -> text
    )(SearchForm_case1.apply)(SearchForm_case1.unapply)verifying("Other location cannot be empty",
      SearchForm_case1=>{
        checkOtherLocation(SearchForm_case1.currentLocation,SearchForm_case1.otherLocation2)
      })
  )

  def checkOtherLocation(choose:String,text:String):Boolean = {
    choose match {
      case "currentLocation" => true
      case "otherLocation" => if(!(text=="")){true}else{false}
      case "" => false
    }}

}
