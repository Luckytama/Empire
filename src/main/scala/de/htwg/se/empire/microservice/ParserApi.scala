package de.htwg.se.empire.microservice

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.google.inject.{Guice, Injector}
import de.htwg.se.empire.EmpireModule
import de.htwg.se.empire.microservice.controller.ParserApiController

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ParserApi extends Directives {

  val REST_PORT: Int = 8000

  implicit val system: ActorSystem = ActorSystem("parser-system")
  system.eventStream.setLogLevel(Logging.DebugLevel)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val injector: Injector = Guice.createInjector(new EmpireModule)

  def main(args: Array[String]): Unit = {
    startControllerApi()
  }

  private def startControllerApi(): Unit = {
    val route = path("loadgridfromfile") {
      post {
        entity(as[String]) { pathToFile =>
          system.log.debug("Received loadgridformfile request with followed entity: " + pathToFile)
          complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, ParserApiController.loadGrid(pathToFile))))
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", REST_PORT)

    println(s"Server online at http://localhost:8000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
