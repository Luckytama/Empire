package de.htwg.se.empire.controller.impl

import de.htwg.se.empire.controller.InitController
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.impl.JsonParser
import org.apache.logging.log4j.{LogManager, Logger}

import scala.util.Random

class DefaultInitController extends InitController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  val INIT_SOLDIERS_5PLAYER = 25
  val INIT_SOLDIERS_4PLAYER = 30
  val INIT_SOLDIERS_3PLAYER = 35
  val INIT_SOLDIERS_2PLAYER = 40

  val INIT_VALUE_SOLDIERS_PER_COUNTRY = 1

  def loadGridFromFile(pathToGrid: String, players: String*): PlayingField = {
    val parser = new JsonParser
    val playingField = parser.parseFileToPlayingField(pathToGrid).addPlayers(players: _*)
    playingField
  }

  /*
   * Distribute randomly all countries to all player with one soldiers in it
   */
  def randDistributeCountries(playingField: PlayingField): PlayingField = {
    var updatedPlayingField = playingField
    if (playingField.players.length < 2) {
      LOG.debug("There are to less players to start the game")
    } else {
      val allCountries = playingField.getAllCountries
      val playerCountries = splitList(Random.shuffle(allCountries), playingField.players.length) zip playingField.players
      for ((countries, player) <- playerCountries) {
        for (country <- countries) {
          updatedPlayingField = updatedPlayingField.addCountryToPlayer(player, country)
        }
      }
    }
    updatedPlayingField
  }

  /*
   * Distribute soldiers
   * 5 Players: 25, 4 Players: 30, 3 Players: 35, 2 Players: 40
   */
  def randDistributeSoldiers(playingField: PlayingField): PlayingField = {
    playingField.players.length match {
      case 5 =>
        distribute(playingField, INIT_SOLDIERS_5PLAYER)
      case 4 =>
        distribute(playingField, INIT_SOLDIERS_4PLAYER)
      case 3 =>
        distribute(playingField, INIT_SOLDIERS_3PLAYER)
      case 2 =>
        distribute(playingField, INIT_SOLDIERS_2PLAYER)
      case _ =>
        LOG.error("There are not a valid number of players")
        playingField
    }
  }

  private def distribute(playingField: PlayingField, soldiers: Int): PlayingField = {
    var updatedPlayingField = playingField
    for (country <- updatedPlayingField.getAllCountries) {
      updatedPlayingField = updatedPlayingField.addSoldiersToCountry(country, INIT_VALUE_SOLDIERS_PER_COUNTRY)
    }
    for (player <- playingField.players) {
      updatedPlayingField = distributeSoldierToRandCountry(updatedPlayingField, player, soldiers - playingField.getNumberOfAllSoldiers(player))
    }
    updatedPlayingField
  }

  private def distributeSoldierToRandCountry(playingField: PlayingField, player: Player, soldiers: Int): PlayingField = {
    if (player.countries.isEmpty) {
      LOG.error("There are no countries set for player ", player.name)
      playingField
    } else if (soldiers != 0) {
      val randomCountry = playingField.getCountriesForPlayer(player)(Random.nextInt(player.countries.length))
      val updatedPlayingField = playingField.addSoldiersToCountry(randomCountry, 1)
      distributeSoldierToRandCountry(updatedPlayingField, player, soldiers - 1)
    } else {
      playingField
    }
  }

  private def splitList[T](l: List[T], pieces: Int, len: Int = -1, done: Int = 0, result: List[List[T]] = Nil): List[List[T]] = {
    if (l.isEmpty) {
      result.reverse
    } else {
      val n = if (len < 0) l.length else len
      val ls = l.splitAt((n.toLong * (done + 1) / pieces - n.toLong * done / pieces).toInt)
      splitList(ls._2, pieces, n, done + 1, ls._1 :: result)
    }
  }
}
