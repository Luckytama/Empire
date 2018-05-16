package de.htwg.se.empire.controller

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class InitControllerTest extends WordSpec with Matchers {
  "A the Init Controller" when {
    "set up without players and with wrong path to file" should {
      val initController = new InitController
      "handle wrong path" in {
        initController.setUpGrid("/wrongPath") should be(None)
      }
    }
    "set up without players and with real path to file" should {
      val initController = new InitController
      val playingField = initController.setUpGrid("playingfield/EmpireData.json")
      "return a playing field without player" in {
        playingField.isDefined should be(true)
        playingField.get.players.length should be(0)
      }
      "not distributing countries because to less players" in {
        initController.randDistributeSoldiers(playingField.get)
        playingField.equals(playingField) should be(true)
      }
    }
    "set up with players and with real path to file" should {
      val initController = new InitController
      val playingField = initController.setUpGrid("playingfield/EmpireData.json", "Hans", "Jürgen", "Karl")
      "return ap playing field with players" in {
        playingField.isDefined should be(true)
        playingField.get.players.length should be(3)
      }
      "distribute countries fair to players" in {
        initController.randDistributeSoldiers(playingField.get)
        playingField.get.players.head.countries.length should be(14)
        playingField.get.players(1).countries.length should be(14)
        playingField.get.players(2).countries.length should be(14)
      }
    }
  }
}