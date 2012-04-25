package de.kantico.sii.game

import de.kantico.sii.engine.{Animation, Vec2d, Movable, Entity, SpriteAnimation}
import de.kantico.sii.engine.Trigger._

// Collectible: Base class for floating things than can collide with the player, i.e. coins and skulls
class Collectible(animation: Animation, startAt: Vec2d, dir: Vec2d, collector: Movable,
    onCollect: Action) extends Entity(ani = animation, pos = startAt) {
  
  direction = dir
  val rightBorder = 318 - animation.size.x
  
  override def tick(ticks: Int) {
    super.tick(ticks)
    if (collector touches this) {
      onCollect(ticks)
      this.alive = false
    } else if (pos.y < -10) {
      this.alive = false
    }
    
    if (pos.x < 2 || pos.x > rightBorder) {
      direction = direction.copy(x = -direction.x)
    }
  }
  
  // Make up for smaller player hitbox
  override def touchBottomRight = pos + Vec2d(16, 33);
}

// Coins just float and don't do much
class Coin(startAt: Vec2d, dir: Vec2d, collector: Movable, onCollect: Action)
    extends Collectible(new SpriteAnimation("coin", numPhases = 8, delay = 2), startAt, dir, collector, onCollect)

// Skull home in on the player on x axis
class Skull(startAt: Vec2d, dir: Vec2d, collector: Movable, onCollect: Action)
    extends Collectible(new SpriteAnimation("skull", numPhases = 9, delay = 2), startAt, dir, collector, onCollect) {
  
  override def tick(ticks: Int) {
    super.tick(ticks)
    if (ticks % 10 == 0) {
      if ((pos.x > collector.pos.x + collector.size.x) && direction.x > -2)
        direction = direction.copy(x = direction.x - 1)
      if ((pos.x + size.x < collector.pos.x) && direction.x < 2)
        direction = direction.copy(x = direction.x + 1)
    }
  }
}

// Juggling Balls
class Ball(startAt: Vec2d, dir: Vec2d, collector: Movable, onCollect: Action)
    extends Collectible(new SpriteAnimation("ball", numPhases = 8, delay = 4), startAt, dir, collector, onCollect)
