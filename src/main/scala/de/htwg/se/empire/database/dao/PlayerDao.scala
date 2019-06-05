package de.htwg.se.empire.database.dao

import de.htwg.se.empire.database.tables.{PlayerTableConfig, PlayerTable}
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object PlayerDao {

  private lazy val db = Database.forURL("jdbc:h2:file:./data/db", driver = "org.h2.Driver", keepAliveConnection = true)
  private lazy val players = TableQuery[PlayerTable]

  private def findByIdQuery(id: String): Query[PlayerTable, PlayerTableConfig, Seq] = {
    players.filter(f => f.id === id)
  }

  def findAll: Future[Seq[String]] = {
    db.run(players.result.map(_.map(f => f.id)))
  }

  def findById(id: String): Future[PlayerTableConfig] = {
    db.run(findByIdQuery(id).result.head)
  }

  def insert(playerTableConfig: PlayerTableConfig): Future[Int] = {
    Await.result(db.run(players.schema.create), Duration.Inf)
    db.run(players += playerTableConfig)
  }

  def update(id: String, playerTableConfig: PlayerTableConfig): Future[Int] = {
    db.run(findByIdQuery(id).update(playerTableConfig))
  }

  def delete(id: String): Future[Int] = {
    db.run(findByIdQuery(id).delete)
  }

}