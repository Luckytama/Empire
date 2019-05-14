package de.htwg.se.empire.model.grid

import de.htwg.se.empire.model.player.Player
import org.apache.logging.log4j.{LogManager, Logger}
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable.ListBuffer

case class PlayingField(continents: List[Continent] = List.empty, players: List[Player] = List.empty, playerOnTurn: String = "") {

  def this() = this(List.empty, List.empty, "")

  val LOG: Logger = LogManager.getLogger(this.getClass)

  def addPlayer(player: Player): PlayingField = {
    if (5 >= players.size) {
      copy(players = player :: players)
    } else {
      LOG.info("There can't be more than 5 players.")
      this
    }

  }

  def addPlayers(players: String*): PlayingField = {
    var playingField: PlayingField = copy()
    for (playerName <- players) {
      playingField = playingField.addPlayer(Player(playerName))
    }
    playingField
  }

  def removePlayer(player: Player): PlayingField = copy(players = players.filter(_ != player))

  def getAllCountries: List[Country] = {
    var countries = new ListBuffer[Country]
    continents.foreach(c => countries ++= c.countries)
    countries.toList
  }

  def getPlayerForCountry(country: Country): Option[Player] = {
    var playerOpt: Option[Player] = None
    players.foreach(p => if (p.countries.contains(country.name)) playerOpt = Some(p))
    playerOpt
  }

  def getPlayer(playerName: String): Option[Player] = {
    val p = players find (_.name == playerName)
    if (p.isDefined) {
      p
    } else {
      LOG.info("Player not found with name ", playerName)
      None
    }
  }

  def getCountry(countryName: String): Option[Country] = {
    val country = getAllCountries find (_.name == countryName)
    if (country.isDefined) {
      country
    } else {
      LOG.info("Country not found with ", countryName)
      None
    }
  }

  def moveSoldiers(src: Country, target: Country, numberOfSoldiers: Int): PlayingField = {
    val srcCountry = src.removeSoldiers(numberOfSoldiers)
    val targetCountry = target.addSoldiers(numberOfSoldiers)
    updateCountry(src, srcCountry.get).updateCountry(target, targetCountry.get)
  }

  def updateCountry(oldCountry: Country, newCountry: Country): PlayingField = {
    val maybeContinent = continents.find(continent => continent.countries.contains(oldCountry))
    if (maybeContinent.isDefined) {
      val indexInContinents = continents.indexOf(maybeContinent.get)
      val indexInCountries = maybeContinent.get.countries.indexOf(oldCountry)
      copy(continents = continents.updated(indexInContinents, maybeContinent.get.copy(countries = maybeContinent.get.countries.updated(indexInCountries, newCountry))))
    } else {
      LOG.error("Could not find country named: " + oldCountry.name)
      this
    }
  }

  def updatePlayer(player: Player): PlayingField = {
    val maybePlayer = players.find(p => p.name == player.name)
    if (maybePlayer.isDefined) {
      copy(players = players.updated(players.indexOf(maybePlayer.get), player))
    } else {
      LOG.error("Could not find player named: " + player.name)
      this
    }
  }

  def addCountryToPlayer(updatePlayer: Player, country: Country): PlayingField = {
    val maybePlayer = players.find(p => p.name == updatePlayer.name)
    if (maybePlayer.isDefined) {
      copy(players = players.updated(players.indexOf(maybePlayer.get), maybePlayer.get.addCountry(country.name)))
    } else {
      this
    }
  }

  def removeCountryFromPlayer(player: Player, country: Country): PlayingField = {
    copy(players = players.updated(players.indexOf(player), player.removeCountry(country.name)))
  }

  def distributeHandholdSoldiers(player: Player, handholdSoldiers: Int): PlayingField = {
    copy(players = players.updated(players.indexOf(player), player.copy(handholdSoldiers = handholdSoldiers)))
  }

  def getPlayerOnTurn: Option[Player] = players.find(_.name eq playerOnTurn)

  def addSoldiersToCountry(country: Country, numberOfSoldiers: Int): PlayingField = {
    updateCountry(country, country.addSoldiers(numberOfSoldiers).get)
  }

  def getCountriesForPlayer(player: Player): List[Country] = getAllCountries.filter(c => player.countries.contains(c.name))

  def getNumberOfAllSoldiers(player: Player): Int = {
    val countries = getCountriesForPlayer(player)
    var sum = 0
    countries.foreach(c => sum += c.soldiers)
    sum
  }

  def generateJsonObject: String = {
    val json =
      ("playerOnTurn" -> this.playerOnTurn) ~
        ("players" ->
          this.players.map { p =>
            ("name" -> p.name) ~
              ("handholdSoldiers" -> p.handholdSoldiers) ~
              ("countries" -> p.countries.map(_.toString))
          }) ~
        ("continents" -> this.continents.map { continent =>
          ("name" -> continent.name) ~
            ("bonus" -> continent.bonus) ~
            ("countries" -> continent.countries.map { country =>
              ("name" -> country.name) ~
                ("soldiers" -> country.soldiers) ~
                ("adjacentCountries" -> country.adjacentCountries.map(_.toString))
            })
        })
    pretty(render(json))
  }

  override def toString: String = {
    val output = new StringBuilder
    if (players.nonEmpty) {
      output.append("Players: " + players.mkString + "\n")
    }
    output.append("Continents: " + continents.mkString)
    output.toString()
  }
}
