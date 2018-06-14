package de.htwg.se.empire.controller

import de.htwg.se.empire.model.Grid

trait ReinforcementController {
  def calcSoldiersToDistribute(playingField: Grid, playerName: String): Int

  def distributeSoldiers(playingField: Grid, countryName: String, soldiers: Int)
}
