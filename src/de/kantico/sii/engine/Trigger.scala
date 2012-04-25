package de.kantico.sii.engine

object Trigger {
  type Action = Int => Unit
  implicit def noArg2intArg(f: (() => Unit)): Action = { i => f() }
}

object NullAction extends Trigger.Action {
  def apply(v: Int) = {}
}

//class Trigger(sze: Vec2d, pos: Vec2d, action: (Int => Unit)) extends Entity(new InvisibleAnimation(sze), pos) {
class Trigger(size: Vec2d,
    pos: Vec2d,
    rearm: Boolean = true,
    subject: Movable,
    enterAction: Trigger.Action,
//    leaveAction: Trigger.Action = NullAction) extends Entity(new BoxAnimation(size), pos) {
    leaveAction: Trigger.Action = NullAction) extends Entity(new NullAnimation(size), pos) {
  var triggered = false 
  
  override def tick(ticks: Int) {
    if ((subject touches this) && !triggered) {
      enterAction(ticks)
      triggered = true
    } else if (!(subject touches this) && triggered) {
      leaveAction(ticks)
      if (rearm) triggered = false else alive = false
    }
  }
}


