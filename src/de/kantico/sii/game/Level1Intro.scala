package de.kantico.sii.game

import java.awt.Color
import java.awt.event.KeyEvent._

import de.kantico.sii.engine.{Vec2d, Scene, Sound, Panel, BlackBox, Entity, FontAnimation, SpriteAnimation, Frame}
import de.kantico.sii.engine.FontAnimation._

class Level1Intro extends Scene {
  addEntity(new Entity(new BlackBox()))
  addEntity(new Entity(new FontAnimation("LEVEL 1: Lecture", Color.WHITE, 22), pos = Vec2d(90, 40)))
  addEntity(new Entity(new Frame("keyboard1"), pos = Vec2d(150, 60)))
  addEntity(new Entity(new SpriteAnimation("player_fr", numPhases = 2, delay = 7), pos = Vec2d(80, 70)))
  
  at(20, { t => addEntity(new Entity(new SpriteAnimation("coin", numPhases = 8, delay = 2), pos = Vec2d(90, 120))) })
  at(20, { t => addEntity(new Entity("Collect information", pos = Vec2d(130, 130))) })
  at(40, { t => addEntity(new Entity(new SpriteAnimation("skull", numPhases = 9, delay = 2), pos = Vec2d(85, 140))) })
  at(40, { t => addEntity(new Entity("Avoid assignments", pos = Vec2d(130, 160))) })
  at(60, { t => addEntity(new Entity("Level ends, when your health is depleted.", pos = Vec2d(70, 190))) })
  at(80, { t => addEntity(new Entity("PRESS SPACE", pos = Vec2d(140, 220))) })
  
  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_SPACE =>
        Panel.switchToScene(new Level1)
      case VK_ESCAPE =>
        Sound.stopMusic
        System.exit(1)
      case _ =>
    }
  }
}