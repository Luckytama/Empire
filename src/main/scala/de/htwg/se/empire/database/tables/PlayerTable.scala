package de.htwg.se.empire.database.tables

import slick.jdbc.H2Profile.api._

case class PlayerTableConfig(id: String, name: String, countries: String)

class PlayerTable(tag: Tag) extends Table[PlayerTableConfig](tag, "PLAYERS") {

  def id: Rep[String] = column[String]("ID", O.PrimaryKey)

  def name: Rep[String] = column[String]("NAME")

  def countries: Rep[String] = column[String]("COUNTRIES")

  def * = (name, id, countries) <> (PlayerTableConfig.tupled, PlayerTableConfig.unapply)
}
