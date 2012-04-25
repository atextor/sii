package de.kantico.sii.engine

import java.awt.Graphics

import scala.collection.mutable.ListBuffer

abstract class ScrollingScene extends Scene {
  val level: Level
  val player: Movable

  val staticEntities: ListBuffer[Entity] = ListBuffer()

  def addStaticEntity(e: Entity): ListBuffer[Entity] = staticEntities += e

  override def draw(g: Graphics) {
    level.draw(g, player.pos)
    g.translate(-player.pos.x + 144, -player.pos.y + 104)
    entities.foreach(_.draw(g))
    g.translate(player.pos.x - 144, player.pos.y - 104)
    staticEntities.foreach(_.draw(g))
  }
}
