package de.htwg.se.empire.model.player

import de.htwg.se.empire.model.grid.Country
import org.apache.logging.log4j.{ LogManager, Logger }

import scala.collection.mutable.ListBuffer

case class Player(name: String, countries: ListBuffer[Country]) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addCountry(country: Country): Unit = countries.append(country)

  def removeCountry(country: Country): Unit = {
    if (countries.contains(country)) {
      countries.remove(countries.indexOf(country))
    } else {
      LOG.error("Country is not in the list.")
    }
  }

  def getCountryAmount(): Int = countries.size

  override def toString: String =  name + " => " + "countries: [" + countries.mkString(",") + "]"
}
