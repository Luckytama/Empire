package de.htwg.se.empire.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.ActorMaterializer
import com.google.inject.{Guice, Injector}
import de.htwg.se.empire.EmpireModule

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ControllerApi {

  implicit val system: ActorSystem = ActorSystem("controller-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val attackController: AttackController = injector.getInstance(classOf[AttackController])
  val reinforcementController: ReinforcementController = injector.getInstance(classOf[ReinforcementController])
  val initController: InitController = injector.getInstance(classOf[InitController])

  def main(args: Array[String]): Unit = {
    startControllerApi()
  }

  private def startControllerApi(): Unit = {
    val route = path("") {
      get {
        complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, "ControllerApi is up")))
      }
    }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8000)

    println(s"Server online at http://localhost:8000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
