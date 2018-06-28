package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase
import javax.swing.border.{ EtchedBorder, TitledBorder }

import scala.swing._

class GameInfoPanel(gameController: GameController) extends FlowPanel {
  name = "Game Info"

  val currentPlayer = new Label("N/A")
  val endTurnButton = new Button("End turn")

  val gameInfoPanel = new GridPanel(2, 2) {
    border = new TitledBorder(new EtchedBorder(), "Game Info")
    contents += new Label("Current player: ")
    contents += currentPlayer
    contents += endTurnButton
  }

  def disable(): Unit = {
    endTurnButton.enabled = false
  }

  def enable(): Unit = {
    endTurnButton.enabled = true
  }

  def refresh(): Unit = {
    if (gameController.getCurrentPhase != Phase.IDLE && gameController.getCurrentPhase != Phase.SETUP) {
      currentPlayer.text = gameController.playerOnTurn.name
    }
    this.revalidate()
    this.repaint()
  }

  contents += gameInfoPanel
}
