package de.htwg.se.empire

import com.google.inject.AbstractModule
import de.htwg.se.empire.controller.impl.{ DefaultAttackController, DefaultGameController, DefaultInitController, DefaultReinforcementController }
import de.htwg.se.empire.controller.{ AttackController, GameController, InitController, ReinforcementController }
import de.htwg.se.empire.model.Grid
import de.htwg.se.empire.model.grid.PlayingField
import de.htwg.se.empire.parser.Parser
import de.htwg.se.empire.parser.impl.JsonParser

class EmpireModule extends AbstractModule {
  def configure {
    bind(classOf[Grid]).to(classOf[PlayingField])
    bind(classOf[AttackController]).to(classOf[DefaultAttackController])
    bind(classOf[InitController]).to(classOf[DefaultInitController])
    bind(classOf[ReinforcementController]).to(classOf[DefaultReinforcementController])
    bind(classOf[GameController]).to(classOf[DefaultGameController])
    bind(classOf[Parser]).to(classOf[JsonParser])
  }
}
