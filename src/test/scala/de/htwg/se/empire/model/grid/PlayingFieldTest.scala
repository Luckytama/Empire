package de.htwg.se.empire.model.grid

import de.htwg.se.empire.model.player.Player
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{ Matchers, WordSpec }

@RunWith(classOf[JUnitRunner])
class PlayingFieldTest extends WordSpec with Matchers {
  "A Playing Field" when {
    val sampleCountry = Country("Deutschland", null)
    val playingField = PlayingField(List(Continent("Europa", 5, List(sampleCountry))))
    "new" should {
      "have no players" in {
        playingField.players should be(List.empty)
        playingField.getPlayer("Hans").isDefined should be(false)
      }
      "have countries" in {
        playingField.continents.length should be(1)
        playingField.getCountry("Deutschland").isDefined should be(true)
      }
      "doesn't find player for country" in {
        playingField.getPlayerForCountry(sampleCountry) should be(None)
      }
      "have a nice String representation" in {
        playingField.toString should be("Continents: Europa => Bonus: 5, Countries: [Deutschland]")
      }
    }
    "add Player" should {
      val player = Player("Hannes")
      "have a player" in {
        playingField.addPlayer(player)
        playingField.players.length should be(1)
        playingField.getPlayer("Hannes").get should be(player)
      }
      "have a nice String representation" in {
        playingField.toString should be("Players: Hannes => countries: []\nContinents: Europa => Bonus: 5, Countries: [Deutschland]")
      }
      "have no player after remove" in {
        playingField.removePlayer(player)
        playingField.players should be(List.empty)
      }
      "provide player for country" in {
        playingField.addPlayer(player)
        player.addCountry(sampleCountry)
        playingField.getPlayerForCountry(sampleCountry) should be(Some(player))
      }
    }
  }
}
