package de.htwg.se.empire.controller

import java.io.FileNotFoundException

import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.impl.JsonParser
import org.apache.logging.log4j.{LogManager, Logger}

import scala.util.Random

class InitController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

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
      case _ => None
    }
  }

  def randDistributeSoldiers(playingField: PlayingField): Unit = {
    if (playingField == null) {
      LOG.info("There is no playing field set yet")
      return
    }
    val allCountries = playingField.getAllCountries
    val shuffeldCountries = Random.shuffle(allCountries)
    if (1 <= playingField.players.length) {
      val playerCountries = splitList(shuffeldCountries, playingField.players.length) zip playingField.players
      print(playerCountries)
      for ((cList, p) <- playerCountries) {
        cList.foreach(c => p.addCountry(c))
      }
    } else {
      LOG.info("There are to less players to start the game")
    }
  }

  def splitList[T](l: List[T], pieces: Int, len: Int = -1, done: Int = 0, result: List[List[T]] = Nil): List[List[T]] = {
    if (l.isEmpty) {
      result.reverse
    }
    else {
      val n = if (len < 0) l.length else len
      val ls = l.splitAt((n.toLong * (done + 1) / pieces - n.toLong * done / pieces).toInt)
      splitList(ls._2, pieces, n, done + 1, ls._1 :: result)
    }
  }
}
