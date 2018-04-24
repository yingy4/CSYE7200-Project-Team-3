import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.Status
import play.api.test.FakeRequest
import play.api.test.Helpers._
import org.scalatest.mock.MockitoSugar
/**
  * Functional tests start a Play application internally, available
  *
  * as `app`.
  */
class FunctionalSpec extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar{

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/")).map(status(_)) mustBe Some(OK)
    }


    "send 200 on a good request" in  {
      route(app, FakeRequest(GET, "/")).map(status(_)) mustBe Some(OK)
    }


  }


  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe Status.OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("A simple trip planning application")

    }

  }

  // login post
  "UserController5" should {
    "render the error page"  in {

      val login =route(app,FakeRequest(POST,"/login")).get
      status(login) mustBe Status.BAD_REQUEST
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include("required")
      contentAsString(login) must include("field")

    }
  }

  "UserController4" should {
    "render the login page"  in {
      val login =route(app,FakeRequest(GET,"/login")).get
      status(login) mustBe Status.OK
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include("")
    }
  }


  "UserController3" should {
    "render the home page"  in {
      val login =route(app,FakeRequest(GET,"/logout")).get
      status(login) mustBe Status.OK
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include("simple")
      contentAsString(login)  must include("application")
    }
  }


  "UserController2" should {
    "render the home page"  in {
      val login =route(app,FakeRequest(POST,"/register")).get
      status(login) mustBe Status.BAD_REQUEST
      contentType(login) mustBe Some("text/html")

    }
    "render the error page" in {
      val x =route(app ,FakeRequest(POST,"/register")).get
      status(x) mustBe Status.BAD_REQUEST
      contentType(x) mustBe Some("text/html")

    }
  }



  "UserController1" should {
    "render the register page"  in {
      val login =route(app,FakeRequest(GET,"/register")).get
      status(login) mustBe Status.OK
      contentType(login) mustBe Some("text/html")
      contentAsString(login) must include("Email")
      contentAsString(login)  must include("Password")
      contentAsString(login)  must include("OnInput")
      contentAsString(login)  must include("human")
      contentAsString(login)  must include("robot")
    }
  }


}


