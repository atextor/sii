package de.kantico.sii.game

import java.awt.Color
import java.awt.event.KeyEvent._

import de.kantico.sii.engine.{Vec2d, Scene, Sound, Panel, BlackBox, Entity, FontAnimation, SpriteAnimation, Frame}
import de.kantico.sii.engine.FontAnimation._

class Level2Intro extends Scene {
  addEntity(new Entity(new BlackBox()))
  addEntity(new Entity(new FontAnimation("LEVEL 2: Samhain", Color.WHITE, 22), pos = Vec2d(70, 40)))
  addEntity(new Entity(new Frame("keyboard1"), pos = Vec2d(150, 60)))
  addEntity(new Entity(new SpriteAnimation("player2_fr", numPhases = 2, delay = 7), pos = Vec2d(80, 70)))
  
  at(1, { t => Sound.playMusic("netherworldshanty") })
  
  at(20, { t => addEntity(new Entity("On Samhain, the undead start to roam the land.", pos = Vec2d(50, 140))) })
  at(40, { t => addEntity(new Entity("As everbody knows, they can only be defeated", pos = Vec2d(50, 155))) })
  at(60, { t => addEntity(new Entity("with entertainment.", pos = Vec2d(50, 170))) })
  at(80, { t => addEntity(new Entity("Thus, you are on a quest to find the long lost", pos = Vec2d(50, 185))) })
  at(100, { t => addEntity(new Entity("magic juggling balls - perfect entertainment!", pos = Vec2d(50, 200))) })
  
  at(120, { t => addEntity(new Entity("PRESS SPACE", pos = Vec2d(130, 220))) })
  
  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_SPACE =>
        Panel.switchToScene(new Level2)
      case VK_ESCAPE =>
        Sound.stopMusic
        System.exit(1)
      case _ =>
    }
  }
}