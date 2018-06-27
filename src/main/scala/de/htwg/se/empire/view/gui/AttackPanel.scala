package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import javax.swing.border.{ EtchedBorder, TitledBorder }

import scala.swing._

class AttackPanel(gameController: GameController) extends FlowPanel {
  enabled = false
  val sourceCountry = new ComboBox("")
  val destCountry = new ComboBox("")
  val soldiersAmount = new TextField()
  soldiersAmount.text = "0"
  val attackButton = new Button("Attack")

  val attackPanel = new GridPanel(7, 1) {
    border = new TitledBorder(new EtchedBorder(), "Attack country")
    contents += new Label("Choose country to attack from:")
    contents += sourceCountry
    contents += new Label("Choose country to attack:")
    contents += destCountry
    contents += new Label("How many soldiers to use in this attack:")
    contents += soldiersAmount
    contents += attackButton
  }

  def enable(): Unit = {
    sourceCountry.enabled = true
    destCountry.enabled = true
    soldiersAmount.enabled = true
    attackButton.enabled = true
  }

  def disable(): Unit = {
    sourceCountry.enabled = false
    destCountry.enabled = false
    soldiersAmount.enabled = false
    attackButton.enabled = false
  }

  contents += attackPanel
}
