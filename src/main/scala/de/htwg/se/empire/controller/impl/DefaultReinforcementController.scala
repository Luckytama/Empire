package de.htwg.se.empire.controller.impl

import de.htwg.se.empire.controller.ReinforcementController
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import org.apache.logging.log4j.{LogManager, Logger}

class DefaultReinforcementController extends ReinforcementController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  /*
   * Player gets one soldier for every 3 countries he have, but minimum 3
   */
  //TODO: savely remove playingfield from method
  def calcSoldiersToDistribute(player: Player): Int = {
    if (player.countries.length / 3 > 3) player.countries.length / 3 else 3
  }

  def distributeSoldiers(playingField: PlayingField, countryName: String, soldiers: Int): Unit = {
    val country = playingField.getCountry(countryName)
    if (country.isDefined) country.get.addSoldiers(soldiers) //else inform view
  }
}
