package de.htwg.se.empire.parser

import de.htwg.se.empire.model.grid.PlayingField

trait Parser {
  def parseFile(path: String): PlayingField
}
