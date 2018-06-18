package de.htwg.se.empire.view

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase

import scala.io.StdIn.readLine

class TUI {

  def processInput(input: String, gameController: GameController): Unit = {
    if (gameController.getCurrentPhase == Phase.IDLE) {
      this.processSetupInput(gameController)
    } else {
      this.processPlayerInput(input, gameController)
    }
  }

  def processSetupInput(gameController: GameController): Unit = {
    var path: String = ""
    var players: String = ""
    println("Enter path to Playingfield.json:")
    path = readLine()
    gameController.setUpPhase(path)
    println("Enter Players (Comma seperated):")
    players = readLine()
    players.trim().split(",").foreach(p => {
      gameController.addPlayer(p)
    })
    gameController.changeToGamePhase()
  }

  def processPlayerInput(input: String, gameController: GameController): Unit = {
    while (gameController.getCurrentPhase == Phase.REINFORCEMENT) {
      var country: String = ""
      var soldiers: Int = 0

      println("Enter country to reinforce: ")
      country = readLine()
      println("Enter amount of soldiers: ")
      soldiers = readLine().toInt
      gameController.distributeSoldiers(soldiers, country)
    }
    input match {
      case "a" =>
        var srcCountry: String = ""
        var targetCountry: String = ""
        var soldiers: Int = 0
        println("Enter from which country to attack: ")
        srcCountry = readLine()
        println("Enter which country to attack: ")
        targetCountry = readLine()
        println("Enter amount of soldiers: ")
        soldiers = readLine().toInt
        gameController.attackCountry(srcCountry, targetCountry, soldiers)
      case "c" =>
        gameController.completeRound()
    }

  }

}
