package de.htwg.se.empire

import de.htwg.se.empire.parser
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.parser.impl.JsonParser

object Empire {
  def main(args: Array[String]): Unit = {
    val parser: Parser = new JsonParser
    parser.parseFile("playingfield/EmpireData.json")

  }

}
