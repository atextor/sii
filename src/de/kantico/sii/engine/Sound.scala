package de.kantico.sii.engine

// javax.sound.sample.AudioSystem complains: javax.sound.sampled.LineUnavailableException:
// Failed to allocate clip data: Requested buffer too large.
// Why? :( So we use the applet audio API which for some reason works...
import java.applet.{Applet, AudioClip}

object Sound {
  var music: AudioClip = null
  
  private def clip(c: String) = Applet.newAudioClip(Sound.getClass.getClassLoader.getResource(c + ".wav"))
  
  def playMusic(m: String) {
    music = clip(m)
    music.loop
  }
  
  def stopMusic = if (music != null) music.stop
  def playSound(s: String) = clip(s).play
}
