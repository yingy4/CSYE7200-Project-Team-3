package controllers
import java.lang.ProcessBuilder.Redirect

import play.api.data.Form
import play.api.data.Forms._


//define a case class that hold your form data
case class SearchForm_case2(keyword:String, currentLocation:String, otherLocation2:String)


//tell how to map between a form data and our new case class
object SearchForm_case2 {

  val searchForm_case2:Form[SearchForm_case2] = Form(
    mapping(
      "keyword" -> nonEmptyText,
      "currentLocation2" -> nonEmptyText,
      "otherLocation2" -> text
    )(SearchForm_case2.apply)(SearchForm_case2.unapply)verifying("Other location cannot be empty",
        searchForm_case2=>{
          checkOtherLocation(searchForm_case2.currentLocation,searchForm_case2.otherLocation2)
        })

  )

  def checkOtherLocation(choose:String,text:String):Boolean = {
    choose match {
    case "currentLocation" => true
    case "otherLocation" => if(!(text=="")){true}else{false}
    case "" => false
  }}
}



