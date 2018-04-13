package de.htwg.se.empire.model.grid

import org.junit.runner.RunWith
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PlayerTest extends WordSpec with Matchers {
  "A Player" when {
    val countries = List[Country](Country("Another Country", null))
    val player = Player("Player1", countries)
    "new" should {
      "have a name" in {
        player.name should be("Player1")
      }
      "have an country" in {
        player.countries.length should be(1)
      }
      "have a nice String representation" in {
        player.toString should be("Player1")
      }
    }
    "add a country" should {
      "have country" in {
        player.addCountry(Country("New Country", null))
        player.countries should be(List(Country("Another Country", null), Country("New Country", null)))
      }
    }
    "remove country" should {
      "have 1 country" in {
        player.removeCountry(Country("New Country", null))
        player.countries should be(List(Country("Another Country", null)))
      }
    }
  }
}

