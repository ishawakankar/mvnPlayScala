package org.sample.analytics.api.service

import akka.actor.Actor
import org.sample.analytics.api.util.HTTPClient

class APIService(restUtil: HTTPClient) extends Actor {

  implicit val className = "org.sample.analytics.api.service.HealthCheckAPIService"
  val apiURL = "https://c43e69c3-76bc-4a52-9c2c-0f0859c57724.mock.pstmn.io/dash" // A mock server

  def receive = {
    case _ => sender() ! getStatus
  }

  def getStatus: String = {
    try {
      val response = restUtil.get[String](apiURL)
      response.toString()
    }
    catch {
      case ex: Exception =>
        ex.printStackTrace()
        ""
    }
  }
}
