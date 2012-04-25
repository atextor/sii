package de.kantico.sii.engine

import java.awt.Graphics
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Queue

abstract class Scene extends Drawable with Tickable {
  type Action = (Int => Unit)
  type TimedAction = (Int, Action)
  
  var actions = ListBuffer[TimedAction]()
  val pressedKeys = Queue[Int]()  
  val entities: ListBuffer[Entity] = ListBuffer()
  
  def addEntity(e: Entity): ListBuffer[Entity] = entities += e
  
  def at(ticks: Int, f: Action) { actions += ((ticks, f)) }
  
  def sortActions { actions = actions.sortWith(_._1 < _._1) }
  
  def tick(ticks: Int) {
    var changed = false
    while (actions.size > 0 && actions.head._1 <= ticks) {
      actions.remove(0)._2(ticks)
      changed = true
    }
    if (changed) sortActions
    entities.foreach(_.tick(ticks))
    entities.foreach(_.move)
    entities.filterNot(_.alive).foreach(entities -= _)
  }
  
  def draw(g: Graphics) {
    entities.foreach(_.draw(g))
  }
  
  def processKeys
  
  def keyPressed(keyCode: Int) {
    pressedKeys += keyCode
    processKeys
  }
  
  def keyReleased(keyCode: Int) {
    pressedKeys.dequeueAll(_ == keyCode)
    processKeys
  }
  
}