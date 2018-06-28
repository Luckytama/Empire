package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase
import javax.swing.border.{EtchedBorder, TitledBorder}

import scala.swing._

class GameInfoPanel(gameController: GameController) extends FlowPanel {
  name = "Game Info"

  val currentPlayer = new Label("N/A")
  val numberOfSoldiers = new Label("N/A")
  val numberOfCountries = new Label("N/A")
  val endTurnButton = new Button("End turn")

  val gameInfoPanel = new GridPanel(7, 2) {
    border = new TitledBorder(new EtchedBorder(), "Game Info")
    contents += new Label("Current player: ")
    contents += currentPlayer
    contents += new Label("Number of countries: ")
    contents += numberOfCountries
    contents += new Label("Number of soldiers: ")
    contents += numberOfSoldiers
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
      numberOfCountries.text = gameController.playerOnTurn.getCountryAmount.toString
      numberOfSoldiers.text = gameController.playerOnTurn.getNumberOfAllSoldiers.toString
    }
    if (gameController.getCurrentPhase != Phase.ATTACK) {
      endTurnButton.enabled = false
    } else {
      endTurnButton.enabled = true
    }
    this.revalidate()
    this.repaint()
  }

  contents += gameInfoPanel
}
