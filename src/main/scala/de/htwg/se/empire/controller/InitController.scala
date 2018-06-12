package de.htwg.se.empire.controller

import de.htwg.se.empire.model.Grid

trait InitController {
  def setUpGrid(pathToGrid: String, players: String*): Option[Grid]

  def randDistributeCountries(playingField: Grid)

  def randDistributeSoldiers(playingField: Grid)
}
