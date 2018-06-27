package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController

import scala.swing._
import scala.swing.event.{ ButtonClicked, Key }

class SwingGui(gameController: GameController) extends Frame {
  title = "HTWG Empire"

  visible = true
  val setupPanel = new SetupPanel(gameController)
  val gameInfoPanel = new GameInfoPanel(gameController)
  val attackPanel = new AttackPanel(gameController)
  val distributePanel = new DistributePanel(gameController)

  distributePanel.disable()
  attackPanel.disable()

  //size = new Dimension(1000, 1000)
  contents = new BorderPanel {
    add(gameInfoPanel, BorderPanel.Position.Center)
    add(setupPanel, BorderPanel.Position.North)
    add(attackPanel, BorderPanel.Position.West)
    add(distributePanel, BorderPanel.Position.East)
  }

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
    }
  }

  listenTo(gameInfoPanel.startGameButton)

  reactions += {
    case ButtonClicked(gameInfoPanel.startGameButton) => {
      setupPanel.disable()
      distributePanel.enable()
      attackPanel.enable()
    }
  }

}
