package controllers

import javax.inject._

import play.api.libs.mailer.{Email, MailerClient}
import play.api.mvc._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents,mailerClient: MailerClient) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    //Ok(views.html.index("Your new application is ready."))
    val temparr = new Array[String](10);
    //sendEmail("test","test","test","test","test", temparr);
    Ok(views.html.home.render())

  }


  def sendEmail(name:String,addr:String, num:String, website:String, mailaddr: String, review:Array[String]): Unit ={
    var temp = "liyanghusky@gmail.com"
    val cid = "Place Email"
    val email = Email(
      "Trval details",
      "Mister FROM <li.yang5@husky.neu.edu>",
      Seq("Miss TO <"+ temp +">"),
      // adds attachment
      // sends text, HTML or both...
      bodyText = Some("Here is your travel destnation Detial!" +"\n"+
        "Name: " + name +"\n" +
        "Address: "+ addr + "\n" +
        "Phone-Number: " + num +"\n" +
        "Website: " + website +"\n" + "" +
        "\n" +"" +
        "Reviews" + "\n"
      )
    )
    mailerClient.send(email)
  }

}
