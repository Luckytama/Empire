package de.htwg.se.empire.model.player

import de.htwg.se.empire.model.grid.Country
import org.junit.runner.RunWith
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class PlayerTest extends WordSpec with Matchers {
  "A Player" when {
    val countries = ListBuffer(Country("Another Country", null))
    val player = Player("Player1", countries)
    "new" should {
      "have a name" in {
        player.name should be("Player1")
      }
      "have a country" in {
        player.countries.length should be(1)
      }
      "have a nice String representation" in {
        player.toString should be("Player1 => countries: [Another Country]")
      }
    }
    "a country is added" should {
      "have 2 countries" in {
        player.addCountry(Country("New Country", null))
        player.countries should be(ListBuffer(Country("Another Country", null), Country("New Country", null)))
      }
    }
    "a country is removed" should {
      "have 1 country" in {
        player.removeCountry(Country("New Country", null))
        player.countries should be(ListBuffer(Country("Another Country", null)))
      }
    }
    "the country amount is requested" should {
      "be 1 country" in {
        player.getCountryAmount() should be(1)
      }
    }
  }
}
