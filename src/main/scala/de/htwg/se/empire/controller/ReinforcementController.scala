package de.htwg.se.empire.controller

import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.model.player.Player

trait ReinforcementController {
  def calcSoldiersToDistribute(playingField: Grid, player: Player): Int

  def distributeSoldiers(playingField: Grid, countryName: String, soldiers: Int)
}
