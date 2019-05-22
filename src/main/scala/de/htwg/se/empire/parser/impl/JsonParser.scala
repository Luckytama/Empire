package de.htwg.se.empire.parser.impl

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}

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
    parsePlayingFieldFromJson(playingFieldJson)
  }

  override def parsePlayingFieldFromJson(json: String): PlayingField = {
    parse(json).extract[PlayingField]
  }

  override def parsePlayingFieldToFile(playingField: String): String = {
    try {
      val file = new File("VERY_IMPORTANTE_JSON_EMPIRE_FILE.json")
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(playingField)
      bw.close()
      "Success"
    }
    "Error"
  }

}
