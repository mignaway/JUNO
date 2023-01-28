package audio;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class CustomAudioManager {
	/**
	 * Audio clip where to call methods
	 */
	private Clip audioclip;
	
	/**
	 * 
	 * Reproduce given audio
	 * 
	 * @param audioPath Audio file path
	 * @param playLoop Boolean value to tells if the songs needs to be looped infinitely
	 * @param volume Set value volume from 0 to 1
	 * 
	 */
	
	public void playAudio(String audioPath,boolean playLoop,double volume) {
		try {
			File audiofile = new File(audioPath);
			if(audiofile.exists()) {
				
				// Settings audio
				
				AudioInputStream audioinput = AudioSystem.getAudioInputStream(audiofile);
				audioclip = AudioSystem.getClip();
				audioclip.open(audioinput);
				
				// Looping audio
				
				if(playLoop) audioclip.loop(Clip.LOOP_CONTINUOUSLY);
				
				// Sets volume
				
				FloatControl gainControl = (FloatControl) audioclip.getControl(FloatControl.Type.MASTER_GAIN);        
			    gainControl.setValue(20f * (float) Math.log10(volume));
			    
			    // Start Clip
			    
				audioclip.start();
//				System.out.println("Playing audio: " + audioPath);
				
			} else {
				System.out.println("Audio not found, the game will start without music.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Resume audio instance
	 */
	
	public void resumeAudio() {
		audioclip.start();
	}
	
	/**
	 * Pause audio instance
	 */
	
	public void pauseAudio() {
		audioclip.stop();
	}
}