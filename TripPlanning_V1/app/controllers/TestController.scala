package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class TestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def getMap = Action{
    Ok(views.html.Example_map_geo("message"))
  }


  //place
  def testPlace = Action{
    Ok(views.html.Example_place.render());
  }



}
