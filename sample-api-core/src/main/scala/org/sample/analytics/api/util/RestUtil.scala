package org.sample.analytics.api.util

import org.apache.http.client.methods.{HttpGet, HttpRequestBase}
import org.apache.http.impl.client.HttpClients
import org.sample.analytics.api.util.RestUtil

import scala.io.Source

trait HTTPClient {
  def get[T](apiURL: String)(implicit mf: Manifest[T]): T
}

object RestUtil extends HTTPClient {

  implicit val className = "org.sample.analytics.api.util.RestUtil"

  private def _call[T](request: HttpRequestBase)(implicit mf: Manifest[T]) = {
    val httpClient = HttpClients.createDefault();
    try {
      val httpResponse = httpClient.execute(request);
      val entity = httpResponse.getEntity()
      val inputStream = entity.getContent()
      val content = Source.fromInputStream(inputStream, "UTF-8").getLines.mkString;
      inputStream.close
      if ("java.lang.String".equals(mf.toString())) {
        content.asInstanceOf[T];
      } else {
        content.asInstanceOf[T]; //Fix: Deserialize content and return
      }
    } finally {
      httpClient.close()
    }
  }

  def get[T](apiURL: String)(implicit mf: Manifest[T]) = {
    val request = new HttpGet(apiURL);
    try {
      _call(request.asInstanceOf[HttpRequestBase])
    }
    catch {
      case ex: Exception =>
        ex.printStackTrace();
        null.asInstanceOf[T];
    }
  }
}
