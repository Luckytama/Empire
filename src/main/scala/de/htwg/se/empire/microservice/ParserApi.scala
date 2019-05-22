package microservice

import java.io.FileNotFoundException

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse }
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import org.json4s.DefaultFormats

import scala.concurrent.ExecutionContextExecutor
import scala.io.{ Source, StdIn }

object ParserApi extends Directives {

  val REST_PORT: Int = 8000

  implicit val system: ActorSystem = ActorSystem("parser-system")
  system.eventStream.setLogLevel(Logging.DebugLevel)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

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

    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8000)

    println(s"Server online at http://0.0.0.0:8000/\nPress RETURN to stop...")

  }
}

object ParserApiController {

  implicit val formats: DefaultFormats.type = DefaultFormats

  def loadGrid(path: String): String = {
    getPlayingFieldFromFile(path)
  }

  @throws(classOf[FileNotFoundException])
  private def getPlayingFieldFromFile(path: String): String = {
    val source = Source.fromFile(path)
    val playingFieldJson = source.getLines().mkString
    source.close
    playingFieldJson
  }
}
