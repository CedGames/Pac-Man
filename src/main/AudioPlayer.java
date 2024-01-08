package main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioPlayer 
{
	//  https://www.youtube.com/watch?v=nUHh_J2Acy8
	private static Clip play;
	private static Clip pacman_chomp; 
	private static Clip button_click;
	private static Clip music;

public static void playMenuMusic()
{
	try
	{
		AudioInputStream menuSound = AudioSystem.getAudioInputStream(new File("src/res/pacman_beginning.wav"));
		play = AudioSystem.getClip();
		play.open(menuSound);
	    
		FloatControl volume = (FloatControl) play.getControl(FloatControl.Type.MASTER_GAIN);
	    volume.setValue(Properties.music_volume);
	    play.loop(Clip.LOOP_CONTINUOUSLY);
	 }
	catch (Exception e)
	{
	   e.printStackTrace();
	}

}

	 public static void playGameMusic()
	 {
		 try
		 {

			 AudioInputStream gameSound = AudioSystem.getAudioInputStream(new File("src/res/pacman_intermission.wav"));
			 play = AudioSystem.getClip();
			 play.open(gameSound);
			 play.loop(Clip.LOOP_CONTINUOUSLY);
			 
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
	 }
	 /*
	 public void playPacmanChomp()
	 {
		 if(!Game.audio) return;
		 if(play == null || !play.isRunning())
		 {
			 if(pacman_chomp == null || !pacman_chomp.isRunning())
			 {
					 try
					 	{
						 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + AssetManager.pacman_chomp);
						 	is = new BufferedInputStream(is);
						 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
						 	pacman_chomp = AudioSystem.getClip();
						 	pacman_chomp.open(sfx);
						 	
						 	//pacman_chomp.loop(Clip.LOOP_CONTINUOUSLY);
						 	pacman_chomp.start();
					 	 }
					 	catch (Exception e)
					 	{
					 	   e.printStackTrace();
					 	}					 
			 }
			 
		 }
		

	 }
	 */
	 int delay = 0;
	 public void playPacmanChomp()
	 {
		 if(!Game.audio) return;
		 if(play == null || !play.isRunning())
		 {
			 if(delay > 16)
			 {
				 try
				 	{
					 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + AssetManager.pacman_chomp);
					 	is = new BufferedInputStream(is);
					 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
					 	pacman_chomp = AudioSystem.getClip();
					 	pacman_chomp.open(sfx);
					 	
					 	//pacman_chomp.loop(Clip.LOOP_CONTINUOUSLY);
					 	pacman_chomp.start();
				 	 }
				 	catch (Exception e)
				 	{
				 	   e.printStackTrace();
				 	}	
				 delay = 0;
			 }
					 				 
		 }

	 }
	 
	 
	 public void playBeginningTune()
	 {
		 if(!Game.audio) return;
		 if(music != null) try {music.close();}catch(Exception e) {};
				 try
				 	{
					 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + AssetManager.pacman_beginning);
					 	is = new BufferedInputStream(is);
					 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
					 	music = AudioSystem.getClip();
					 	music.open(sfx);
					 	music.start();
				 	 }
				 	catch (Exception e)
				 	{
				 	   e.printStackTrace();
				 	}

	 }
	 
	 public void playGhostSiren()
	 {
		 if(!Game.audio) return;
		 if(music != null) try {music.close();}catch(Exception e) {};
				 try
				 	{
					 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + AssetManager.ghost_siren);
					 	is = new BufferedInputStream(is);
					 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
					 	music = AudioSystem.getClip();
					 	music.open(sfx);
					 	music.loop(Clip.LOOP_CONTINUOUSLY);
				 	 }
				 	catch (Exception e)
				 	{
				 	   e.printStackTrace();
				 	}

	 }
	 
	 public void playSFX(String name)
	 {
		 if(!Game.audio) return;
		 if(pacman_chomp != null) pacman_chomp.stop();
		 if(play == null || !play.isRunning())
		 {
				 try
				 	{
					 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + name);
				 		//AudioInputStream sfx = AudioSystem.getAudioInputStream(new File("src/assets/sfx/" + name));
					 	is = new BufferedInputStream(is);
					 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
				 		play = AudioSystem.getClip();
				 		play.open(sfx);

				 	    play.start();
				 	 }
				 	catch (Exception e)
				 	{
				 	   e.printStackTrace();
				 	}
		 }

	 }
	 
	 
	 public void playButtonClick()
	 {
		 if(!Game.audio) return;
		 if(pacman_chomp != null) pacman_chomp.close();
		 if(button_click == null || !button_click.isRunning())
		 {
				 try
				 	{
					 	InputStream is = getClass().getResourceAsStream("/assets/sfx/" + AssetManager.button_click);
				 		//AudioInputStream sfx = AudioSystem.getAudioInputStream(new File("src/assets/sfx/" + name));
					 	is = new BufferedInputStream(is);
					 	AudioInputStream sfx = AudioSystem.getAudioInputStream(is);
					 	button_click = AudioSystem.getClip();
					 	button_click.open(sfx);

					 	button_click.start();
				 	 }
				 	catch (Exception e)
				 	{
				 	   e.printStackTrace();
				 	}
		 }

	 }
	

	 public void stopSound()
	 {
		 if(play != null) play.close();
		 if(pacman_chomp != null) pacman_chomp.close();
		 //if(button_click != null) button_click.close();
	 }
	 
	 public void stopChomp()
	 {
		 if(pacman_chomp != null) pacman_chomp.close();
	 }
	 
	 public Clip getPlay()
	 {
		 return play;
	 }
	 
	 public static void stopMusic()
	 {
		 if(music != null) try {music.close();}catch(Exception e) {};
	 }

}
