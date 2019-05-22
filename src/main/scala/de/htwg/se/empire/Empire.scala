package de.htwg.se.empire

import com.google.inject.{Guice, Injector}
import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.view.TUI
import de.htwg.se.empire.view.gui.SwingGui
import de.htwg.se.empire.view.rest.RestApi

import scala.io.StdIn.readLine

object Empire {

  val REST_PORT: Int = 8888
  val injector: Injector = Guice.createInjector(new EmpireModule)
  val parser: Parser = injector.getInstance(classOf[Parser])
  val gameController: GameController = injector.getInstance(classOf[GameController])

  def main(args: Array[String]): Unit = {
    val gui = new SwingGui(gameController)
    val restApi = new RestApi(gameController)

    restApi.startRestApi(REST_PORT)

  }

  def processTUIInput(): Unit = {
    val tui = new TUI(gameController)

    println("Enter '1' to start a new game or 'q' to exit the game:")
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input)
    } while (input != "q")
  }

}
