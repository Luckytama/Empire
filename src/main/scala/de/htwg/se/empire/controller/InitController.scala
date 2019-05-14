package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.PlayingField

trait InitController {
  def loadGridFromFile(pathToGrid: String, players: String*): PlayingField

  def randDistributeCountries(playingField: PlayingField): PlayingField

  def randDistributeSoldiers(playingField: PlayingField): PlayingField
}
