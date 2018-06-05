package de.htwg.se.empire.util

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player

object GameStatus extends Enumeration {
  type GamePhase = Value
  val IDLE, START, REINFORCEMENT, ATTACK, FIGHT = Value
  var player: Player = _
  var soldiersOnHand: Int = 0
  val playingField: PlayingField = _
}
