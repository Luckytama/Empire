package de.htwg.se.empire.controller.impl

import de.htwg.se.empire.controller.AttackController
import de.htwg.se.empire.model.grid.Country
import org.apache.logging.log4j.{ LogManager, Logger }

import scala.util.Random

class DefaultAttackController extends AttackController {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  val DICE_UPPER_CAP = 6

  def attackCountry(src: Country, target: Country, numberOfSoldiers: Int): (Country, Country) = {
    val attackerValues = generateAttackingValues(numberOfSoldiers)
    val defenderValues = generateAttackingValues(target.soldiers)

    val result = performAttack(attackerValues, defenderValues)

    if (src.soldiers - result._1 > 0) {
      val srcResult = src.removeSoldiers(result._1).get
      val targetResult = target.removeSoldiers(result._2).get
      if (target.soldiers < 0) {
        LOG.error("Something went wrong while attacking")
      }
      (srcResult, targetResult)
    } else {
      LOG.error("Something went wrong while attacking")
      throw new Exception
    }
  }

  private def generateAttackingValues(numberOfSoldiers: Int): Array[Int] = {
    numberOfSoldiers match {
      case 1 =>
        rollDice(1)
      case 2 =>
        rollDice(2)
      case x if x > 2 =>
        rollDice(3)
      case _ =>
        LOG.error("Unexpected number of Soldiers")
        throw new IllegalArgumentException
    }
  }

  private def rollDice(attackerDice: Int): Array[Int] = {
    val numbers = new Array[Int](attackerDice)
    for (i <- 0 until attackerDice) yield numbers(i) = Random.nextInt(DICE_UPPER_CAP + 1)
    numbers
  }

  private def performAttack(attackerValues: Array[Int], defenderValues: Array[Int]): (Int, Int) = {
    var attacker, defender = 0
    for (attack <- attackerValues) {
      defenderValues.foreach(defense => if (attack > defense) defender += 1 else attacker += 1)
    }
    (attacker, defender)
  }
}
