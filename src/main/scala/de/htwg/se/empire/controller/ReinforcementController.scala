package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.PlayingField

trait ReinforcementController {
  def calcSoldiersToDistribute(playingField: PlayingField, playerName: String)

  def distributeSoldiers(playingField: PlayingField, countryName: String, soldiers: Int)
}
