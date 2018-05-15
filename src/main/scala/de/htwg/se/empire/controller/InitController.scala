package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.{Country, PlayingField}
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.impl.JsonParser
import org.apache.logging.log4j.{LogManager, Logger}

import scala.util.Random

class InitController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def setUpGrid(pathToGrid: String, players: String*): PlayingField = {
    val parser = new JsonParser
    val playingField = parser.parseFileToPlayingField(pathToGrid)
    players.foreach(p => playingField.addPlayer(Player(p)))
    playingField
  }

  def randDistributeSoldiers(playingField: PlayingField): Unit = {
    val allCountries = playingField.getAllCountries
    val shuffeldCountries = Random.shuffle(allCountries)
    if (1 <= playingField.players.length) {
      val playerCountries = shuffeldCountries.grouped(shuffeldCountries.length / playingField.players.length).toList zip playingField.players
      print(playerCountries)
      for ((cList, p) <- playerCountries) {
        cList.foreach(c => p.addCountry(c))
      }
    } else {
      LOG.info("There are to less players to start the game")
      None
    }
  }

  def split_list(countryList: List[Country], numberOfPlayer: Int) = {
    //countryList.span()
  }
}
