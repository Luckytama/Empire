package de.htwg.se.empire.controller

import de.htwg.se.empire.controller.impl.DefaultGameController
import de.htwg.se.empire.model.grid.{ Continent, Country, PlayingField }
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.util.Phase
import de.htwg.se.empire.util.Phase._
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class DefaultGameControllerTest extends WordSpec with Matchers {

  "The Game Controller" when {
    "new" should {
      val playingField = PlayingField(null)
      val gameController = DefaultGameController(playingField)
      "have a empty Playing Field" in {
        gameController.playingField should be(playingField)
      }
      "have the Status IDLE" in {
        gameController.status should be(IDLE)
      }
      "cannot add some Players" in {
        gameController.addPlayer("Hans", "Markus")
        // inform view
      }
    }
    "when setup without player" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingfield/EmpireData.json")
      "have a non empty Playing Field" in {
        gameController.playingField.continents should not be (null)
      }
      "have no players" in {
        gameController.playingField.players.isEmpty should be(true)
      }
      "have the status SETUP" in {
        gameController.getCurrentPhase should be(SETUP)
      }
      "can add some player" in {
        gameController.addPlayer("Hans", "Markus")
        gameController.playingField.players.length should be(2)
      }
    }
    "when setup with players" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingfield/EmpireData.json", "Hans", "Markus")
      "have a non empty Playing Field" in {
        gameController.playingField.continents should not be (null)
      }
      "have set some players" in {
        gameController.playingField.players.length should be(2)
      }
      "have the status SETUP" in {
        gameController.getCurrentPhase should be(SETUP)
      }
    }
    "try to change to Game Phase with valid Playing Field" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingfield/EmpireData.json", "Hans", "Markus")
      gameController.changeToGamePhase()
      "correctly distribute countries" in {
        gameController.playingField.getPlayer("Hans").get.getCountryAmount should not be (0)
      }
      "correctly distribute soldiers" in {
        gameController.playingField.getPlayer("Hans").get.getNumberOfAllSoldiers should not be (0)
      }
      "be in REINFORCEMENT Status" in {
        gameController.getCurrentPhase should be(REINFORCEMENT)
      }
    }
    "try to change to Game Phase with invalid Playing Field" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingfield/EmpireData.json")
      gameController.changeToGamePhase()
      "inform View that the Playing Field is not valid" in {
        // To be implemented
      }
      "be not in REINFORCEMENT status" in {
        gameController.getCurrentPhase should not be (REINFORCEMENT)
      }
    }
    "try to change to Reinforcement Phase with status REINFORCEMENT" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingfield/EmpireData.json", "Hans", "Markus")
      gameController.changeToGamePhase()
      gameController.changeToReinforcementPhase()
      "set a player who's on turn" in {
        gameController.playerOnTurn.name should be("Hans")
      }
      "give the player handhold soldiers" in {
        gameController.playerOnTurn.handholdSoldiers should not be (0)
      }
    }
    "try to change to Reinforcement Phase without status REINFORCEMENT" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.changeToReinforcementPhase()
      "inform View that he cannot change to REINFORCEMENT Phase" in {
        // To be implemented after a view exists
      }
    }
    "a Player wants to distribute soldiers in another game phase" should {
      val gameController = new DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("Country", null))))))
      gameController.status = Phase.IDLE
      gameController.distributeSoldiers(5, "Country")
      "not change the amout of soldiers on a country" in {
        gameController.playingField.getCountry("Country").get.soldiers should be(0)
      }
    }
    "a Player wants to distribute less soldiers that he have" should {
      val gameController = new DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("Country", null))))))
      gameController.status = Phase.REINFORCEMENT
      val player = Player("Hans")
      gameController.playerOnTurn = player
      gameController.distributeSoldiers(5, "Country")
      "not infect the amount of soldiers on an country" in {
        gameController.playingField.getCountry("Country").get.soldiers should be(0)
      }
      "not infect the amount of handhold soldiers" in {
        gameController.playerOnTurn.handholdSoldiers should be(0)
      }
    }
    "a Player wants to distribute soldiers" should {
      val gameController = DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("Country", null))))))
      gameController.status = Phase.REINFORCEMENT
      val player = Player("Hans")
      gameController.playerOnTurn = player
      player.handholdSoldiers = 5
      gameController.distributeSoldiers(5, "Country")
      "have less soldiers on hand" in {
        player.handholdSoldiers should be(0)
      }
      "infect the amount of soldiers on the country" in {
        gameController.playingField.getCountry("Country").get.soldiers should be(5)
      }
      "change to Attack Phase after all soldiers are distributed" in {
        gameController.status should be(Phase.ATTACK)
      }
    }
    "a Player wants to attack another country with an invalid attack" should {
      val gameController = DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("src", List("target")), Country("target", List("src")), Country("x", List("y")))))))
      val player = Player("Hans")
      player.handholdSoldiers = 5
      gameController.playerOnTurn = player
      "not perform the attack with invalid countrie names" in {
        gameController.attackCountry("x", "y", 0)
      }
      "not perform the attack when countries are not connected" in {
        gameController.attackCountry("src", "x", 0)
      }
      "not perform the attack when performed with more soldiers than handhold" in {
        gameController.attackCountry("src", "target", 5)
      }
      "not perform the attack when target country is owned by attacker" in {
        player.addCountry(gameController.playingField.getCountry("target").get)
        gameController.attackCountry("src", "target", 2)
      }
    }
    "a Player wants to attack another country with a valid attack" should {
      val gameController = DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("src", List("target")), Country("target", List("src")), Country("x", List("y")))))))
      val player = Player("Hans")
      player.handholdSoldiers = 5
      player.addCountry(gameController.playingField.getCountry("src").get)
      gameController.playingField.getCountry("src").get.soldiers = 10
      gameController.playingField.getCountry("target").get.soldiers = 10
      gameController.playerOnTurn = player
      "no Exception is thrown" in {
        gameController.attackCountry("src", "target", 2)
      }
    }
    "a Player wants to end his round after defeated the other player" should {
      val gameController = DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("src", null))))))
      gameController.playingField.players.append(Player("Hans"), Player("Markus"))
      gameController.playingField.getPlayer("Hans").get.addCountry(gameController.playingField.getCountry("src").get)
      gameController.playerOnTurn = gameController.playingField.getPlayer("Hans").get
      gameController.completeRound()
      "the game status should be FINISH" in {
        gameController.status should be(Phase.FINISH)
      }
    }
    "a Player wants to end his round" should {
      val gameController = DefaultGameController(PlayingField(List(Continent("Continent", 5, List(Country("src", null), Country("target", null))))))
      gameController.playingField.players.append(Player("Hans"), Player("Markus"))
      gameController.playingField.getPlayer("Hans").get.addCountry(gameController.playingField.getCountry("src").get)
      gameController.playingField.getPlayer("Markus").get.addCountry(gameController.playingField.getCountry("target").get)
      gameController.playerOnTurn = gameController.playingField.getPlayer("Hans").get
      gameController.completeRound()
      "the game status should be REINFORCEMENT" in {
        gameController.status should be(Phase.REINFORCEMENT)
      }
      "the next player should be on turn" in {
        gameController.playerOnTurn should be(gameController.playingField.getPlayer("Markus").get)
      }
    }
  }
}
