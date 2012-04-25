package de.kantico.sii.game

import de.kantico.sii.engine.{Living, SpriteAnimation, Vec2d}
  
class Player(speed: Int = 2, skin: String = "") extends Living(
    aniLeft = new SpriteAnimation("player" + skin + "_lf" , numPhases = 2, delay = 7),
    aniUp = new SpriteAnimation("player" + skin + "_bk", numPhases = 2, delay = 7),
    aniRight = new SpriteAnimation("player" + skin + "_rt", numPhases = 2, delay = 7),
    aniDown = new SpriteAnimation("player" + skin + "_fr", numPhases = 2, delay = 7),
    speed = speed
) {
  override def touchTopLeft = pos + Vec2d(6, 20)
  override def touchBottomRight = pos + Vec2d(24, 32)
}
