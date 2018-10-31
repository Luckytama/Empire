package de.htwg.se.empire.view

import com.google.inject.{Guice, Injector}
import de.htwg.se.empire.EmpireModule
import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.view.TUI

case class WebTecTUI() {

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val parser: Parser = injector.getInstance(classOf[Parser])
  val playingField: Grid = injector.getInstance(classOf[Grid])
  val gameController: GameController = injector.getInstance(classOf[GameController])

  def processPlayerInput(input: String): String = {
    input match {
      case "r" =>
        println("Enter country to reinforce: ")
        val country = readLine()
        println("Enter amount of soldiers: ")
        val soldiers = readLine().toInt
        gameController.distributeSoldiers(soldiers, country)
        "done"
      case "a" =>
        "Enter from which country to attack: "
        val srcCountry = readLine()
        println("Enter which country to attack: ")
        val targetCountry = readLine()
        println("Enter amount of soldiers: ")
        val soldiers = readLine().toInt
        gameController.attackCountry(srcCountry, targetCountry, soldiers)
        "done"
      case "p" =>
        gameController.playerOnTurn.toString
      case "c" =>
        gameController.completeRound()
        "done"
      case _ =>
        "Enter 'r' to reinforce, 'a' to attack country, 'p' for player info or 'c' to complete your round."
    }
  }
}