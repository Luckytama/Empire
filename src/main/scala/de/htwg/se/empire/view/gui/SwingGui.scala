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

  distributePanel.visible = false
  attackPanel.visible = false
  gameInfoPanel.visible = false

  menuBar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("Quit") {
        System.exit(0)
      })
    }
  }

  //size = new Dimension(1000, 1000)
  contents = new BorderPanel {
    add(setupPanel, BorderPanel.Position.North)
    add(attackPanel, BorderPanel.Position.West)
    add(gameInfoPanel, BorderPanel.Position.Center)
    add(distributePanel, BorderPanel.Position.East)
  }

  listenTo(setupPanel.startGameButton)

  reactions += {
    case ButtonClicked(setupPanel.startGameButton) => {
      gameController.changeToGamePhase()
      setupPanel.visible = false
      distributePanel.visible = true
      attackPanel.visible = true
      gameInfoPanel.visible = true
      attackPanel.disable()
      gameInfoPanel.refresh()
      size = new Dimension(1200, 400)
      this.repaint()
    }
  }

}
