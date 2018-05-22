package de.htwg.se.empire.controller

import de.htwg.se.empire.controller.impl.DefaultAttackController
import de.htwg.se.empire.model.grid.Country
import de.htwg.se.empire.model.player.Player
import org.junit.runner.RunWith
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AttackControllerTest extends WordSpec with Matchers{
  val attackingCountry = Country("Attacker", List("Defender"))
  val defendingCountry = Country("Defender", List("Attacker"))
  val attacker = Player("Hans")
  val defender = Player("Jürgen")
  attacker.addCountry(attackingCountry)
  defender.addCountry(defendingCountry)

  "The Attacking Controller" when {
    val attackController = new DefaultAttackController
    "Somebody performs an attack with 1 attacking and 1 defending soldier" should {
      attackingCountry.addSoldiers(2)
      defendingCountry.addSoldiers(1)
      attackController.attackCountry(attackingCountry, defendingCountry, 1)
      "should have a country with one soldier less" in {
        if (attackingCountry.soldiers == 2) defendingCountry.soldiers should be(0) else attackingCountry.soldiers should be(1)
      }
    }
  }
}
