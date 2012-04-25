package de.kantico.sii.engine

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

import scala.collection.mutable.Map

trait Animation extends Tickable {
  def size: Vec2d
  def draw(g: Graphics, pos: Vec2d)
}

class SpriteAnimation(val imageName: String, val numPhases: Int, delay: Int) extends Animation {
  private var repeat: Int = 0
  private var phase: Int = 0
  
  val frames: List[Image] = (1 to numPhases).map { x => FrameImage(imageName + x) }.toList
  
  val size = {
    val f = FrameImage(imageName + "1")
    Vec2d(f.getWidth, f.getHeight)
  }
  
  def tick(ticks: Int) {
    repeat += 1
    if (repeat >= delay) {
      repeat = 0
      phase = (phase + 1) % numPhases
    }
  }
  
  def draw(g: Graphics, pos: Vec2d) {
    g.drawImage(frames(phase), pos.x, pos.y, null)
  }
}

object FrameImage {
  private val frames: Map[String, BufferedImage] = Map() 
  def apply(name: String): BufferedImage = frames.getOrElseUpdate(name,
    ImageIO.read(this.getClass.getClassLoader.getResource(name + ".png")))
}

class Frame(val imageName: String) extends Animation {
  val frame = FrameImage(imageName)
  val size = Vec2d(frame.getWidth, frame.getHeight)
  
  def tick(ticks: Int) { }
  def draw(g: Graphics, pos: Vec2d) {
    g.drawImage(frame, pos.x, pos.y, null)
  }
}

object FontAnimation {
  val font = Font.createFont(Font.TRUETYPE_FONT, FontAnimation.getClass.getClassLoader.getResourceAsStream("cure.se.ttf"))
  implicit def str2SmallText(t: String) = new SmallText(t)
}

class FontAnimation(var text: String, var color: Color, val fontSize: Int) extends Animation {
  override def size = Vec2d(text.length * 7 * fontSize, 9 * fontSize)
  private val font = FontAnimation.font.deriveFont(Font.PLAIN, fontSize)
  
  def draw(g: Graphics, pos: Vec2d) {
    g.setFont(font)
    g.setColor(color)
    g.drawString(text, pos.x, pos.y)
  }
  
  def tick(ticks: Int) { }
}

class SmallText(text: String) extends FontAnimation(text, Color.white, 11)

class FadeAnimation(fadeOut: Boolean) extends Animation {
  var fadingProgress = (if (fadeOut) 0 else 150)
  
  val size = Vec2d(320, 240)
  
  def draw(g: Graphics, pos: Vec2d) {
    g.setColor(Color.black)
    val (x1, y1, x2, y2) = (fadingProgress, fadingProgress, 320 - fadingProgress, 240 - fadingProgress)
    g.fillRect(0, 0, 320, y1)
    g.fillRect(0, y2, 320, 240)
    g.fillRect(0, 0, x1, 240)
    g.fillRect(x2, 0, 320, 240)
  }
  
  def tick(ticks: Int) {
    fadingProgress += (if (fadeOut) 5 else -5)
  }
}

class BlackBox() extends Animation {
  val size = Vec2d(320, 240)
  def draw(g: Graphics, pos: Vec2d) {
    g.setColor(Color.black)
    g.fillRect(pos.x, pos.y, size.x, size.y)
  }
  def tick(ticks: Int) { }
}

class BoxAnimation(val size: Vec2d) extends Animation {
  def draw(g: Graphics, pos: Vec2d) {
    g.setColor(Color.black)
    g.drawRect(pos.x, pos.y, size.x, size.y)
  }
  
  def tick(ticks: Int) { }
}

class NullAnimation(val size: Vec2d) extends Animation {
  def draw(g: Graphics, pos: Vec2d) { }
  def tick(ticks: Int) { }
}
