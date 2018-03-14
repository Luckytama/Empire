package de.htwg.se.empire.model.grid

case class Country(name: String, adjacentCountries: List[Country]) {
  var soldiers = 0

  def setSoldiers(numberOfSoldiers: Int): Unit = {
    assert(0 < numberOfSoldiers, "Numbers of soldiers can't be negative or null")
    soldiers += numberOfSoldiers
  }

  override def toString: String = name
}
