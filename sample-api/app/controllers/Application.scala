package controllers

import akka.actor.{ActorSystem, Props}
import org.sample.analytics.api.service._
import akka.routing._
import play.api._
import play.api.mvc._
import play.api.mvc.Result
import play.api.libs.json._
import akka.util.Timeout
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import akka.pattern._
import play.api.libs.functional.syntax._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import akka.actor.{ActorRef, ActorSystem, Props}

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.pattern._
import javax.inject.{Inject, Singleton}
import org.sample.analytics.api.util.RestUtil
import org.sample.analytics.api.service.APIService

/**
  * @author Isha
  */

@Singleton
class Application @Inject() (system: ActorSystem) extends Controller {

  implicit val className = "controllers.Application"
  implicit val timeout: Timeout = 20 seconds

//  val healthCheckActor = system.actorOf(Props[HealthCheckAPIService].withRouter(FromConfig()), name = "healthCheckActor")

  val APIActor = system.actorOf(Props(new APIService(RestUtil)), "APIActor")

  def getAPIMessage() = Action.async { implicit request =>
    val result = ask(APIActor, None).mapTo[String]
    result.map { x =>
      Ok(x).withHeaders(CONTENT_TYPE -> "text/plain")
    }
  }
}