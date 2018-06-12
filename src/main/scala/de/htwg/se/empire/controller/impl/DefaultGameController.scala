package de.htwg.se.empire.controller.impl

import com.google.inject.{ Guice, Inject, Injector }
import de.htwg.se.empire.EmpireModule
import de.htwg.se.empire.controller.{ AttackController, GameController, InitController, ReinforcementController }
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.util.Phase.{ Phase, _ }
import org.apache.logging.log4j.{ LogManager, Logger }

case class DefaultGameController @Inject() (var playingField: Grid) extends GameController {

  //TODO: Change println to View.display message
  val injector: Injector = Guice.createInjector(new EmpireModule)
  val attackController: AttackController = injector.getInstance(classOf[AttackController])
  val initController: InitController = injector.getInstance(classOf[InitController])
  val reinforcementController: ReinforcementController = injector.getInstance(classOf[ReinforcementController])

  var status: Phase = IDLE
  var playerOnTurn: Player = _

  val LOG: Logger = LogManager.getLogger(this.getClass)

  override def setUpPhase(pathToGrid: String, players: String*): Unit = {
    status = SETUP
    val playingFieldOpt = initController.setUpGrid(pathToGrid, players: _*)
    if (playingFieldOpt.isDefined) {
      playingField = playingFieldOpt.get
    }
  }

  override def addPlayer(players: String*): Unit = {
    if (status != SETUP) {
      println("You can't add new Players at this time of the game")
    } else {
      initController.addPlayers(playingField, players: _*)
      println("Players are successfully added.")
    }
  }

  override def changeToGamePhase(): Unit = {
    if (checkIfPlayingFieldIsValid()) {
      initController.randDistributeCountries(playingField)
      initController.randDistributeSoldiers(playingField)
      status = REINFORCEMENT
      println("Game starts")
    } else {
      println("The playing field is not setup correct.")
    }
  }

  override def changeToReinforcementPhase(): Unit = {
    if (status == REINFORCEMENT) {
      playerOnTurn = playingField.players.head
      playerOnTurn.handholdSoldiers = reinforcementController.calcSoldiersToDistribute(playingField, playerOnTurn)
      println(playerOnTurn.name + " is on turn!\nYou have " + playerOnTurn.handholdSoldiers + " soldiers to distribute")
    } else {
      println("You are not in the Reinforcement Phase")
    }
  }

  override def distributeSoldiers(soldiers: Int, countryName: String): Unit = {
    if (status == REINFORCEMENT) {
      if (playerOnTurn.handholdSoldiers - soldiers >= 0) {
        reinforcementController.distributeSoldiers(playingField, countryName, soldiers)
        playerOnTurn.handholdSoldiers -= soldiers
        if (playerOnTurn.handholdSoldiers == 0) {
          changeToAttackPhase()
        }
      } else {
        println("You don't have that much soldiers to distribute")
      }
    } else {
      println("You are not in the Reinforcement Phase")
    }
  }

  override def attackCountry(srcCountry: String, targetCountry: String, soldiers: Int): Unit = {
    if (checkIfAttackIsValid(srcCountry, targetCountry, soldiers)) {
      attackController.attackCountry(playingField.getCountry(srcCountry).get, playingField.getCountry(targetCountry).get, soldiers)
      if (playingField.getCountry(targetCountry).get.soldiers == 0) {
        val ownerTargetCountry = playingField.getPlayerForCountry(playingField.getCountry(targetCountry).get).get
        ownerTargetCountry.countries.remove(ownerTargetCountry.countries.indexOf(playingField.getCountry(targetCountry).get))
        playerOnTurn.addCountry(playingField.getCountry(targetCountry).get)
        println("You won how much soldiers do you want to move to " + targetCountry)
      } else {
        println("Defender has defended his country")
      }
    } else {
      println("This is not a valid attack")
    }
  }

  override def completeRound(): Unit = {
    playerOnTurn = getNextPlayer
    status = REINFORCEMENT
    isPlayerDefeated
    changeToReinforcementPhase()
  }

  override def getCurrentPhase: Phase = status

  private def getNextPlayer: Player = {
    val idx = playingField.players.indexOf(playerOnTurn)
    if (idx + 1 == playingField.players.length) playingField.players.head else playingField.players(idx + 1)
  }

  private def isPlayerDefeated: Option[Player] = {
    var defeatedPlayer: Option[Player] = None
    playingField.players.foreach(p => if (p.countries.isEmpty) {
      playingField.players.remove(playingField.players.indexOf(p))
      defeatedPlayer = Some(p)
      println(p.name + " is defeated")
    })
    if (playingField.players.length == 1) {
      println(playingField.players.head + " won the game!!!!")
      status = IDLE
    }
    defeatedPlayer
  }

  private def checkIfAttackIsValid(srcCountry: String, targetCountry: String, soldiers: Int): Boolean = {
    val src = playingField.getCountry(srcCountry)
    val target = playingField.getCountry(targetCountry)
    if ((src.isDefined && target.isDefined)
      && src.get.adjacentCountries.contains(target.get.name)
      && (src.get.soldiers > soldiers) && (playerOnTurn.countries.contains(src.get)
        && !playerOnTurn.countries.contains(target.get))) {
      true
    } else {
      false
    }
  }

  private def changeToAttackPhase(): Unit = {
    if (playerOnTurn.handholdSoldiers == 0) {
      status = ATTACK
      println("You have successfully distribute all of your soldiers!\nAttack Phase starts")
    } else {
      ""
    }
  }

  private def checkIfPlayingFieldIsValid(): Boolean = if (playingField.getAllCountries.nonEmpty && playingField.players.length >= 2) true else false
}
