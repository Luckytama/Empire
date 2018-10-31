package de.htwg.se.empire

import de.htwg.se.empire.view.WebTecTUI

object Empire {

  def main(args: Array[String]): Unit = {
    val tui = WebTecTUI()
    tui.processPlayerInput("r")
  }

}
