package de.htwg.se.empire.view.rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.parser.impl.JsonParser

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

class RestApi(gameController: GameController) {

  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val parser = new JsonParser

  def startRestApi(restPort: Int) {
    val route =
      path("status") {
        get {
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, gameController.status.toString)))
        }
      } ~ path("players") {
        get {
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, gameController.playingField.players.toString())))
        }
      } ~ path("playingfield") {
        get {
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, gameController.playingField.generateJsonObject)))
        }
      } ~ path("save") {
        get {
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, parser.parsePlayingFieldToFile(gameController.playingField.generateJsonObject))))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", restPort)

    println(s"Server online at http://localhost:8888/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
