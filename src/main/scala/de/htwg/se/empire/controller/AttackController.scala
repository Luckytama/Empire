package de.htwg.se.empire.controller

import de.htwg.se.empire.model.grid.Country

trait AttackController {

  def attackCountry(src: Country, target: Country, numberOfSoldiers: Int): (Country, Country)

}
