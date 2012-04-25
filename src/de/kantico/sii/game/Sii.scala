package de.kantico.sii.game

import javax.swing.JFrame

import de.kantico.sii.engine.{Panel, FrameImage}

object Sii {
  var score: Int = 0
  
  def main(args: Array[String]) {
	val frame = new JFrame("Studying in Ireland")
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	frame.setResizable(false)
	
	Panel.scene = new TitleScene
	Panel.tickTimer.start
		
	frame.add(Panel)
	frame.setIconImage(FrameImage("player_lf1"))
	frame.pack
	frame.setVisible(true)
  }
}

