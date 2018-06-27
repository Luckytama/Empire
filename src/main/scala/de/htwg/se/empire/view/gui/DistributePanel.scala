package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import javax.swing.border.{ EtchedBorder, TitledBorder }

import scala.swing._

class DistributePanel(gameController: GameController) extends FlowPanel {
  enabled = false

  val soldiersToDistribute = new Label("N/A")

  val soldiersAmount = new TextField()
  soldiersAmount.text = "0"

  val distributeButton = new Button("Distribute Soldiers")

  val countriesCombo = new ComboBox("")

  val distributeInfoPanel = new GridPanel(1, 2) {
    contents += new Label("Soldiers to Distribute: ")
    contents += soldiersToDistribute
  }

  val distributeSoldierPanel = new GridPanel(3, 1) {
    vGap = 10
    hGap = 5
    border = new TitledBorder(new EtchedBorder(), "Distribute Soldiers")
    contents += new Label("Choose country:")
    contents += countriesCombo
    contents += new Label("Enter how many soldiers to disribute:")
    contents += soldiersAmount
    contents += distributeButton
  }

  val distributePanel = new GridPanel(2, 1) {
    enabled = false
    border = new TitledBorder(new EtchedBorder(), "Soldier Distribution")
    contents += distributeInfoPanel
    contents += distributeSoldierPanel
  }

  contents += distributePanel

  def enable(): Unit = {
    countriesCombo.enabled = true
    soldiersAmount.enabled = true
    distributeButton.enabled = true
  }

  def disable(): Unit = {
    countriesCombo.enabled = false
    soldiersAmount.enabled = false
    distributeButton.enabled = false
  }

}
