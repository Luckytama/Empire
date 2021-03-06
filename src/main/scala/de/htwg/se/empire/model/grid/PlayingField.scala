package de.htwg.se.empire.model.grid

import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.model.player.Player
import org.apache.logging.log4j.{ LogManager, Logger }

import scala.collection.mutable.ListBuffer

case class PlayingField(continents: List[Continent]) extends Grid {

  def this() = this(List.empty[Continent])

  var players: ListBuffer[Player] = ListBuffer()

  val LOG: Logger = LogManager.getLogger(this.getClass)

  override def addPlayer(player: Player): Unit = {
    if (5 >= players.size) {
      players.append(player)
    } else {
      LOG.info("There can't be more than 5 players.")
    }
  }

  override def removePlayer(player: Player): Unit = {
    if (players.contains(player)) {
      players.remove(players.indexOf(player))
    } else {
      LOG.info("Cannot find Player")
    }
  }

  override def getAllCountries: List[Country] = {
    var countries = new ListBuffer[Country]
    continents.foreach(c => countries ++= c.countries)
    countries.toList
  }

  override def getPlayerForCountry(country: Country): Option[Player] = {
    var playerOpt: Option[Player] = None
    players.foreach(p => if (p.countries.contains(country)) playerOpt = Some(p))
    playerOpt
  }

  override def getPlayer(playerName: String): Option[Player] = {
    val p = players find (_.name == playerName)
    if (p.isDefined) {
      p
    } else {
      LOG.info("Player not found with name ", playerName)
      None
    }
  }

  override def getCountry(countryName: String): Option[Country] = {
    val c = getAllCountries find (_.name == countryName)
    if (c.isDefined) {
      c
    } else {
      LOG.info("Country not found with ", countryName)
      None
    }
  }

  override def toString: String = {
    var output = new StringBuilder
    if (players.nonEmpty) {
      output.append("Players: " + players.mkString + "\n")
    }
    output.append("Continents: " + continents.mkString)
    output.toString()
  }
}
