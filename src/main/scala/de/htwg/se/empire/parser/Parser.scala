package de.htwg.se.empire.parser

import de.htwg.se.empire.model.grid.PlayingField

trait Parser {
  def parseFileToPlayingField(path: String): PlayingField
}
