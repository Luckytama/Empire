package de.htwg.se.empire

import com.google.inject.AbstractModule
import de.htwg.se.empire.controller.impl.{DefaultAttackController, DefaultGameController, DefaultInitController, DefaultReinforcementController}
import de.htwg.se.empire.controller.{AttackController, GameController, InitController, ReinforcementController}
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.parser.impl.JsonParser
import net.codingwell.scalaguice.ScalaModule

class EmpireModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Grid].to[PlayingField]
    bind[AttackController].to[DefaultAttackController]
    bind[InitController].to[DefaultInitController]
    bind[ReinforcementController].to[DefaultReinforcementController]
    bind[GameController].to[DefaultGameController]
    bind[Parser].to[JsonParser]
  }
}
