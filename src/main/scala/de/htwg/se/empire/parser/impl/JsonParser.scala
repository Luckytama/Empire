package de.htwg.se.empire.parser.impl

import java.io.FileNotFoundException

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.parser.Parser
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.io.Source

class JsonParser extends Parser {

  implicit val formats: DefaultFormats.type = DefaultFormats

  @throws(classOf[FileNotFoundException])
  override def parseFileToPlayingField(path: String): PlayingField = {
    val source = Source.fromFile(path)
    val playingFieldJson = source.getLines().mkString
    source.close
    parse(playingFieldJson).extract[PlayingField]
  }

}
