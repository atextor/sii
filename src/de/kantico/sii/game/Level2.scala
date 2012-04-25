package de.kantico.sii.game

import java.awt.event.KeyEvent._
import java.awt.Color

import de.kantico.sii.engine.{Level, Panel, ScrollingScene, Vec2d, Sound, Living, SpriteAnimation}
import de.kantico.sii.engine.{Entity, Frame, Movable, Solid, FontAnimation}
import de.kantico.sii.engine.FontAnimation._
import de.kantico.sii.engine.Trigger._

// A skeleton is a Living thing.... kind of :)
class Skeleton(level: Level, target: Movable, val originalPosition: Vec2d, onTouch: Skeleton => Action) extends Living(
    aniLeft = new SpriteAnimation("skeleton_lf", numPhases = 2, delay = 7),
    aniUp = new SpriteAnimation("skeleton_bk", numPhases = 2, delay = 7),
    aniRight = new SpriteAnimation("skeleton_rt", numPhases = 2, delay = 7),
    aniDown = new SpriteAnimation("skeleton_fr", numPhases = 2, delay = 7),
    speed = 2
) {
  override def tick(ticks: Int) {
    super.tick(ticks)

    if (target touches this) {
      onTouch(this)(ticks)
    }

    // hunt player :)
    if (ticks % 10 == 0) {
      if (pos.y > target.pos.y) { goUp; if (move) return }
      if (pos.x > target.pos.x) { goLeft; if (move) return }
      if (pos.y + size.y < target.pos.y) { goDown; if (move) return }
      if (pos.x + size.x < target.pos.x) { goRight; if (move) return }
    }
  }
  
  override def move: Boolean = {
    pos += direction
    val thisBlock = level.at((pos.x + 16) / 32, (pos.y + 16) / 32)
    val canMove = !level.solidBlocks.contains(thisBlock) &&
      !(obstacles.foldLeft(false) { (b, s) => b || s.touches(this) })
    if (!canMove) pos -= direction
    canMove
  }
}

// Now for level 2
class Level2 extends ScrollingScene {
  // Number of juggling balls collected
  var balls: Int = 0
  
  val ouch1 = new Entity("Ouch! The skeleton steals 200 coins", pos = Vec2d(70, 140), active = false)
  val ouch2 = new Entity("and you are thrown back to the start", pos = Vec2d(70, 155), active = false)
  addStaticEntity(ouch1)
  addStaticEntity(ouch2)
  
  // Load the level. Also sets everthing that is supposed to happen on touch of coins, skeletons and juggling balls
  val level = new ScrollingLevel("untitled", onCoinCollect = { _ =>
    Sii.score += 100
    scoreText.text = "Score: " + Sii.score
    Sound.playSound("pling")
  }, onBallCollect = { i =>
    balls += 1
    ballsText.text = "Juggling Balls: " + balls + " / 3"
    Sound.playSound("ironcur1")
    if (balls == 3) {
      addStaticEntity(new Entity(new FontAnimation("Found all juggling balls!", Color.BLACK, 22), Vec2d(50, 100)))
      addStaticEntity(new Entity(new FontAnimation("Found all juggling balls!", Color.WHITE, 22), Vec2d(48, 98)))
      at(i + 150, { t => Sound.stopMusic; Panel.switchToScene(new Intermission2) })
    }
  }, onMonsterTouch = { skel => { t =>
    Sound.playSound("sound8")
    ouch1.setActive
    ouch2.setActive
    at(t + 80, ouch1.setPassive _)
    at(t + 80, ouch2.setPassive _)
    Sii.score -= 200
    if (Sii.score < 0) Sii.score = 0
    scoreText.text = "Score: " + Sii.score
    player.pos = spawn
    skel.pos = skel.originalPosition
  }})
  
  // Player
  val player: Player = new Player(speed = 3, skin = "2") {
    override def move: Boolean = {
      pos += direction
      val thisBlock = level.at((pos.x + 16) / 32, (pos.y + 16) / 32)
      val canMove = !level.solidBlocks.contains(thisBlock) &&
        !(obstacles.foldLeft(false) { (b, s) => b || s.touches(this) })
      if (!canMove) pos -= direction
      canMove
    }
  }
  
  val spawn = Vec2d(715, 580)
  player.pos = spawn
  player.stop

  level.solids.foreach(player.addObstacle _)
  level.belowLayer(player).foreach(addEntity _)
  addEntity(player)
  
  // Score and Balls display
  addStaticEntity(new Entity(new Frame("coin3"), pos = Vec2d(50, 2)))
  val scoreText = new FontAnimation("Score: " + Sii.score, Color.white, 11)
  addStaticEntity(new Entity(scoreText, pos = Vec2d(70, 13)))
  
  addStaticEntity(new Entity(new Frame("ball1"), pos = Vec2d(140, -7)))
  val ballsText = new FontAnimation("Juggling Balls: " + balls + " / 3", Color.white, 11)
  addStaticEntity(new Entity(ballsText, pos = Vec2d(170, 13)))

  level.aboveLayer(player).foreach(addEntity _)
  
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
