package de.htwg.se.empire.controller.impl

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpCharsets._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import de.htwg.se.empire.parser.impl.JsonParser

import scala.concurrent.{ExecutionContextExecutor, Future}

class ApiController {

  val URL_PREFIX_TO_PARSER_API: String = "http://localhost:8000"
  implicit val system: ActorSystem = ActorSystem()
  system.eventStream.setLogLevel(Logging.DebugLevel)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val parser: JsonParser = new JsonParser

  def getPlayingFieldFromParserApi(json: String): Future[HttpResponse] = {
    system.log.debug("Api Controller send request to load the file")
    sendPost(json, URL_PREFIX_TO_PARSER_API + "/loadgridfromfile")
  }

  def sendPost(data: String, url: String): Future[HttpResponse] = {
    system.log.debug("Sending Request....")
    system.log.debug("DATA: " + data.toString)
    system.log.debug("URL: " + url)
    Http().singleRequest(HttpRequest(POST, uri = url, entity = HttpEntity(`text/plain` withCharset `UTF-8`, data)))
  }
}
