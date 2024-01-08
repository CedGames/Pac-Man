package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import main.Language.LANG;

public class Properties
{
	public static float audio_volume = 0.5f;
	public static float music_volume = 0.5f;
	public static LANG language = LANG.EN;
	public static String resourcepack = "default";
	public static int renderscale = 2;
	
	
	public void saveProperties()
	{	
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("pacman\\game.properties"));

		    writer.write("audio_volume=" + audio_volume);
		    writer.write(System.lineSeparator());
		    writer.write("music_volume=" + music_volume);
		    writer.write(System.lineSeparator());
		    writer.write("language=" + language);
		    writer.write(System.lineSeparator());
		    writer.write("resourcepack=" + resourcepack);
		    writer.write(System.lineSeparator());
		    writer.write("renderscale=" + renderscale);
		    writer.write(System.lineSeparator());
		    
			writer.close();
			
			System.out.println("Properties saved");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void loadProperties()
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("pacman\\game.properties"));
			audio_volume = Float.parseFloat(reader.readLine().substring(13));
			music_volume = Float.parseFloat(reader.readLine().substring(13));
			language = LANG.valueOf(reader.readLine().substring(9));
			resourcepack = reader.readLine().substring(13);
			renderscale = Integer.parseInt(reader.readLine().substring(12));
			reader.close();
			
			System.out.println("Properties loaded");
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			System.out.println("Properties are null");
			saveProperties();
		}
	}
}
