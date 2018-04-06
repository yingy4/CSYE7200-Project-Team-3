package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{AbstractController, ControllerComponents}



@Singleton
class PlaceController  @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  //link:/case1/placeList
  def case1Search = Action{Ok(views.html.placelist.render())}

  //link:/case2/placeList
  def case2Search = Action{Ok(views.html.placelist.render())}

  //link:/placedetail
  def placedetail = Action{Ok(views.html.placedetail.render())}

}
