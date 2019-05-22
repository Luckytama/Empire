package microservice.controller

import java.io.FileNotFoundException

import org.json4s._

import scala.io.Source

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
