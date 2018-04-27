package de.htwg.se.empire.parser.impl

import de.htwg.se.empire.model.grid.{ Continent, Country }
import de.htwg.se.empire.parser.Parser
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.io.Source

class JsonParser extends Parser {

  implicit val continentReads: Reads[Continent] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "bonus").read[Int] and
    (JsPath \ "countries").read[List[Country]]
  )(Continent.apply _)

  implicit val countryReads: Reads[Country] = (JsPath \ "name").read[String](Country.apply _)

  implicit val adjacentCountryReads: Reads[String] = (JsPath \ "adjacentCountries").read[String]

  override def parseFile(path: String): Unit = {
    val source: String = Source.fromFile(path).getLines().mkString
    val json: JsValue = Json.parse(source)
    val playingField: JsValue = (json \ "playingField").get

    val result: JsResult[List[Continent]] = playingField.validate[List[Continent]]
    print(result.toString)
  }

}
