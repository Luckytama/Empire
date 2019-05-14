package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.util.Phase.Phase

import scala.swing.Publisher

trait GameController extends Publisher {

  var playingField: PlayingField

  def status: Phase

  def setUpPhase(pathToGrid: String, players: String*): Unit

  def addPlayer(players: String*): Unit

  def changeToGamePhase(): Unit

  def changeToReinforcementPhase(): Unit

  def distributeSoldiers(soldiers: Int, countryName: String): Int

  def attackCountry(srcCountry: String, targetCountry: String, soldiers: Int): String

  def completeRound(): Unit

  def getCurrentPhase: Phase

  def getAttackableCountries(country: String): List[String]

  def getPlayerOnTurn: Player

  def setPlayerOnTurn(player: Player): Unit
}
