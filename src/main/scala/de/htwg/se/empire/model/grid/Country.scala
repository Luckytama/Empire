package de.htwg.se.empire.model.grid

import org.apache.logging.log4j.{ LogManager, Logger }

import scala.util.{ Failure, Success, Try }

case class Country(name: String, adjacentCountries: List[String], soldiers: Int = 0) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addSoldiers(numberOfSoldiers: Int): Try[Country] = {
    if (0 > numberOfSoldiers) {
      LOG.error("Numbers of soldiers can't be negative or null")
      throw new IllegalArgumentException
    }
    Success(copy(soldiers = soldiers + numberOfSoldiers))
  }

  def removeSoldiers(numberOfSoldiers: Int): Try[Country] = {
    if (0 > numberOfSoldiers) {
      LOG.error("Numbers of soldiers to remove can't be negative or null")
      Failure(new IllegalArgumentException)
    } else if (0 <= soldiers - numberOfSoldiers) Success(copy(soldiers = soldiers - numberOfSoldiers)) else Success(copy(soldiers = 0))
  }

  override def toString: String = name
}
