package de.htwg.se.empire.model.player

import org.apache.logging.log4j.{LogManager, Logger}

case class Player(name: String, countries: List[String] = List.empty, handholdSoldiers: Int = 0) {

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addCountry(country: String): Player = {
    if (countries.contains(country)) {
      LOG.info("Could not add " + country + " because player already own country")
      copy()
    } else {
      copy(countries = country :: countries)
    }
  }

  def removeCountry(country: String): Player = {
    if (countries.contains(country)) {
      copy(countries = countries.filter(_ != country))
    } else {
      LOG.error("Country is not in the list.")
      copy()
    }
  }

  def putSoldiers(soldiers: Int): Player = {
    copy(handholdSoldiers = handholdSoldiers - soldiers)
  }

  /*def addSoldiersToCountry(country: String, numberOfSoldiers: Int): Player = {
    country.addSoldiers(numberOfSoldiers) match {
      case Success(updatedCountry) =>
        copy(countries = countries.updated(countries.indexOf(country), updatedCountry))
      case Failure(exception) =>
        LOG.debug("Could not add " + numberOfSoldiers + " to country " + country.name, exception)
        this
    }
  }*/

  /*def getNumberOfAllSoldiers: Int = {
    var count: Int = 0
    countries.foreach(c => count += c.soldiers)
    count
  }*/

  def getCountryAmount: Int = countries.size

  def containsCountry(country: String): Boolean = {
    if (countries.exists(_.equals(country))) true else false
  }

  override def toString: String = name + " => " + "countries: [" + countries.mkString(",") + "]"
}
