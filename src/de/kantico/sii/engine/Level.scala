package de.kantico.sii.engine

import java.awt.Graphics

trait Level {
  def draw(g: Graphics, offset: Vec2d)
  def solidBlocks: Set[Int]
  def at(x: Int, y: Int): Int
}