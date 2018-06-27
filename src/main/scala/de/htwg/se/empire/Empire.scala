package de.htwg.se.empire

import de.htwg.se.empire.controller.impl.DefaultGameController
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.view.TUI

import com.google.inject.{ Guice, Injector }
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.parser.Parser
import scala.io.StdIn.readLine

object Empire {

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val parser: Parser = injector.getInstance(classOf[Parser])
  val playingField: Grid = injector.getInstance(classOf[Grid])

  def main(args: Array[String]): Unit = {
    val tui = new TUI()
    var playingField: PlayingField = null
    val gameController = DefaultGameController(playingField)

    println("Enter '1' to start a new game or 'q' to exit the game:")
    var input: String = ""
    do {
      input = readLine()
      tui.processInput(input, gameController)
    } while (input != "q")
  }

}
