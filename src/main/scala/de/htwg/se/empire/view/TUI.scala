package de.htwg.se.empire.view

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase

import scala.io.StdIn.readLine

case class TUI(gameController: GameController) {

  def processInput(input: String): Unit = {
    if (gameController.getCurrentPhase == Phase.IDLE) {
      this.processSetupInput()
    } else {
      this.processPlayerInput(input)
    }
  }

  def processSetupInput(): Unit = {
    println("Enter path to Playingfield.json:")
    val path = readLine()
    gameController.setUpPhase(path)
    println("Enter Players (Comma seperated):")
    val players = readLine()
    players.trim().split(",").foreach(p => {
      gameController.addPlayer(p)
    })
    gameController.changeToGamePhase()
    this.processPlayerInput("")
  }

  def processPlayerInput(input: String): Unit = {
    input match {
      case "r" =>
        println("Enter country to reinforce: ")
        val country = readLine()
        println("Enter amount of soldiers: ")
        val soldiers = readLine().toInt
        gameController.distributeSoldiers(soldiers, country)
      case "a" =>
        println("Enter from which country to attack: ")
        val srcCountry = readLine()
        println("Enter which country to attack: ")
        val targetCountry = readLine()
        println("Enter amount of soldiers: ")
        val soldiers = readLine().toInt
        gameController.attackCountry(srcCountry, targetCountry, soldiers)
      case "p" =>
        println(gameController.playerOnTurn.toString)
      case "c" =>
        gameController.completeRound()
      case _ =>
        println("Enter 'r' to reinforce, 'a' to attack country, 'p' for player info or 'c' to complete your round.")
    }
  }

}
