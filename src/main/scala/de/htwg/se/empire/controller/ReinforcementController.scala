package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.{ Country, PlayingField }
import org.apache.logging.log4j.{ LogManager, Logger }

class ReinforcementController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  /*
   * Player gets one soldier for every 3 countries he have, but minimum 3
   */
  def calcSoldiersToDistribute(playingField: PlayingField, playerName: String): Int = {
    if (playingField.getPlayer(playerName).isEmpty) {

    } // inform view
    val player = playingField.getPlayer(playerName).get
    if (player.countries.length / 3 < 3) player.countries.length / 3 else 3
  }

  def distributeSoldiers(playingField: PlayingField, countryName: String, soldiers: Int): Unit = {
    val country = playingField.getCountry(countryName)
    if (country.isDefined) country.get.addSoldiers(soldiers) //else inform view
  }
}
