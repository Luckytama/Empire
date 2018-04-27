package de.htwg.se.empire.parser

trait Parser {

  def parseFile(path: String): Unit
}
