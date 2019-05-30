package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.controller.impl.PhaseChanged
import de.htwg.se.empire.util.Phase
import javax.swing.{ JFrame, JOptionPane }

import scala.swing._
import scala.swing.event.{ ButtonClicked, Key }
import scala.swing.ComboBox

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

  contents = new BorderPanel {
    add(setupPanel, BorderPanel.Position.North)
    add(attackPanel, BorderPanel.Position.West)
    add(gameInfoPanel, BorderPanel.Position.Center)
    add(distributePanel, BorderPanel.Position.East)
  }

  listenTo(setupPanel.startGameButton)
  listenTo(distributePanel.distributeButton)
  listenTo(gameInfoPanel.endTurnButton)
  listenTo(attackPanel.attackButton)
  listenTo(gameController)

  reactions += {
    case ButtonClicked(setupPanel.startGameButton) => {
      gameController.changeToGamePhase()
      setupPanel.visible = false
      distributePanel.visible = true
      attackPanel.visible = true
      gameInfoPanel.visible = true
      attackPanel.disable()
      gameInfoPanel.refresh()
      distributePanel.refresh()
      size = new Dimension(1200, 400)
      this.repaint()
    }
    case ButtonClicked(distributePanel.distributeButton) => {
      gameController.distributeSoldiers(distributePanel.soldiersAmount.text.toInt, distributePanel.countriesCombo.selection.item)
      distributePanel.refresh()
      attackPanel.refresh()
      gameInfoPanel.refresh()
    }
    case ButtonClicked(gameInfoPanel.endTurnButton) => {
      gameController.completeRound()
      attackPanel.refresh()
      distributePanel.refresh()
      gameInfoPanel.refresh()
    }
    case ButtonClicked(attackPanel.attackButton) => {
      gameController.attackCountry(attackPanel.sourceCountry.selection.item, attackPanel.destCountry.selection.item, attackPanel.soldiersAmount.text.toInt)
      gameInfoPanel.refresh()
      attackPanel.refresh()
      if (gameController.getCurrentPhase == Phase.FINISH) {
        distributePanel.disable()
        attackPanel.disable()
        gameInfoPanel.disable()
        JOptionPane.showMessageDialog(new JFrame(), "Player " + gameController.getPlayerOnTurn.name + " won!", "Success", JOptionPane.INFORMATION_MESSAGE)
      }
    }
    case event: PhaseChanged => {
      gameInfoPanel.refresh()
      attackPanel.refresh()
      distributePanel.refresh()
    }
  }

}
