package main;

import java.awt.image.BufferedImage;
import java.io.File;

public class AssetManager
{
	public static String dir = "pacman";
	private static String subDir = "resourcepacks";
	
	// Variable names
	
	public static String button_click = "button_click.wav";
	public static String pacman_beginning = "pacman_beginning.wav";
	public static String pacman_chomp = "pacman_chomp.wav";
	public static String pacman_death = "pacman_death.wav";
	public static String pacman_eatfruit = "pacman_eatfruit.wav";
	public static String ghost_siren = "ghost_siren.wav";
	
	
	public static String cursor = "/assets/cursor/cursor7_1.png"; // https://www.seekpng.com/ipng/u2e6r5t4q8q8t4y3_piq-mouse-pointer-pixel-art-by-cesarloose-pixel/
	public static String font = "assets/monogram.ttf";
	
	public static String ghosts_items = "/assets/ghosts_items.png";
	public static String pacman = "/assets/pacman.png";
	public static String level = "/assets/level.png";
	public static String icon = "/assets/icon3.png";
    public static String logo = "/assets/logo.png";
	 
	public static BufferedImage ss_ghosts_items = null;
	public static BufferedImage ss_level = null;
	public static BufferedImage ss_pacman = null;
	
	// Variable names end 
	
	public static void loadSprites()
	{
		BufferedImageLoader loader = new BufferedImageLoader();
		try
		{
			ss_ghosts_items = loader.loadImage(ghosts_items);
			ss_level = loader.loadImage(level);
			ss_pacman = loader.loadImage(pacman);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void loadResourcePack(String name)
	{
		String path = dir + "/" + subDir + "/" + name + "/";
		BufferedImageLoader loader = new BufferedImageLoader();
		try
		{
			ss_ghosts_items = loader.loadExternalImage(path + ghosts_items);
			ss_level = loader.loadExternalImage(path + level);
			ss_pacman = loader.loadExternalImage(path + pacman);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
					
	}
	
	public static void createFileStructure()
	{
	    try
	    {
	      File fileDir = new File(dir);
	      fileDir.mkdir();
	      File fileSubDir = new File(dir + "\\" + subDir + "\\");
	      fileSubDir.mkdir();
	    } catch(Exception e)
	    {
	      System.out.println("Datei-Struktur konnte nicht erstellt werden!" );
	    }
	}
	
}
