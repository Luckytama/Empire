package de.htwg.se.empire.controller

import de.htwg.se.empire.controller.impl.DefaultReinforcementController
import de.htwg.se.empire.model.grid.{Continent, Country, PlayingField}
import de.htwg.se.empire.model.player.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class DefaultReinforcementControllerTest extends WordSpec with Matchers {
  "The Reinforcement Controller" when {
    val reinforcementController = new DefaultReinforcementController
    val player = Player("Hans")
    val sampleCountry = Country("Deutschland", null)
    val playingField = PlayingField(List(Continent("Europe", 5, List(sampleCountry))))
    player.addCountry(sampleCountry)
    "calculate minimum Soldiers to distribute" in {
      reinforcementController.calcSoldiersToDistribute(playingField, player) should be(8)
    }
    "calculate correct amount of Soldiers to distribute" in {
      player.addCountry(Country("Italy", null))
      player.addCountry(Country("Spain", null))
      player.addCountry(Country("Greek", null))
      player.addCountry(Country("Swiss", null))
      player.addCountry(Country("Great Britain", null))
      player.addCountry(Country("New Zealand", null))
      player.addCountry(Country("Brasil", null))
      player.addCountry(Country("Marokko", null))
      player.addCountry(Country("Agypt", null))
      player.addCountry(Country("Denmark", null))
      player.addCountry(Country("Netherlands", null))

      reinforcementController.calcSoldiersToDistribute(playingField, player) should be(4)
    }
    "distribute soldiers" in {
      reinforcementController.distributeSoldiers(playingField, sampleCountry.name, 5)
      sampleCountry.soldiers should be(5)
    }
  }
}