package de.htwg.se.empire.model.grid

case class Continent(name: String, bonus: Int, countries: List[Country]) {

  override def toString: String = name + " => " + "Bonus: " + bonus + " Countries: [" + countries.mkString(",") + "]"
}
