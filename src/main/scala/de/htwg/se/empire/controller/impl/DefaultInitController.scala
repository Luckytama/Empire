package de.htwg.se.empire.controller.impl

import java.io.FileNotFoundException

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.impl.JsonParser
import org.apache.logging.log4j.{LogManager, Logger}

import scala.util.Random

class DefaultInitController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  val INIT_SOLDIERS_5PLAYER = 25
  val INIT_SOLDIERS_4PLAYER = 30
  val INIT_SOLDIERS_3PLAYER = 35
  val INIT_SOLDIERS_2PLAYER = 40

  val INIT_VALUE_SOLDIERS_PER_COUNTRY = 1

  def setUpGrid(pathToGrid: String, players: String*): Option[PlayingField] = {
    val parser = new JsonParser
    try {
      val playingField = parser.parseFileToPlayingField(pathToGrid)
      players.foreach(p => playingField.addPlayer(Player(p)))
      Some.apply(playingField)
    } catch {
      case fnfe: FileNotFoundException =>
        LOG.info("Can't find file with path ", pathToGrid)
        None
      case _: Throwable =>
        LOG.error("Unhandled exception")
        None
    }
  }

  /*
   * Distribute randomly all countries to all player with one soldiers in it
   */
  def randDistributeCountries(playingField: PlayingField): Unit = {
    if (playingField == null) {
      LOG.info("There is no playing field set yet")
      return
    }
    val allCountries = playingField.getAllCountries
    if (1 <= playingField.players.length) {
      val playerCountries = splitList(Random.shuffle(allCountries), playingField.players.length) zip playingField.players
      for ((cList, p) <- playerCountries) {
        for (c <- cList) {
          c.addSoldiers(INIT_VALUE_SOLDIERS_PER_COUNTRY)
          p.addCountry(c)
        }
      }
    } else {
      LOG.info("There are to less players to start the game")
    }
  }

  /*
   * Distribute soldiers
   * 5 Players: 25, 4 Players: 30, 3 Players: 35, 2 Players: 40
   */
  def randDistributeSoldiers(playingField: PlayingField): Unit = {
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
        LOG.info("There are not a valid number of players")
    }
  }

  private def distribute(playingField: PlayingField, soldiers: Int): Unit = {
    playingField.players.foreach(p => distributeSoldierToRandCountry(p, soldiers - p.getNumberOfAllSoldiers))
  }

  private def distributeSoldierToRandCountry(player: Player, soldiers: Int): Unit = {
    if (player.countries.isEmpty) {
      LOG.info("There are no countries set for player ", player.name)
      None
    } else if (soldiers != 0) {
      player.countries(Random.nextInt(player.countries.length + 1)).addSoldiers(1)
      distributeSoldierToRandCountry(player, soldiers - 1)
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
