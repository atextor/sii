package de.kantico.sii.engine

import java.awt.Color
import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.Graphics
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JPanel
import javax.swing.Timer

import SwingImplicits._

object Panel extends JPanel {
  abstract class SceneState
  case object Normal extends SceneState
  case object FadingIn extends SceneState
  case object FadingOut extends SceneState
  
  var sceneState: SceneState = Normal
  var scene: Scene = null
  var fadingProgress = 0
  var nextScene: Scene = null
  var ticks: Int = -1
  
  val image = new BufferedImage(320, 240, BufferedImage.TYPE_INT_ARGB)
//  val drawTimer = new Timer(40, { e: ActionEvent => repaint() })
  
  val tickTimer = new Timer(40, { e: ActionEvent => sceneState match {
    case Normal =>
      ticks += 1
      scene.tick(ticks)
    case FadingIn =>
      fadingProgress -= 5
      if (fadingProgress <= 0) {
        ticks = 0
        sceneState = Normal
      }
    case FadingOut =>
      fadingProgress += 5
      if (fadingProgress >= 150) {
        ticks = 0
        sceneState = FadingIn
        scene = nextScene
      }
    case _ =>
  }
  repaint()
  })
  
  setFocusable(true)
  
//  Music.start
  
  this.addKeyListener(new KeyAdapter { 
    override def keyPressed(e: KeyEvent) { if (sceneState == Normal) scene.keyPressed(e.getKeyCode) }
    override def keyReleased(e: KeyEvent) { if (sceneState == Normal) scene.keyReleased(e.getKeyCode) }
  })
  
  override val getPreferredSize: Dimension = new Dimension(640, 480)
  
  override def paintComponent(g: Graphics) {
    val g2 = image.getGraphics
    scene.draw(g2)
    if (sceneState != Normal) {
      g2.setColor(Color.black)
      val (x1, y1, x2, y2) = (fadingProgress, fadingProgress, 320 - fadingProgress, 240 - fadingProgress)
      g2.fillRect(0, 0, 320, y1)
      g2.fillRect(0, y2, 320, 240)
      g2.fillRect(0, 0, x1, 240)
      g2.fillRect(x2, 0, 320, 240)
    }
    g.drawImage(image.getScaledInstance(getPreferredSize.width, getPreferredSize.height, Image.SCALE_FAST), 0, 0, null)
  }
  
  def switchToScene(s: Scene) {
    nextScene = s
    fadingProgress = 0
    sceneState = FadingOut
  }
  
}