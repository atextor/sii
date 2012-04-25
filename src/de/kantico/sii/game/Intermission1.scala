package de.kantico.sii.game

import java.awt.Color
import java.awt.event.KeyEvent._

import de.kantico.sii.engine.{Vec2d, Scene, Sound, Panel, Entity, SpriteAnimation, Frame, Movable, Living, NullAnimation}
import de.kantico.sii.engine.FontAnimation._
import de.kantico.sii.engine.Trigger._

class Intermission1 extends Scene {
  at(1, { t => Sound.playMusic("machine") })
  
  val player = new Player(speed = 2)
  player.pos = Vec2d(-40, 200)
  player.stop
  
  // Add background and player
  addEntity(new Entity(new Frame("imbg1")))
  addEntity(player)
  addEntity(new Entity(new Frame("imhouse1"), pos = Vec2d(35, 159)))
  
  // Player enters house
  at(30, player.goRight _) 
  at(60, { t => player.pos -= Vec2d(0, 5) })
  at(65, { t => player.pos -= Vec2d(0, 5) })
  at(80, player.stop _)
  
  // Switch on and off lights
  val lights = new Entity(new Frame("imlights1"), pos = Vec2d(112, 175))
  at(100, { t => addEntity(lights) })
  at(200, { t => lights.alive = false })
  
  val imp1bed = new Entity(new SpriteAnimation("imp1bed", numPhases = 2, delay = 15), pos = Vec2d(125, 178))
  at(240, { t =>
    addEntity(new Entity(new Frame("imroom1"), pos = Vec2d(111, 172)))
    addEntity(new Entity(new Frame("imbed1"), pos = Vec2d(120, 175)))
    addEntity(imp1bed)
  })
  
  // Show boiler
  at(340, { t => addEntity(new Entity(new SpriteAnimation("imboil", numPhases = 2, delay = 10), pos = Vec2d(126, 67))) })
  
  // Show upper floor
  val imp3bed1 = new Entity(new Frame("imp3bed1"), pos = Vec2d(125, 135))
  at(420, { t =>
    addEntity(new Entity(new Frame("imroom1"), pos = Vec2d(111, 120)))
    addEntity(new Entity(new Frame("imbed1"), pos = Vec2d(120, 126)))
    addEntity(imp3bed1)
    addEntity(new Entity(new SpriteAnimation("imdrip", numPhases = 4, delay = 5), pos = Vec2d(136, 120)))
  })
  
  // Wake up
  val imp3bed2 = new Entity(new Frame("imp3bed2"), pos = Vec2d(125, 135))
  at(540, { t =>
    imp3bed1.alive = false
    addEntity(imp3bed2)
  })
  
  val girl = new Living(
    aniLeft = new SpriteAnimation("g1_lf", numPhases = 2, delay = 9),
    aniRight = new SpriteAnimation("g1_rt", numPhases = 2, delay = 9),
    speed = 1
  )
  val girlGoesRight: Action = { i => girl.goRight; at(i + 60, girlGoesLeft) }
  val girlGoesLeft: Action = { i => girl.goLeft; at(i + 60, girlGoesRight) }
  girl.pos = Vec2d(120, 132)
  
  // Start running around
  at(620, { t =>
    imp3bed2.alive = false
    addEntity(girl)
    girl.goRight
    at(670, girlGoesLeft)
  })
  
  // Player wakes up
  at(720, { t =>
    imp1bed.alive = false
    addEntity(new Entity(new Frame("imp2bed1"), pos = Vec2d(125, 185)))
  })
  
  at(980, { t => Sound.stopMusic; Panel.switchToScene(new Level2Intro) })

  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_ESCAPE =>
        Panel.switchToScene(new Level2Intro)
      case _ =>
    }
  }
}