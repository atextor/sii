package de.kantico.sii.game

import java.awt.Color
import java.awt.event.KeyEvent._

import de.kantico.sii.engine.{Scene, Entity, Vec2d, FontAnimation, SpriteAnimation, Panel, Sound, Frame}

class TitleScene extends Scene {
  val player = new Player
  
  // Music!
  at(1, { t => Sound.playMusic("achaidhcheide") })
  
  // Add background to entities
  addEntity(new Entity(new Frame("titlebg1")))
  
  // Add "studying in" graphics and letter moving animation
  "studying_in".map(charToEntity).foldLeft(10) { case (i, e) =>
    e.pos = Vec2d(i, 40)
    addEntity(e)
    at(40 + i / 2, { t => e.direction = Vec2d.up * 3 })
    at(50 + i / 2, { t => e.direction = Vec2d.down * 3 })
    at(60 + i / 2, { t => e.direction = Vec2d.nowhere })
    i + e.size.x
  }
  
  // Let player walk to right, down, right
  player.pos = Vec2d(-40, 65)
  player.stop
  at(30, { t => player.goRight })
  at(180, { t => player.goDown })
  at(230, { t => player.goRight })
  at(280, { t => player.stop })
  
  // Add flag and move flag and player to the left
  val flag = new Entity(new SpriteAnimation("flag", numPhases = 4, delay = 5), Vec2d(320, 150))
  at(275, { t => flag.direction = Vec2d.left * 2 })
  at(285, { t => player.goLeft })
  at(430, { t => flag.direction = Vec2d.nowhere })
  at(450, { t => player.goDown; player.stop })
  
  // Add "IRELAND" graphics and move up animation
  "IRELAND".map(charToEntity).foldLeft(10) { case (i, e) =>
    e.pos = Vec2d(i, 250)
    addEntity(e)
    at(350 + i / 4, { t => e.direction = Vec2d.up * 3 })
    at(400 + i / 4, { t => e.direction = Vec2d.nowhere })
    i + e.size.x
  }
  
  // Show continue text
  at(460, { t => addEntity(new Entity(new FontAnimation("PRESS SPACE", Color.WHITE, 22), Vec2d(100, 180))) })
  
  // Add flag and player last, to be drawn on top
  addEntity(flag)
  addEntity(player)
  
  // Initial sort of actions
  sortActions
  
  /** Create a graphical letter entity from a char */
  private def charToEntity(c: Char) = new Entity(new Frame("letter_" + c + "1"))
  
  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_SPACE =>
        Panel.switchToScene(new Level1Intro)
      case VK_ESCAPE =>
        Sound.stopMusic
        System.exit(1)
      case _ =>
    }
  }
  
}