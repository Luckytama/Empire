package de.htwg.se.empire.model.grid

import org.apache.logging.log4j.{ LogManager, Logger }

case class Player(name: String, countries: List[Country]) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addCountry(country: Country): Unit = {
    countries :+ country
  }

  def removeCountry(country: Country): Unit = {
    if (countries.contains(country)){
      countries = Countries - country
    } else {
      LOG.error("Country is not in the list.")

    }
  }

  override def toString: String = name
}

