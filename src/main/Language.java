package main;

public class Language
{
	
	// Wörter/Sätze hier einfügen
	
	public static String pacman;
	public static String settings;
	public static String play;
	public static String quit;
	public static String back;
	public static String lan;
	public static String mazemaker;
	public static String save;
	public static String load;
	public static String mirrorhorizontally;
	public static String mirrorvertically;
	public static String checkerboard;
	public static String preview;
	public static String override;
	public static String fileexists;
	public static String savesucces;
	public static String clear;
	public static String audio;
	public static String showwalkable;
	public static String renderscale;
	public static String colorselection;
	public static String ready;
	public static String gameover;
	public static String copyright1;
	public static String copyright2;
	public static String limitFPStoTicks;
	public static String mminfo1;
	public static String mminfo2;
	public static String mminfo3;
	
	
	
	
	
	// Wörter/Sätze Ende
	
	public enum LANG // alle möglichen Sprachen werden festgelegt
	{
		EN,
		DE
	};
	
	public static LANG language;
	
	
	public static void setLanguage(LANG lang)
	{
		language = lang;
		
		if(lang == LANG.DE)
		{
			// Sprache auf Deutsch stellen
			pacman = "Pac-Man";
			settings = "Optionen";
			play = "Spielen";
			quit = "Beenden";
			back = "Zurueck";
			lan = "DE";
			mazemaker = "Maze-Maker";
			save = "Speichern";
			load = "Laden";
			mirrorhorizontally = "X-Spiegel";
			mirrorvertically = "Y-Spiegel";
			checkerboard = "Schachbrett";
			preview = "Vorschau-Fliese";
			override = "Ueberschreiben";
			fileexists = "Datei existiert bereits!";
			savesucces = "Speichern erfolgreich!";
			clear = "Leeren";
			audio = "Ton";
			showwalkable = "Zeige Begehbare";
			renderscale = "Render Aufloesung";
			colorselection = "Farbauswahl";
			ready = "Bereit!";
			gameover = "GAME OVER";
			copyright1 = "Replikat von Pac-Man (© NAMCO, 1980)";
			copyright2 = "von CedGames";
			limitFPStoTicks = "FPS Limit";
			mminfo1 = "0,1,2,3,4,5: Fliesen";
			mminfo2 = "R: Rotieren";
			mminfo3 = "Mausrad: Fliese picken";
		}
		else 
		{
			/*
			wenn keine der zuvorigen Sprachen gewählt wurde muss Englisch ausgewählt werden, ineffizient,
			da Englisch am Wahrscheinlichsten ist, fängt dafür aber Eingabe-Fehler ab
			*/
			
			// Sprache auf Englisch stellen
			pacman = "Pac-Man";
			settings = "Settings";
			play = "Play";
			quit = "Quit";
			back = "Back";
			lan = "EN";
			mazemaker = "Maze-Maker";
			save = "Save";
			load = "Load";
			mirrorhorizontally = "X-Mirror";
			mirrorvertically = "Y-Mirror";
			checkerboard = "Checkerboard";
			preview = "Preview-Tile";
			override = "Override";
			fileexists = "File already exists!";
			savesucces = "Saving successful!";
			clear = "Clear";
			audio = "Sound";
			showwalkable = "Show walkable";
			renderscale = "Renderscale";
			colorselection = "Color Selection";
			ready = "READY!";
			gameover = "GAME OVER";
			copyright1 = "Replica of Pac-Man (© NAMCO, 1980)";
			copyright2 = "by CedGames";
			limitFPStoTicks = "limit FPS";
			mminfo1 = "0,1,2,3,4,5: Tiles";
			mminfo2 = "R: Rotate Tile";
			mminfo3 = "Mouse3: Pick Tile";
		}
		
	}
}
