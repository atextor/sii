package de.kantico.sii.game

import java.awt.Graphics

import scala.collection.mutable.Map
import scala.xml.{XML, NodeSeq, Node}

import de.kantico.sii.engine.{Vec2d, FrameImage, Solid, Entity, Trigger, SpriteAnimation, Frame, Level}
import de.kantico.sii.engine.Trigger._
import de.kantico.sii.engine.FontAnimation._

class ScrollingLevel(name: String, onCoinCollect: Action, onBallCollect: Action, onMonsterTouch: Skeleton => Action)
   extends Level {
  private val doc = XML.load(this.getClass.getClassLoader.getResourceAsStream(name + ".tmx"))
  val blockSize = 32
  val size = Vec2d((doc \\ "map" \ "@width").text.toInt, (doc \\ "map" \ "@height").text.toInt)
  val levelData = doc \\ "@gid" map (_.text.toInt)
  val gfx = levelData map (t => FrameImage("tile" + t))
  
  val solidBlocks: Set[Int] = (doc \\ "map" \ "tileset" \\ "@id").map(_.text.toInt + 1).toSet
  
  // Tiles in this game are 32x32 while the tileset used to create the level files uses 48x48 versions
  // of the same tiles
  private def coord(s: String): Int = s.toInt * blockSize / 48
  
  def at(x: Int, y: Int) = levelData(x + y * size.x)

  def gfxAt(x: Int, y: Int) = FrameImage("tile" + at(x, y))
  
  def draw(g: Graphics, playerPos: Vec2d) {
    val blockOffsetX = (playerPos.x + 16) / 32
    val blockOffsetY = (playerPos.y + 16) / 32
    
    for (x <- blockOffsetX - 5 to blockOffsetX + 5; y <- blockOffsetY - 4 to blockOffsetY + 4) {
      val tile = if (x > 0 && x < size.x && y > 0 && y < size.y) gfxAt(x, y) else gfxAt(0, 0)
      g.drawImage(tile, x * blockSize - playerPos.x + 144, y * blockSize - playerPos.y + 104, null)
    }
  }
  
  def solids: Seq[Solid] = (doc \\ "object").filter(_.attribute("gid") == None) map { o =>
    val size = Vec2d(coord(o \ "@width" text), coord(o \ "@height" text))
    val pos = Vec2d(coord(o \ "@x" text), coord(o \ "@y" text))
    new Solid(size, pos)
  }
  
  def belowLayer(player: Entity): Seq[Entity] = objects(player, "Below")
  
  def aboveLayer(player: Entity): Seq[Entity] = objects(player, "Above")
  
  private def objectLayer(l: String) = ((doc \\ "objectgroup") filter { n => (n \ "@name").toString == l }) \\ "object"
  
  private def objects(player: Entity, layer: String): Seq[Entity] = 
      objectLayer(layer).filterNot(_.attribute("gid") == None) flatMap { o =>
    val pos = Vec2d(coord(o \ "@x" text), coord(o \ "@y" text) - 32)
    val gid = (o \ "@gid" text)
    gid match {
      // Sign
      case "261" =>
        val size = Vec2d(blockSize, blockSize)
        val text = new Entity((o \\ "@value").text, pos = (pos - Vec2d(50, 20)), active = false)
        new Trigger(size = Vec2d(blockSize, blockSize), pos = pos, subject = player,
            enterAction = text.setActive _, leaveAction = text.setPassive _) ::
          text ::
          new Entity(new Frame("tile" + gid), pos) ::
          Nil
      // Coin
      case "262" =>
        new Coin(startAt = pos, dir = Vec2d.nowhere, collector = player, onCollect = onCoinCollect) :: Nil
      // Juggling ball
      case "263" =>
        new Ball(startAt = pos, dir = Vec2d.nowhere, collector = player, onCollect = onBallCollect) :: Nil
      // Skeleton
      case "264" =>
        val skel = new Skeleton(level = this, target = player, originalPosition = pos, onTouch = onMonsterTouch)
        solids.foreach(skel.addObstacle _)
        skel.pos = pos
        skel :: Nil
      case _ => List(new Entity(new Frame("tile" + gid), pos))
    }
  }
  
  def tick(ticks: Int) { }
}
