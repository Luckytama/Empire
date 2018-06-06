package de.htwg.se.empire.controller

import de.htwg.se.empire.util.Phase.Phase

trait GameController {

  def setUpPhase(pathToGrid: String, players: String*): Unit

  def addPlayer(players: String*): Unit

  def changeToGamePhase(): Unit

  def changeToReinforcementPhase(): Unit

  def distributeSoldiers(soldiers: Int, countryName: String): Unit

  def attackCountry(srcCountry: String, targetCountry: String, soldiers: Int): Unit

  def completeRound(): Unit

  def getCurrentPhase: Phase
}
