package de.htwg.se.empire.model.grid

import org.apache.logging.log4j.{ LogManager, Logger }

case class Country(name: String) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  var adjacentCountries: List[Country] = _
  var soldiers = 0

  def setAdjacentCountries(adjacentCountries: List[Country]): Unit = this.adjacentCountries = adjacentCountries

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
