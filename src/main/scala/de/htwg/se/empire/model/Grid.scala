package de.htwg.se.empire.model

import de.htwg.se.empire.model.grid.Country
import de.htwg.se.empire.model.player.Player

import scala.collection.mutable.ListBuffer

trait Grid {

  var players: ListBuffer[Player]

  def addPlayer(player: Player): Unit

  def removePlayer(player: Player): Unit

  def getAllCountries: List[Country]

  def getPlayerForCountry(country: Country): Option[Player]

  def getPlayer(playerName: String): Option[Player]

  def getCountry(countryName: String): Option[Country]
}
