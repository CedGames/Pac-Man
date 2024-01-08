package main;

import level.MazeLoader;

public class Play
{
	int pacdots;
	int powerups;
	int[][] level;
	MazeLoader ml;
	
	public Play(String lvl)
	{
		ml = new MazeLoader();
		level = ml.loadSrcMaze(lvl);
		pacdots = 0;
		powerups = 0;
		for(int x = 0; x < level.length; x++)
		{
			for(int y = 0; y < level[0].length; y++)
			{
				if(level[x][y] == 01) pacdots++;
				else if(level[x][y] == 02) powerups++;
			}
		}
	}
	
	public void tick()
	{
		
	}
	
	public void render()
	{
		
	}
}
