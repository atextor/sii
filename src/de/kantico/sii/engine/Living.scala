package de.kantico.sii.engine

class Living(
    val aniLeft: Animation = new NullAnimation(Vec2d.nowhere),
    val aniUp: Animation = new NullAnimation(Vec2d.nowhere),
    val aniRight: Animation = new NullAnimation(Vec2d.nowhere),
    val aniDown: Animation = new NullAnimation(Vec2d.nowhere),
    val speed: Int,
    val startPos: Vec2d = Vec2d(0, 0))
      extends Entity(aniDown, startPos) {
  
  def stop {
    direction = Vec2d.nowhere
  }
  
  def goLeft {
    direction = Vec2d.left * speed
    ani = aniLeft
  }
  
  def goUp {
    direction = Vec2d.up * speed
    ani = aniUp
  }
  
  def goRight {
    direction = Vec2d.right * speed
    ani = aniRight
  }
  
  def goDown {
    direction = Vec2d.down * speed
    ani = aniDown
  }
   
}
