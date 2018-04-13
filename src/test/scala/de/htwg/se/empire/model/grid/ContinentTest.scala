package de.htwg.se.empire.model.grid

import org.junit.runner.RunWith
import org.scalatest.{ Matchers, WordSpec }
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ContinentTest extends WordSpec with Matchers {
  "A Continent" when {
    "new" should {
      val continent = Continent("Continent", 5, List(Country("Country", null)))
      "have a name" in {
        continent.name should be("Continent")
      }
      "have a bonus value" in {
        continent.bonus should be(5)
      }
      "have a list of countries" in {
        continent.countries.length should be(1)
      }
      "have a nice Strin representation" in {
        continent.toString should be("Continent => Bonus: 5, Countries: [Country]")
      }
    }
  }
}
