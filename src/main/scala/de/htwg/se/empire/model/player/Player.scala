package de.htwg.se.empire.model.player

import de.htwg.se.empire.model.grid.Country
import org.apache.logging.log4j.{LogManager, Logger}

import scala.collection.mutable.ListBuffer

case class Player(name: String) {

  val countries = new ListBuffer[Country]
  var handholdSoldiers: Int = 0

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addCountry(country: Country): Unit = countries.append(country)

  def removeCountry(country: Country): Unit = {
    if (countries.contains(country)) {
      countries.remove(countries.indexOf(country))
    } else {
      LOG.error("Country is not in the list.")
    }
  }

  def addHandholdSoldiers(soldiers: Int): Unit = {
    handholdSoldiers += soldiers
  }

  def putSoldiers(soldiers: Int): Unit = {
    if (0 > (handholdSoldiers - soldiers)) {
      LOG.warn("You don't have that amout of soldiers in your hands.")
    } else {
      handholdSoldiers -= soldiers
    }
  }

  def getNumberOfAllSoldiers: Int = {
    var count: Int = 0
    countries.foreach(c => count += c.soldiers)
    count
  }

  def getCountryAmount: Int = countries.size

  override def toString: String = name + " => " + "countries: [" + countries.mkString(",") + "]"
}
