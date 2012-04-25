package de.kantico.sii.engine

object Vec2d {
  val left = Vec2d(-1, 0)
  val down = Vec2d(0, 1)
  val right = Vec2d(1, 0)
  val up = Vec2d(0, -1)
  val nowhere = Vec2d(0, 0)
}

case class Vec2d(x: Int, y: Int) {
  def +(v: Vec2d): Vec2d = Vec2d(x + v.x, y + v.y)
  def -(v: Vec2d): Vec2d = Vec2d(x - v.x, y - v.y)
  def *(f: Int): Vec2d = Vec2d(x * f, y * f)
  def /(f: Int): Vec2d = Vec2d(x / f, y / f)
}