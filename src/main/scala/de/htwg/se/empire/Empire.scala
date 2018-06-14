package de.htwg.se.empire

import com.google.inject.{ Guice, Injector }
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.parser.Parser
import scala.io.StdIn.readLine

object Empire {

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val parser: Parser = injector.getInstance(classOf[Parser])
  val playingField: Grid = injector.getInstance(classOf[Grid])

  def main(args: Array[String]): Unit = {
    var input: String = ""
    //TODO: create field
    do {
      input = readLine()
      //TODO: print field and pass input to TUI
    } while (input != "q")
  }

}
