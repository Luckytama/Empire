package de.htwg.se.empire.controller

import de.htwg.se.empire.controller.impl.DefaultGameController
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.util.Phase._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class DefaultGameControllerTest extends WordSpec with Matchers {

  "The Game Controller" when {
    "new" should {
      val playingField = new PlayingField(null)
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
    /*"player wants to distribute soldiers in phase REINFORCEMENT" should {
      val playingField: PlayingField = null
      val gameController = DefaultGameController(playingField)
      gameController.setUpPhase("playingField/EmpireData.json", "Hans", "Markus")
      gameController.changeToGamePhase()
      gameController.changeToReinforcementPhase()
      val country = gameController.playingField.getPlayer("Hans").get.countries.head.name
      val amountOfSoldiersBefore = gameController.playingField.getCountry(country).get.soldiers
      val handholdSoldiers = gameController.playerOnTurn.handholdSoldiers
      gameController.distributeSoldiers(3, country)
      "have now more soldiers on a country" in {
        amountOfSoldiersBefore + 3 should be(gameController.playingField.getCountry(country).get.soldiers)
      }
      "are always in same phase" in {
        gameController.status should be(REINFORCEMENT)
      }
      "change to attack phase when handhold soldiers are empty" in {
        gameController.distributeSoldiers(gameController.playerOnTurn.handholdSoldiers, country)
        gameController.status should be(ATTACK)
      }
      "Not enough handhold soldiers" should {
        "inform the View that the player have not enough handhold soldiers" in {
          gameController.distributeSoldiers(3, country)
          // inform view
        }
      }
    }*/
  }
}
