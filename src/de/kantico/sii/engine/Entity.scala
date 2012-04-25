package de.kantico.sii.engine

import java.awt.Graphics

import scala.collection.mutable.ListBuffer

class Entity(var ani: Animation, override var pos: Vec2d = Vec2d.nowhere, var active: Boolean = true)
    extends Movable with Drawable with Tickable {
  var size = ani.size
  var direction = Vec2d.nowhere
  var alive = true
  val obstacles: ListBuffer[Movable] = ListBuffer()
  
  def draw(g: Graphics) =  if (active) ani.draw(g, pos)
  def tick(ticks: Int) = if (active) ani.tick(ticks)
  def setActive = this.active = true
  def setPassive = this.active = false
}

class Solid(size: Vec2d, pos: Vec2d) extends Entity(new NullAnimation(size), pos)
