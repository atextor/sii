package de.kantico.sii.game

import java.awt.Color
import java.awt.event.KeyEvent._

import java.util.Random

import de.kantico.sii.engine.{Vec2d, BlackBox, Scene, Sound, Panel, Entity, SpriteAnimation, Frame, Movable, Living}
import de.kantico.sii.engine.FontAnimation._
import de.kantico.sii.engine.Trigger._

class DumbSkeleton(override val startPos: Vec2d) extends Living(
    aniLeft = new SpriteAnimation("skeleton_lf", numPhases = 2, delay = Intermission2.random.nextInt(3) + 7),
    aniUp = new SpriteAnimation("skeleton_bk", numPhases = 2, delay = Intermission2.random.nextInt(3) + 7),
    aniRight = new SpriteAnimation("skeleton_rt", numPhases = 2, delay = Intermission2.random.nextInt(3) + 7),
    aniDown = new SpriteAnimation("skeleton_fr", numPhases = 2, delay = Intermission2.random.nextInt(3) + 7),
    speed = 1,
    startPos = startPos
)

object Intermission2 {
  val random = new Random
}

class Intermission2 extends Scene {
  // "Gunther take the hat!" "Banana Banana Banana"
  at(1, { t => Sound.playMusic("royalbanana") })
  
  val player = new Player(speed = 2)
  player.pos = Vec2d(150, 120)
  player.goLeft
  player.stop
  
  // Enter skeletons
  addEntity(new Entity(new BlackBox()))
  addEntity(player)
  
  val skels =
    new DumbSkeleton(startPos = Vec2d(-30, 120)) ::
    new DumbSkeleton(startPos = Vec2d(-60, 120)) ::
    new DumbSkeleton(startPos = Vec2d(-90, 120)) ::
    new DumbSkeleton(startPos = Vec2d(330, 120)) ::
    new DumbSkeleton(startPos = Vec2d(360, 120)) ::
    new DumbSkeleton(startPos = Vec2d(390, 120)) ::
    Nil
  skels.foreach(addEntity)
  
  at(20, { t =>
    skels.splitAt(3)._1.foreach(_.goRight)
    skels.splitAt(3)._2.foreach(_.goLeft)
  })
  at(30, player.goLeft _)
  at(50, player.goRight _)
  at(80, player.goLeft _)
  at(90, player.stop _)
  at(140, { t => skels.foreach(_.stop) })
  
  // Juggling!
  at(170, { t => player.goDown; player.stop })
  val juggle = new Entity(new SpriteAnimation("jugg", numPhases = 12, delay = 2), pos = Vec2d(150, 110))
  at(200, { t => addEntity(juggle) })
  
  // Exeunt skeletons
  at(400, { t =>
    skels.splitAt(3)._1.foreach(_.goLeft)
    skels.splitAt(3)._2.foreach(_.goRight)
  })
  at(490, { t => juggle.active = false })
  at(500, { t => player.goLeft; player.stop })
  
  // Enter guy
  val guy = new Living(
    aniLeft = new SpriteAnimation("guy1_lf", numPhases = 2, delay = 7),
    aniUp= new SpriteAnimation("guy1_bk", numPhases = 2, delay = 7),
    aniRight = new SpriteAnimation("guy1_rt", numPhases = 2, delay = 7),
    aniDown = new SpriteAnimation("guy1_fr", numPhases = 2, delay = 7),
    speed = 2
  )
  guy.pos = Vec2d(-40, 120)
  addEntity(guy)
  
  at(520, guy.goRight _)
  at(600, guy.stop _)
  
  """|Congratulations, you have defeated the undead
     |and rescued the land!
     |Also, HAPPY BIRTHDAY!""".stripMargin.lines.foldLeft(610) { (i, l) =>
    val line = new Entity(l, pos = Vec2d(80, 110))
    at(i, { t => addEntity(line) })
    at(i + 80, { t => line.alive = false })
    i + 90
  }
  
  val flowers = new Entity(new Frame("flowers1"), pos = Vec2d(125, 120))
  at(900, { t => addEntity(flowers) })
  at(940, { t => flowers.direction = Vec2d.right })
  at(970, { t => flowers.direction = Vec2d.nowhere })
  at(1010, { t => flowers.alive = false })
  
  at(1040, { t => guy.goLeft; player.goDown; player.stop })
  at(1060, { t => guy.goRight; guy.stop })
  at(1070, { t => juggle.active = true })
  
  at(1080, { t => addEntity(new Entity("Final Score: " + Sii.score, pos = Vec2d(120, 50))) })
  
  """|Program and design: Andreas Textor
     |Music: Kevin MacLeod
     |Character sprites based on work by Philipp Lenssen
     |Level 2 tileset by Hermann Hillmann
     |Coin/Balls animation by Marc Russel (gfxlib-fuzed)
     |March 2011
     |THE END""".stripMargin.lines.foldLeft(1070) { (i, l) =>
    val line = new Entity(l, pos = Vec2d(70, 170))
    at(i, { t => addEntity(line) })
    at(i + 80, { t => line.alive = false })
    i + 90
  }
  
  sortActions
  
  /** Handle keypresses */
  def processKeys {
    if (pressedKeys.size > 0) pressedKeys.last match {
      case VK_ESCAPE =>
        Sound.stopMusic
        System.exit(1)
      case _ =>
    }
  }
}