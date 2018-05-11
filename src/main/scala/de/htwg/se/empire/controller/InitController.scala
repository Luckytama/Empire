package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.impl.JsonParser

class InitController {
  def setUpGrid(pathToGrid: String, players: String*): PlayingField = {
    val parser = new JsonParser
    val playingField = parser.parseFile(pathToGrid)
    players.foreach(p => playingField.addPlayer(Player(p)))
    playingField
  }

}
