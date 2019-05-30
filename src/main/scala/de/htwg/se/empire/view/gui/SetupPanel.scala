package de.htwg.se.empire.view.gui

import de.htwg.se.empire.controller.GameController
import javax.swing.{ JFileChooser, JFrame, JOptionPane }
import javax.swing.filechooser.FileNameExtensionFilter

import scala.swing._
import scala.swing.ComboBox

import javax.swing.border.{ EtchedBorder, TitledBorder }

import scala.swing.event.ButtonClicked

class SetupPanel(gameController: GameController) extends FlowPanel {

  val chooseFileLabel = new Label("Please choose a Playingfield.json: ")
  val chooseFileButton = new Button("Choose file")
  val fileChooser = new JFileChooser("/Users/michael/Documents/Web-Technologien/Play-Empire/app/playingfield/EmpireData.json")
  fileChooser.setFileFilter(new FileNameExtensionFilter("Json Files", "json"))
  val fields = new ComboBox[String](List("EmpireData.json", "PresentationData.json"))

  val playerInput = new TextField()
  val addPlayerButton = new Button("Add Player")

  val startGameButton = new Button("Start Game")

  listenTo(addPlayerButton)
  listenTo(chooseFileButton)

  val setupPanel = new GridPanel(3, 2) {
    vGap = 10
    hGap = 5
    border = new TitledBorder(new EtchedBorder(), "Setup")
    contents += chooseFileLabel
    contents += chooseFileButton
    contents += fields
    contents += playerInput
    contents += addPlayerButton
    contents += startGameButton
  }

  contents += setupPanel

  reactions += {
    case ButtonClicked(`chooseFileButton`) => {
      val file: String = fields.selection.item
      gameController.setUpPhase("/api/" + file)
      JOptionPane.showMessageDialog(new JFrame(), "Chosen playingfield: " + file)
    }
    case ButtonClicked(`addPlayerButton`) => {
      if (playerInput.text.trim == "") {
        JOptionPane.showMessageDialog(new JFrame(), "Player name can't be empty!", "Error", JOptionPane.ERROR_MESSAGE)
        playerInput.text = ""
      } else {
        gameController.addPlayer(playerInput.text.trim)
        JOptionPane.showMessageDialog(new JFrame(), "Added player: " + playerInput.text, "Success", JOptionPane.INFORMATION_MESSAGE)
        playerInput.text = ""
      }

    }
  }

  def disable(): Unit = {
    addPlayerButton.enabled = false
    playerInput.enabled = false
  }

}

