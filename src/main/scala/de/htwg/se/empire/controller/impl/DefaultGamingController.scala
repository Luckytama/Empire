package de.htwg.se.empire.controller.impl

import de.htwg.se.empire.controller.GamingController
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.util.Phase.{Phase, _}
import org.apache.logging.log4j.{LogManager, Logger}

case class DefaultGamingController(var playingField: PlayingField) extends GamingController {

  val attackController = new DefaultAttackController
  val initController = new DefaultInitController
  val reinforcementController = new DefaultReinforcementController

  var status: Phase = IDLE
  var playerOnTurn: Player = _

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def setUpPhase(pathToGrid: String, players: String*): Unit = {
    status = SETUP
    val playingFieldOpt = initController.setUpGrid(pathToGrid, players: _*)
    if (playingFieldOpt.isDefined) {
      playingField = playingFieldOpt.get
    }
  }

  def addPlayer(players: String*): String = {
    if (status != SETUP) {
      "You can't add new Players at this time of the game"
    } else {
      initController.addPlayers(playingField, players: _*)
      "Players are successfully added."
    }
  }

  def changeToGamePhase(): String = {
    if (checkIfPlayingFieldIsValid()) {
      initController.randDistributeCountries(playingField)
      initController.randDistributeSoldiers(playingField)
      status = REINFORCEMENT
      "Game starts"
    } else {
      "The playing field is not setup correct."
    }
  }

  def changeToReinforcementPhase(): String = {
    if (status == REINFORCEMENT) {
      playerOnTurn = playingField.players.head
      playerOnTurn.handholdSoldiers = reinforcementController.calcSoldiersToDistribute(playingField, playerOnTurn)
      playerOnTurn.name + " is on turn!\nYou have " + playerOnTurn.handholdSoldiers + " soldiers to distribute"
    } else {
      "You are not in the Reinforcement Phase"
    }
  }

  def distributeSoldiers(soldiers: Int, countryName: String): String = {
    if (status == REINFORCEMENT) {
      if (playerOnTurn.handholdSoldiers - soldiers >= 0) {
        reinforcementController.distributeSoldiers(playingField, countryName, soldiers)
        changeToAttackPhase()
      } else {
        "You don't have that much soldiers to distribute"
      }
    } else {
      "You are not in the Reinforcement Phase"
    }
  }

  private def changeToAttackPhase(): String = {
    if (playerOnTurn.handholdSoldiers == 0) {
      status = ATTACK
      "You have successfully distribute all of your soldiers!\nAttack Phase starts"
    } else {
      ""
    }
  }

  private def checkIfPlayingFieldIsValid(): Boolean = {
    if (playingField.getAllCountries.nonEmpty && playingField.players.length >= 2) {
      false
    }
    true
  }
}
