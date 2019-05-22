package de.htwg.se.empire.util

object ControllerMethod extends Enumeration {
  type ControllerMethod = Value
  val ATTACK_COUNTRY, LOAD_GRID_FROM_FILE, RAND_DISTRIBUTE_COUNTRIES, RAND_DISTRIBUTE_SOLDIERS, CALC_SOLDIERS_TO_DISTRIBUTE, DISTRIBUTE_SOLDIERS = Value
}
