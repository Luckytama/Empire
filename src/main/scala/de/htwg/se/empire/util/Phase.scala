package de.htwg.se.empire.util

object Phase extends Enumeration {
  type Phase = Value
  val IDLE, SETUP, REINFORCEMENT, ATTACK, MOVING = Value
}
