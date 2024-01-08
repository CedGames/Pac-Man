package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class WriteToFile
{
	public void saveExample(String content, String name)
	{	
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("pacman\\" + name + ".txt"));
			writer.write(content);
			writer.close();
			
			System.out.println("Saved '" + name + "'");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void saveHighscore(int highscore)
	{
		if(highscore <= loadHighscore()) return;
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter("pacman\\highscore.txt"));
			writer.write(String.valueOf(highscore));
			writer.close();
			
			//System.out.println("Highscore File Saved (" + highscore + ")");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int loadHighscore()
	{
		int highscore = 0;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader("pacman\\highscore.txt"));
			highscore = Integer.valueOf(reader.readLine());
			reader.close();
			
			//System.out.println("Highscore File Loaded (" + highscore + ")");
			
			return highscore;
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return 0;
		}
	}
}
