package de.htwg.se.empire.model.grid

import org.apache.logging.log4j.{ LogManager, Logger }

case class Country(name: String, adjacentCountries: List[String]) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  var soldiers = 0

  def addSoldiers(numberOfSoldiers: Int): Unit = {
    if (0 > numberOfSoldiers) {
      LOG.error("Numbers of soldiers can't be negative or null")
      throw new IllegalArgumentException
    }
    soldiers += numberOfSoldiers
  }

  def removeSoldiers(numberOfSoldiers: Int): Unit = {
    if (0 > numberOfSoldiers) {
      LOG.error("Numbers of soldiers to remove can't be negative or null")
      throw new IllegalArgumentException
    }
    if (0 <= soldiers - numberOfSoldiers) soldiers -= numberOfSoldiers else soldiers = 0
  }

  override def toString: String = name
}
