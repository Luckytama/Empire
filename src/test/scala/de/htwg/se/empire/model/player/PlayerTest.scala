package de.htwg.se.empire.model.player

import de.htwg.se.empire.model.grid.Country
import org.junit.runner.RunWith
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class PlayerTest extends WordSpec with Matchers {
  "A Player" when {
    val player = Player("Player1")
    "new" should {
      "have a name" in {
        player.name should be("Player1")
      }
      "not have a country" in {
        player.countries.length should be(0)
      }
      "not have soldiers on countries" in {
        player.getNumberOfAllSoldiers should be(0)
      }
      "have a nice String representation" in {
        player.toString should be("Player1 => countries: []")
      }
    }
    "two countries are added" should {
      "have 2 countries" in {
        player.addCountry(Country("Another Country", null))
        player.addCountry(Country("New Country", null))
        player.countries should be(ListBuffer(Country("Another Country", null), Country("New Country", null)))
      }
      "provide amount of soldiers on countries" in {
        player.countries.head.addSoldiers(3)
        player.countries.last.addSoldiers(2)
        player.getNumberOfAllSoldiers should be(5)
      }
    }
    "a country is removed" should {
      "have 1 country" in {
        player.removeCountry(Country("New Country", null))
        player.countries should be(ListBuffer(Country("Another Country", null)))
      }
    }
    "a unkown country is removerd" should {
      "log an error" in {
        player.removeCountry(Country("x", null))
      }
    }
    "the country amount is requested" should {
      "be 1 country" in {
        player.getCountryAmount should be(1)
      }
    }
    "add handhold soldiers" should {
      "have more handhold soldiers" in {
        player.addHandholdSoldiers(5)
        player.handholdSoldiers should be(5)
      }
    }
    "put a valid amount of soldiers" should {
      "have less handhold soldiers" in {
        player.putSoldiers(5)
        player.handholdSoldiers should be(0)
      }
    }
    "put an invalid amount of soldiers" should {
      "have the same amount of soldiers on hand" in {
        player.putSoldiers(5)
        player.handholdSoldiers should be(0)
      }
    }
  }
}
