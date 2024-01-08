package level;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class MazeLoader
{
	public static String dir = "pacman";
	private static String subDir = "levels";
	private static String[] sourceLevel = {"empty", "level0", "level1", "level2"};
	
	public void saveMaze(int[][] Grid, String name)
	{	
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(dir + "\\" + subDir + "\\" + name + ".level"));
			for(int y = 0; y < 31; y++)
		    {
		    	
		    	for(int x = 0; x < 28; x++)
		    	{
		    		if(Grid[x][y] < 10) writer.write("0" + Integer.toString(Grid[x][y])); // schönere formatierung in der datei
		    		else writer.write(Integer.toString(Grid[x][y]));
		    		writer.write(" ");

		    	}
		    	//writer.write("\n");
		    	writer.write(System.lineSeparator()); // funktioniert jetzt auch bei anderen Betriebssystemen (Eigentlich unnötig?)
		    }
			writer.close();
			
			System.out.println("Saved '" + name + "'");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public int[][] loadMaze(String name)
	{
		int[][] Grid = new int[28][31];

		String[] row = new String[31];
		String[] col = new String[28];
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(dir + "\\" + subDir + "\\" + name + ".level"));
			for(int y = 0; y < 31; y++)
		    {
		    	row[y] = reader.readLine();
		    	col = row[y].split(" "); // https://www.baeldung.com/string/split
		    	
		    	//System.out.println(y+1 + ": " + line[y]);
		    	
				for(int x = 0; x < 28; x++)
				{
					Grid[x][y] = Integer.parseInt(col[x]);
				}
		    }
			reader.close();
			
			System.out.println("Loaded '" + name + "'");
		}
		catch(Exception e)
		{
			System.out.println("FileNotFoundException:" + name);
			return Grid;
			//e.printStackTrace();
			
		}
		
		return Grid;
	}
	
	public int[][] loadSrcMaze(String name)
	{
		int[][] Grid = new int[28][31];
		String[] row = new String[31];
		String[] col = new String[28];
		
		try
		{
			//BufferedReader reader = new BufferedReader(new FileReader(dir + "\\" + subDir + "\\" + name + ".level"));
			InputStream is = getClass().getResourceAsStream("/levels/" + name + ".level");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			for(int y = 0; y < 31; y++)
		    {
		    	row[y] = reader.readLine();
		    	col = row[y].split(" "); // https://www.baeldung.com/string/split
		    	
		    	//System.out.println(y+1 + ": " + line[y]);
		    	
				for(int x = 0; x < 28; x++)
				{
					Grid[x][y] = Integer.parseInt(col[x]);
				}
		    }
			reader.close();
			
			System.out.println("Loaded '" + name + "'");
		}
		catch(Exception e)
		{
			System.out.println("FileNotFoundException");
			return Grid;
			//e.printStackTrace();

		}

		return Grid;
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
	
	public static String[] sourceFileList()
	{
		return sourceLevel;
	}
	
	public static String sourceFileList(int index)
	{
		return sourceLevel[index];
	}
	
	public static String[] fileList()
	{
		try
		{
			File file = new File(dir + "\\" + subDir + "\\");
			String[] fileList = file.list();
			/*
			for(int i = 0; i < fileList.length; i++)
			{
				System.out.println(fileList[i]);
			}
			*/
			return fileList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static String fileList(int index)
	{
		try
		{
			File file = new File(dir + "\\" + subDir + "\\");
			String[] fileList = file.list();
			return fileList[index];
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getFilePath()
	{
		try
	    {
			File file = new File(dir + "\\" + subDir + "\\");
			return file.getAbsolutePath();
	    }
		catch(Exception e)
	    {
			e.printStackTrace();
	    }
		return null;
	}
	
	public static boolean levelExists(String name)
	 {
	    boolean exists;
	    try (Reader reader = new FileReader(dir + "/" + subDir + "/" + name + ".level")) {
	      exists = true;
	    } catch (Exception e) {
	      exists = false;
	    }
	    return exists;
	 }
	
}
