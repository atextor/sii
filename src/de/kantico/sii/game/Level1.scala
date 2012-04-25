package de.kantico.sii.game

import java.awt.event.KeyEvent._
import java.awt.Color

import java.util.Random

import scala.io.Source

import de.kantico.sii.engine.{Scene, Vec2d, Entity, Animation, SpriteAnimation, FontAnimation}
import de.kantico.sii.engine.{Trigger, Sound, Solid, Living, Movable, Frame, Panel}
import de.kantico.sii.engine.Trigger._
import de.kantico.sii.engine.FontAnimation._


class Level1 extends Scene {
  val random = new Random
  
  // Player
  val player = new Player(speed = 3)
  player.pos = Vec2d(8, 45)
  player.stop
  
  // Background
  addEntity(new Entity(new Frame("level1bg1")))
  
  // Door text
  val text = new Entity("You can't leave now. The lecture is still running.", pos = Vec2d(50, 30), active = false)
  addEntity(text)
  addEntity(new Trigger(size = Vec2d(25, 30), pos = Vec2d(10, 30), subject = player,
    enterAction = text.setActive _, leaveAction = text.setPassive _))
  addEntity(new Trigger(size = Vec2d(25, 30), pos = Vec2d(285, 30), subject = player,
    enterAction = text.setActive _, leaveAction = text.setPassive _))
  
  // Tables
  val tableGraphics = new Frame("table1")
  0 to 2 map { y =>
    addEntity(new Entity(tableGraphics, pos = Vec2d(45, 80 + y * 50)))
    player.addObstacle(new Solid(size = Vec2d(228, 17), pos = Vec2d(45, 81 + y * 50)))
  }
  
  // Student: lower halves
  addEntity(new Entity(new SpriteAnimation("stud1b", numPhases = 2, delay = 50), pos = Vec2d(110, 180)))
  addEntity(new Entity(new Frame("stud2b1"), pos = Vec2d(225, 80)))
  addEntity(new Entity(new SpriteAnimation("stud3b", numPhases = 2, delay = 60), pos = Vec2d(135, 130)))
  
  // Add player between tables and seats
  addEntity(player)
  
  // Seats
  val seatGraphics = new Frame("seat1")
  for (y <- 0 to 2; x <- 1 to 10) {
    addEntity(new Entity(seatGraphics, pos = Vec2d(x * 23 + 25, 67 + y * 50)))
  }
  
  // Students: upper halves
  addEntity(new Entity(new SpriteAnimation("stud1a", numPhases = 2, delay = 50), pos = Vec2d(110, 157)))
  addEntity(new Entity(new SpriteAnimation("stud2a", numPhases = 2, delay = 30), pos = Vec2d(225, 46)))
  addEntity(new Entity(new SpriteAnimation("stud3a", numPhases = 2, delay = 60), pos = Vec2d(135, 107)))
  
  // Other player boundaries: top, bottom, sides
  player.addObstacle(new Solid(size = Vec2d(320, 15), pos = Vec2d(0, 30)))
  player.addObstacle(new Solid(size = Vec2d(320, 15), pos = Vec2d(0, 235)))
  player.addObstacle(new Solid(size = Vec2d(15, 240), pos = Vec2d(-15, 0)))
  player.addObstacle(new Solid(size = Vec2d(15, 240), pos = Vec2d(320, 0)))
  
  // Professor
  val prof = new Living(
    aniLeft = new SpriteAnimation("prof_lf", numPhases = 2, delay = 9),
    aniUp = new SpriteAnimation("prof_bk", numPhases = 2, delay = 25),
    aniRight = new SpriteAnimation("prof_rt", numPhases = 2, delay = 9),
    aniDown = new SpriteAnimation("prof_fr", numPhases = 2, delay = 9),
    speed = 1
  )
  val profGoesRight: Action = { i => prof.goRight; at(i + 60, profGoesLeft) }
  val profGoesLeft: Action = { i => prof.goLeft; at(i + 60, profGoesRight) }
  
  // Make him stand and look up in the beginning, then walk left and right in a loop
  prof.pos = Vec2d(140, 207)
  prof.goUp
  prof.stop
  at(100, prof.goRight _)
  at(130, profGoesLeft)
  addEntity(prof)
  
  // Add professor blah blah
  Source.fromURL(this.getClass.getClassLoader.getResource("prof1.txt")).getLines.foldLeft(10) { (i, l) =>
    val line = new Entity(l + (if (l.last.toString == ".") "" else "..."), pos = Vec2d(30, 220))
    at(i, { t => addEntity(line) })
    at(i + 90, { t => line.alive = false })
    i + 100
  }
  
  // Score display
  addEntity(new Entity(new Frame("coin3"), pos = Vec2d(50, 2)))
  val scoreText = new FontAnimation("Score: 0", Color.white, 11)
  addEntity(new Entity(scoreText, pos = Vec2d(70, 13)))
  
  // Health display
  addEntity(new Entity("Health:", pos = Vec2d(140, 13)))
  var hearts = (0 to 5).foldLeft(List[Entity]()) { (l, i) =>
    val heart = new Entity(new Frame("heart1"), pos = Vec2d(175 + 9 * i, 5))
    addEntity(heart)
    heart :: l
  }
  
  // Spawn coins
  val spawnCoin: Action = { i =>
    val xdirection = random.nextInt(6) - 3
    addEntity(new Coin(startAt = prof.pos + Vec2d(10, 0), Vec2d(xdirection, -1), collector = player, onCollect = { _ =>
      Sii.score += 100
      scoreText.text = "Score: " + Sii.score
      Sound.playSound("pling")
    }))
    at(i + 50, spawnCoin)
  }
  at(100, spawnCoin)
  
  // Spawn skulls
  val spawnSkull: Action = { i =>
    val xdirection = random.nextInt(6) - 3
    addEntity(new Skull(startAt = prof.pos + Vec2d(10, 0), Vec2d(xdirection, -1), collector = player, onCollect = { _ =>
      Sound.playSound("placbldg")
      if (hearts.size >= 1) {
        hearts.head.alive = false
        hearts = hearts.tail
      }
      if (hearts.size == 0) {
        // cheap ad-hoc drop shadow
        addEntity(new Entity(new FontAnimation("Too much work!", Color.BLACK, 22), Vec2d(50, 100)))
        addEntity(new Entity(new FontAnimation("Too much work!", Color.WHITE, 22), Vec2d(48, 98)))
        addEntity(new Entity(new FontAnimation("Time to go home...", Color.BLACK, 22), Vec2d(30, 150)))
        addEntity(new Entity(new FontAnimation("Time to go home...", Color.WHITE, 22), Vec2d(28, 148)))
        at(i + 150, { t => Sound.stopMusic; Panel.switchToScene(new Intermission1) })
      }
    }))
    if (hearts.size > 0) at(i + 80, spawnSkull)
  }
  at(200, spawnSkull)
  
  // Initial sort of actions
  sortActions
  
  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_LEFT => player.goLeft
      case VK_UP => player.goUp
      case VK_RIGHT => player.goRight
      case VK_DOWN => player.goDown
      case VK_ESCAPE =>
        Sound.stopMusic
        System.exit(1)
      case _ =>
    } else {
      player.stop
    }
  }

}