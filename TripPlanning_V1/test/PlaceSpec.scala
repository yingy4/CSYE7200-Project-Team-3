import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._


class PlaceSpec extends PlaySpec with GuiceOneAppPerSuite {



  "PlaceControllerDetail" should {
    "render the placedetail page"  in {
      val login =route(app,FakeRequest(GET,"/placedeatil?id=ChIJYxwUj25544kRcHNM0FRKnP8&location={lat:42.31148049999999,lng:-71.11400379999999}&lat=42.3059&lon=-71.0859")).get
      status(login) mustBe Status.OK
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include("photo")
      contentAsString(login) must include("Phone Number")
      contentAsString(login) must include("Rating")
      contentAsString(login) must include("Address")
      contentAsString(login) must include("Driving")
      contentAsString(login) must include("Walking")
      contentAsString(login) must include("Bicycling")


    }
  }


  "PlaceControllerAddtoFav" should {
    "render the Message page"  in {
      val login =route(app,FakeRequest(GET,"/addToFav?id=ChIJvzt-LYx544kRcz7UXq2GCMk&current_user=admin")).get
      status(login) mustBe Status.UNAUTHORIZED
    }
  }

  "PlaceControllerRemoveFav" should {
    "render the Message page"  in {
      val login =route(app,FakeRequest(GET,"/removeFromFav??id=ChIJvzt-LYx544kRcz7UXq2GCMk")).get
      status(login) mustBe Status.UNAUTHORIZED
    }
  }

  "PlaceControllerEmail" should {
    "render the Message page"  in {
      val login =route(app,FakeRequest(GET,"/sendToEmail?id=ChIJvzt-LYx544kRcz7UXq2GCMk")).get
      status(login) mustBe Status.UNAUTHORIZED

    }
  }

}
