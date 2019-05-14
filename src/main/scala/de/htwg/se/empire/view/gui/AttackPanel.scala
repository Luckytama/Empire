package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import de.htwg.se.empire.util.Phase
import javax.swing.border.{EtchedBorder, TitledBorder}

import scala.collection.mutable.ListBuffer
import scala.swing._
import scala.swing.event.SelectionChanged

class AttackPanel(gameController: GameController) extends FlowPanel {
  var sourceCountry = new ComboBox[String](List.empty[String])
  var destCountry = new ComboBox[String](List.empty[String])
  val soldiersAmount = new TextField()
  soldiersAmount.text = "0"
  val attackButton = new Button("Attack")

  var sourceComboPanel: FlowPanel = new FlowPanel {
    contents += sourceCountry
  }

  var destComboPanel: FlowPanel = new FlowPanel {
    contents += destCountry
  }

  val attackPanel: GridPanel = new GridPanel(7, 1) {
    border = new TitledBorder(new EtchedBorder(), "Attack country")
    contents += new Label("Choose country to attack from:")
    contents += sourceComboPanel
    contents += new Label("Choose country to attack:")
    contents += destComboPanel
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

  def refresh(): Unit = {
    if (gameController.getCurrentPhase == Phase.ATTACK) {
      this.enable()
      val countrySource = new ListBuffer[String]
      gameController.playingField.getCountriesForPlayer(gameController.getPlayerOnTurn).foreach(c => {
        countrySource.append(c.name)
      })
      sourceCountry = new ComboBox[String](countrySource) {
        listenTo(this.selection)
        reactions += {
          case SelectionChanged(_) => {
            val list = new ListBuffer[String]
            gameController.playingField.getCountriesForPlayer(gameController.getPlayerOnTurn).foreach(c => {
              if (c.name == sourceCountry.selection.item) {
                soldiersAmount.text = (c.soldiers - 1).toString
                c.adjacentCountries.foreach(f => {
                  if (!gameController.playingField.getCountriesForPlayer(gameController.getPlayerOnTurn).exists(p => p.name == f)) {
                    list.append(f)
                  }
                })
              }
            })
            destComboPanel.contents.clear()
            destCountry = new ComboBox[String](list)
            destComboPanel.contents += destCountry
            attackPanel.revalidate()
            attackPanel.repaint()
          }
        }
      }
      sourceComboPanel.contents.clear()
      sourceComboPanel.contents += sourceCountry
      this.revalidate()
      this.repaint()
    } else {
      this.disable()
    }
  }

  contents += attackPanel
}
