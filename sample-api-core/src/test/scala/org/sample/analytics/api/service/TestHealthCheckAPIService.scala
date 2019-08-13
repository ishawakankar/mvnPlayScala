package org.sample.analytics.api.service

import akka.actor.ActorSystem
import org.scalatest._
import akka.testkit.TestActorRef
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import com.typesafe.config.ConfigFactory
import org.sample.analytics.api.util.HTTPClient


class TestHealthCheckAPIService extends FlatSpec with Matchers with BeforeAndAfterAll with MockitoSugar {

  "HealthCheckAPIService" should "return health status of the API" in {
    implicit val config = ConfigFactory.load()

    val HTTPClientMock = mock[HTTPClient]
    implicit val actorSystem = ActorSystem("testActorSystem", config)

    val apiURL = "https://c43e69c3-76bc-4a52-9c2c-0f0859c57724.mock.pstmn.io/dash"
    when(HTTPClientMock.get[String](apiURL)).thenReturn("welcome")

    val experimentService = TestActorRef(new APIService((HTTPClientMock))).underlyingActor
    println(experimentService.getStatus)

    experimentService.getStatus should be("welcome")

  }

}
