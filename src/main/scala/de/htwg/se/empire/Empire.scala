package de.htwg.se.empire

import com.google.inject.{ Guice, Injector }
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.parser.Parser

object Empire {

  val injector: Injector = Guice.createInjector(new EmpireModule)
  val parser: Parser = injector.getInstance(classOf[Parser])
  val playingField: Grid = injector.getInstance(classOf[Grid])

  def main(args: Array[String]): Unit = {

  }
}
