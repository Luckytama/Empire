package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase
import javax.swing.border.{ EtchedBorder, TitledBorder }

import scala.collection.mutable.ListBuffer
import scala.swing._

class DistributePanel(gameController: GameController) extends FlowPanel {

  val soldiersToDistribute = new Label("N/A")

  val soldiersAmount = new TextField()
  soldiersAmount.text = "0"

  val distributeButton = new Button("Distribute Soldiers")

  var countriesCombo = new ComboBox[String](List.empty[String])

  val distributeInfoPanel = new GridPanel(1, 2) {
    contents += new Label("Soldiers to Distribute: ")
    contents += soldiersToDistribute
  }

  val comboPanel = new FlowPanel {
    contents += countriesCombo
  }

  val distributeSoldierPanel = new GridPanel(3, 1) {
    vGap = 10
    hGap = 5
    border = new TitledBorder(new EtchedBorder(), "Distribute Soldiers")
    contents += new Label("Choose country:")
    contents += comboPanel
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

  def refresh(): Unit = {
    if (gameController.getCurrentPhase == Phase.REINFORCEMENT) {
      this.enable()
      soldiersToDistribute.text = gameController.playerOnTurn.handholdSoldiers.toString
      val countries = new ListBuffer[String]
      gameController.playerOnTurn.countries.toList.foreach(c => {
        countries.append(c.name)
      })
      this.comboPanel.contents.clear()
      countriesCombo = new ComboBox[String](countries)
      this.comboPanel.contents += countriesCombo
      this.revalidate()
      this.repaint()
    } else {
      this.disable()
    }
  }
}
