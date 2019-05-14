package de.htwg.se.empire.controller.impl

import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.empire.EmpireModule
import de.htwg.se.empire.controller.{AttackController, GameController, InitController, ReinforcementController}
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.model.player.Player
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.util.Phase.{Phase, _}
import org.apache.logging.log4j.{LogManager, Logger}

case class DefaultGameController @Inject() (var playingField: PlayingField) extends GameController {

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val attackController: AttackController = injector.getInstance(classOf[AttackController])
  val initController: InitController = injector.getInstance(classOf[InitController])
  val reinforcementController: ReinforcementController = injector.getInstance(classOf[ReinforcementController])
  val parser: Parser = injector.getInstance(classOf[Parser])

  var status: Phase = SETUP

  val LOG: Logger = LogManager.getLogger(this.getClass)

  override def setUpPhase(pathToGrid: String, players: String*): Unit = {
    status = SETUP
    this.playingField = initController.loadGridFromFile(pathToGrid, players: _*)
  }

  override def addPlayer(players: String*): Unit = {
    if (status != SETUP) {
      LOG.error("You can't add new Players at this time of the game")
    } else {
      this.playingField = playingField.addPlayers(players: _*)
      LOG.info("Players are successfully added.")
    }
  }

  override def changeToGamePhase(): Unit = {
    if (checkIfPlayingFieldIsValid()) {
      this.playingField = initController.randDistributeCountries(this.playingField)
      this.playingField = initController.randDistributeSoldiers(this.playingField)
      this.playingField = this.playingField.copy(playerOnTurn = this.playingField.players.head.name)
      print(this.playingField)
      status = REINFORCEMENT
      publish(new PhaseChanged)
      changeToReinforcementPhase()
      println("Game starts")
    } else {
      println("The playing field is not setup correct.")
    }
  }

  override def changeToReinforcementPhase(): Unit = {
    if (status == REINFORCEMENT) {
      this.playingField = playingField.distributeHandholdSoldiers(this.playingField.getPlayerOnTurn.get, reinforcementController.calcSoldiersToDistribute(this.playingField, this.playingField.getPlayerOnTurn.get))
    } else {
      println("You are not in the Reinforcement Phase")
    }
  }

  override def distributeSoldiers(soldiers: Int, countryName: String): Int = {
    if (status == REINFORCEMENT) {
      if (this.playingField.getPlayerOnTurn.get.handholdSoldiers - soldiers >= 0) {
        this.playingField = reinforcementController.distributeSoldiers(this.playingField, countryName, soldiers)
        this.playingField = this.playingField.updatePlayer(this.playingField.getPlayerOnTurn.get.putSoldiers(soldiers))
        if (this.playingField.getPlayerOnTurn.get.handholdSoldiers == 0) {
          changeToAttackPhase()
        }
        soldiers
      } else {
        LOG.info("You don't have that much soldiers to distribute")
        0
      }
    } else {
      LOG.info("You are not in the Reinforcement Phase")
      0
    }
  }

  override def attackCountry(srcCountryName: String, targetCountryName: String, soldiers: Int): String = {
    if (checkIfAttackIsValid(srcCountryName, targetCountryName, soldiers)) {
      val srcCountry = this.playingField.getCountry(srcCountryName).get
      val targetCountry = this.playingField.getCountry(targetCountryName).get
      val (src, target) = attackController.attackCountry(srcCountry, playingField.getCountry(targetCountryName).get, soldiers)
      this.playingField = this.playingField.updateCountry(srcCountry, src)
      this.playingField = this.playingField.updateCountry(targetCountry, target)
      if (playingField.getCountry(targetCountryName).get.soldiers == 0) {
        val ownerTargetCountry = playingField.getPlayerForCountry(playingField.getCountry(targetCountryName).get).get
        this.playingField = this.playingField.removeCountryFromPlayer(ownerTargetCountry, this.playingField.getCountry(targetCountryName).get)
        this.playingField = this.playingField.addCountryToPlayer(this.playingField.getPlayerOnTurn.get, this.playingField.getCountry(targetCountryName).get)
        this.playingField = this.playingField.moveSoldiers(playingField.getCountry(srcCountryName).get, playingField.getCountry(targetCountryName).get, playingField.getCountry(srcCountryName).get.soldiers / 2)
        "You win the battle and gain the country"
      } else {
        "The Country was defended"
      }
    } else {
      "This is not a valid attack"
    }
  }

  override def completeRound(): Unit = {
    val defeatedPlayerOpt = isPlayerDefeated
    if (defeatedPlayerOpt.isDefined) {
      println(defeatedPlayerOpt.get.name + " is defeated")
    } else {
      this.playingField = nextPlayer
      status = REINFORCEMENT
      publish(new PhaseChanged)
      changeToReinforcementPhase()
    }
  }

  override def getCurrentPhase: Phase = status

  override def getAttackableCountries(country: String): List[String] = {
    if (country != "") {
      playingField.getCountry(country).get.adjacentCountries.filter(!this.playingField.getPlayerOnTurn.get.containsCountry(_))
    } else {
      var dm = List[String]()
      dm
    }
  }

  override def getPlayerOnTurn: Player = this.playingField.getPlayerOnTurn.get

  override def setPlayerOnTurn(player: Player): Unit = this.playingField = this.playingField.copy(playerOnTurn = player.name)

  private def nextPlayer: PlayingField = {
    val idx = playingField.players.indexOf(this.playingField.getPlayerOnTurn.get)
    if (idx + 1 == playingField.players.length) this.playingField.copy(playerOnTurn = playingField.players.head.name) else this.playingField.copy(playerOnTurn = playingField.players(idx + 1).name)
  }

  private def isPlayerDefeated: Option[Player] = {
    var defeatedPlayer: Option[Player] = None
    playingField.players.foreach(p => if (p.countries.isEmpty) {
      this.playingField = playingField.removePlayer(p)
      defeatedPlayer = Some(p)
    })
    if (playingField.players.length == 1) {
      println(playingField.players.head + " won the game!!!!")
      status = FINISH
    }
    defeatedPlayer
  }

  private def checkIfAttackIsValid(srcCountry: String, targetCountry: String, soldiers: Int): Boolean = {
    val src = playingField.getCountry(srcCountry)
    val target = playingField.getCountry(targetCountry)
    (src.isDefined && target.isDefined) && src.get.adjacentCountries.contains(target.get.name) && (src.get.soldiers > soldiers) && (this.playingField.getPlayerOnTurn.get.countries.contains(src.get.name) && !this.playingField.getPlayerOnTurn.get.countries.contains(target.get.name))
  }

  private def changeToAttackPhase(): Unit = {
    if (this.playingField.getPlayerOnTurn.get.handholdSoldiers == 0) {
      status = ATTACK
      publish(new PhaseChanged)
      LOG.info("You have successfully distribute all of your soldiers!\nAttack Phase starts")
    }
  }

  private def checkIfPlayingFieldIsValid(): Boolean = {
    playingField.getAllCountries.nonEmpty && playingField.players.length >= 2
  }
}
