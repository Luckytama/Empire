package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player

trait ReinforcementController {
  def calcSoldiersToDistribute(player: Player): Int

  def distributeSoldiers(playingField: PlayingField, countryName: String, soldiers: Int): Unit
}
