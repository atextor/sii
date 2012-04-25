package de.kantico.sii.engine

import scala.collection.mutable.ListBuffer

trait Movable {
  var pos: Vec2d
  var size: Vec2d
  var direction: Vec2d
  val obstacles: ListBuffer[Movable]
  
  def addObstacle(m: Movable): ListBuffer[Movable] = obstacles += m
  
  def removeObstacle(m: Movable): ListBuffer[Movable] = obstacles -= m
  
  def middle: Vec2d = Vec2d(pos.x + size.x / 2, pos.y + size.y / 2)
  
  def touchTopLeft = pos
  
  def touchBottomRight = pos + size
  
  def touches(other: Movable): Boolean = {
    val thisBR = touchBottomRight
    val otherBR = other.touchBottomRight
    !((other.touchTopLeft.x > thisBR.x) ||
      (other.touchTopLeft.y > thisBR.y) ||
      (touchTopLeft.x > otherBR.x) ||
      (touchTopLeft.y > otherBR.y))
  }
      
  def move: Boolean = {
    pos += direction
    val canMove = !(obstacles.foldLeft(false) { (b, s) => b || s.touches(this) })
    if (!canMove) pos -= direction
    canMove
  }
}