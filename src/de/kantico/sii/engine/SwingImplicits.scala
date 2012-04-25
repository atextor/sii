package de.kantico.sii.engine

import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

object SwingImplicits {
  implicit def funToActionListener(f: (ActionEvent => Unit)): ActionListener = new ActionListener {
    def actionPerformed(e: ActionEvent) { f(e) }
  }
  
  implicit def funToKeyAdapter(keyPressed: (KeyEvent => Unit), keyReleased: (KeyEvent => Unit)): KeyAdapter = new KeyAdapter {
    override def keyPressed(e: KeyEvent) { keyPressed(e) }
    override def keyReleased(e: KeyEvent) { keyReleased(e) }
  }
}