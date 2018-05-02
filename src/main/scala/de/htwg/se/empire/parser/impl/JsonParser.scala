package de.htwg.se.empire.parser.impl

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.parser.Parser
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.io.Source

class JsonParser extends Parser {

  implicit val formats: DefaultFormats.type = DefaultFormats

  override def parseFile(path: String): PlayingField = {
    val source: String = Source.fromFile(path).getLines().mkString
    parse(source).extract[PlayingField]
  }
}
