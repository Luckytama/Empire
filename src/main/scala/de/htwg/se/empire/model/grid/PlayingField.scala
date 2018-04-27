package de.htwg.se.empire.model.grid

import com.sun.deploy.util.StringUtils
import de.htwg.se.empire.model.player.Player
import org.apache.logging.log4j.{ LogManager, Logger }

import scala.collection.mutable.ListBuffer

case class PlayingField(continents: List[Continent]) {

  var players: ListBuffer[Player] = ListBuffer()

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addPlayer(player: Player): Unit = {
    if (5 >= players.size) {
      players.append(player)
    } else {
      LOG.info("There can't be more than 5 players.")
    }
  }

  def removePlayer(player: Player): Unit = {
    if (players.contains(player)) {
      players.remove(players.indexOf(player))
    } else {
      LOG.info("Cannot find Player")
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
