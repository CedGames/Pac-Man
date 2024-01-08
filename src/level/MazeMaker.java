package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Stack;

import main.AssetManager;
import main.Game;
import main.SpriteSheet;

public class MazeMaker
{
	/*
	 *  Pac-Man Level bzw. Labyrinthe sind typischer weise 28x31 Zellen groß,
	 *  weshalb auch ich mich für diese Größe entschieden habe. Diese Größe
	 *  ist hier "hardcoded", also unveränderbar festgelegt
	 *  
	 */
	
	private static int[][] Grid = new int[28][31];
	private static Color c1, c2;
	private static BufferedImage[][] wall = new BufferedImage[10][4];
	private static Image[][] wallScaled = new Image[wall.length][wall[0].length];
	
	private static BufferedImage pacman;
	private static Image pacmanScaled;
	
	private static BufferedImage[] ghost = new BufferedImage[4];
	private static Image[] ghostScaled = new Image[4];
	
	private static BufferedImage[] item = new BufferedImage[8];
	private static Image[] itemScaled = new Image[8];
	
	private static BufferedImage pacman_live;
	private static Image pacman_live_scaled;
	
	private static int type = 10;
	private static int rotation = 0;
	private static boolean checkerboard = true;
	private static boolean mirrorhorizontally = false;
	private static boolean mirrorvertically = false;
	//private static boolean smartplace = false;
	public static boolean preview = true;
	public static boolean showwalkable = false;
	public static boolean drawLines = false;
	private static int shift = 0;
	
	private static int maxPacdots = 0;
	private int pacdots = 0, powerups = 0;
	private int fruit = 0;
	
	private static MazeLoader ml;
	
	private static Stack<int[][]> undo_stack = new Stack<int[][]>();
	private static Stack<int[][]> redo_stack = new Stack<int[][]>();
	
	public Graphics g;
	
	public enum STATE
	{
		Base,
		Load,
		LoadSrc,
		Save
	};
	
	public static STATE state = STATE.Base;
	
	public MazeMaker()
	{		
		ml = new MazeLoader();
		c1 = new Color(39, 39, 39);
		c2 = new Color(24, 24, 24);
		
		SpriteSheet ss = new SpriteSheet(AssetManager.ss_level, 8);
		
		// wall[row][col] of 'level.png'
		
		// Misc
		wall[0][0] = ss.grabImage(1, 1, 8, 8); // Empty
		wall[0][1] = ss.grabImage(1, 2, 8, 8); // Pac-Dot
		wall[0][2] = ss.grabImage(1, 3, 8, 8); // Power Up
		wall[0][3] = ss.grabImage(1, 4, 8, 8); // Full Block
		
		// Round Outward Corner
		wall[1][0] = ss.grabImage(2, 1, 8, 8);
		wall[1][1] = ss.grabImage(2, 2, 8, 8);
		wall[1][2] = ss.grabImage(2, 3, 8, 8);
		wall[1][3] = ss.grabImage(2, 4, 8, 8);
		
		// Wall (Two-Sided)
		wall[2][0] = ss.grabImage(3, 1, 8, 8);
		wall[2][1] = ss.grabImage(3, 2, 8, 8);
		wall[2][2] = ss.grabImage(3, 3, 8, 8);
		wall[2][3] = ss.grabImage(3, 4, 8, 8);
		
		// Round Inward Corner
		wall[3][0] = ss.grabImage(4, 1, 8, 8);
		wall[3][1] = ss.grabImage(4, 2, 8, 8);
		wall[3][2] = ss.grabImage(4, 3, 8, 8);
		wall[3][3] = ss.grabImage(4, 4, 8, 8);
		
		// Sharp Inward Corner
		wall[4][0] = ss.grabImage(5, 1, 8, 8);
		wall[4][1] = ss.grabImage(5, 2, 8, 8);
		wall[4][2] = ss.grabImage(5, 3, 8, 8);
		wall[4][3] = ss.grabImage(5, 4, 8, 8);
		
		// Ghost Gate
		wall[5][0] = ss.grabImage(6, 1, 8, 8);
		wall[5][1] = ss.grabImage(6, 2, 8, 8);
		wall[5][2] = ss.grabImage(6, 3, 8, 8);
		wall[5][3] = ss.grabImage(6, 4, 8, 8);
		
		// Wall (One-Sided)
		wall[6][0] = ss.grabImage(7, 1, 8, 8);
		wall[6][1] = ss.grabImage(7, 2, 8, 8);
		wall[6][2] = ss.grabImage(7, 3, 8, 8);
		wall[6][3] = ss.grabImage(7, 4, 8, 8);
		
		// Filled Outward Corner Type 1
		wall[7][0] = ss.grabImage(8, 1, 8, 8);
		wall[7][1] = ss.grabImage(8, 2, 8, 8);
		wall[7][2] = ss.grabImage(8, 3, 8, 8);
		wall[7][3] = ss.grabImage(8, 4, 8, 8);
		
		// Filled Outward Corner Type 2
		wall[8][0] = ss.grabImage(9, 1, 8, 8);
		wall[8][1] = ss.grabImage(9, 2, 8, 8);
		wall[8][2] = ss.grabImage(9, 3, 8, 8);
		wall[8][3] = ss.grabImage(9, 4, 8, 8);	
		
		// Filled Outward Corner Type 3
		wall[9][0] = ss.grabImage(10, 1, 8, 8);
		wall[9][1] = ss.grabImage(10, 2, 8, 8);
		wall[9][2] = ss.grabImage(10, 3, 8, 8);
		wall[9][3] = ss.grabImage(10, 4, 8, 8);

		
		/* Prints initial Grid values (all '0' on initiation)
		for(int x = 0; x < 28; x++)
        {
            for(int y = 0; y < 31; y++)
            {
                System.out.print(Grid[x][y]);
            }
            System.out.println();
        }
        */
		
		SpriteSheet ss_p = new SpriteSheet(AssetManager.ss_pacman, 15);
		pacman = ss_p.grabImage(4, 2, 15, 15); // Pacman
		
		SpriteSheet ss_g = new SpriteSheet(AssetManager.ss_ghosts_items, 14);
		ghost[0] = ss_g.grabImage(5, 1, 14, 14); // Blue Ghost
		ghost[1] = ss_g.grabImage(5, 2, 14, 14); // Orange Ghost
		ghost[2] = ss_g.grabImage(5, 3, 14, 14); // Pink Ghost
		ghost[3] = ss_g.grabImage(5, 4, 14, 14); // Red Ghost
		for(int i = 0; i < 8; i++)
		{
			item[i] = ss_g.grabImage(i+1, 7, 14, 14); // Cherry
		}
		
		pacman_live = ss_g.grabImage(1, 8, 14, 14);
		
		
		rescaleImages();
		
		// Save Levels contained in source file as regular levels so their copies can be edited
		
		save(ml.loadSrcMaze("level0"), "game.level0");
		save(ml.loadSrcMaze("level0_old"), "game.level0_old");
		save(ml.loadSrcMaze("level1"), "game.level1");
		save(ml.loadSrcMaze("Ja7"), "game.Ja7");
		
	}
	
	public void tick()
	{
		calculateEatables();
		
		if(Game.gameState == Game.STATE.Game)
		{
			if(maxPacdots/2 == pacdots)
			{
				maxPacdots = -1;
				inGrid(fruit+5, 13, 17);
				fruit++;
			}
		}
	}
	
	public void render(Graphics g)
	{
		if(Game.gameState == Game.STATE.MazeMaker)
		{
			if(checkerboard)
			{
			int offset = 0;
			for(int x = 0; x < 28; x++)
			{
				
				for(int y = 0; y < 31; y++)
				{
					if(offset % 2 == 0)
					{
						g.setColor(c1);
						g.fillRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S); 
					}
					else
					{
						g.setColor(c2);
						g.fillRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S);
					}
					offset++;
				}
			}
			}
			/*
			else
			{
				g.setColor(Color.black);
				g.fillRect(0, 0, 28*8*Game.S, Game.tHEIGHT*Game.S);
			}
			*/
			// Draw WalkableGrid
			if(showwalkable)
			{
				for(int x = 0; x < 28; x++)
				 {
				    	
				   for(int y = 0; y < 31; y++)
				   {
				    
					   if(isWalkable(x, y))
					   {
						   g.setColor(new Color(0, 255, 0, 50));
						   g.fillRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S);
						   g.drawRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S);
						   //g.fillRoundRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S, Game.S*2, Game.S*2);
				    
				    	//place(wallScaled[wll][rot], x, y, g);	
					   }
					   else
					   {
						   g.setColor(new Color(255, 0, 0, 50));
						   g.fillRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S);
						   g.drawRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S);
						   //g.fillRoundRect(x*8*Game.S, y*8*Game.S, 8*Game.S, 8*Game.S, Game.S*2, Game.S*2);
					   }
				    }
				    	
				   }
			}
			
	// Draw WalkableGrid End
			
			g.setColor(Color.blue);
			g.drawRect(227*Game.S, 160*Game.S, 64*Game.S, 32*Game.S);
			if(!gridContains(4)) g.drawImage(pacmanScaled, 227*Game.S, 160*Game.S, null);
			if(!gridContains(5)) g.drawImage(itemScaled[0], (16+227)*Game.S, (160)*Game.S, null);
			if(!gridContains(6)) g.drawImage(ghostScaled[0], (227)*Game.S, (16+160)*Game.S, null);
			if(!gridContains(7)) g.drawImage(ghostScaled[1], (16+227)*Game.S, (16+160)*Game.S, null);
			if(!gridContains(8)) g.drawImage(ghostScaled[2], (32+227)*Game.S, (16+160)*Game.S, null);
			if(!gridContains(9)) g.drawImage(ghostScaled[3], (48+227)*Game.S, (16+160)*Game.S, null);
			
		}
		else if(Game.gameState == Game.STATE.Game)
		{
			
		}
		
		
		
		// Draw the Grid (of 'level.png')
		for(int x = 0; x < 28; x++)
	    {
	    	
	    	for(int y = 0; y < 31; y++)
	    	{
	    		if(Grid[x][y] == 04)
	    		{
	    			g.drawImage(pacmanScaled, ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 05)
	    		{
	    			g.drawImage(itemScaled[0], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 15)
	    		{
	    			g.drawImage(itemScaled[1], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 25)
	    		{
	    			g.drawImage(itemScaled[2], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 35)
	    		{
	    			g.drawImage(itemScaled[3], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 45)
	    		{
	    			g.drawImage(itemScaled[4], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 55)
	    		{
	    			g.drawImage(itemScaled[5], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 65)
	    		{
	    			g.drawImage(itemScaled[6], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] == 75)
	    		{
	    			g.drawImage(itemScaled[0], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(Grid[x][y] > 05 && Grid[x][y] < 10)
	    		{
	    			g.drawImage(ghostScaled[Grid[x][y]-6], ((x*8)-shift)*Game.S, ((y*8)-4)*Game.S, null);
	    		}
	    		else if(!(Grid[x][y] == 0))
	    		{
	    			int rot = 0;
	    			int wll = 0;
	    			
	    			rot = Grid[x][y] % 10;       // rest = rotation value
	    			wll = (Grid[x][y] - rot)/10; // = type value
	    			place(wallScaled[wll][rot], x, y, g);	
	    		}
	    	}
	    	
	    }
		
		// Draw the Grid End
		
		
		if(Game.gameState == Game.STATE.MazeMaker)
		{
			if(preview)
			{
				
				int sel = getType()+getRotation();
				if(sel == 04)
	    		{
	    			//g.drawImage(pacmanScaled, ((Game.getMX()/8)-shift)*Game.S, Game.getMY()/8, null);
					place(pacmanScaled, Game.getMX()/8/Game.S, Game.getMY()/8/Game.S, g);
	    		}
	    		else if(sel == 05)
	    		{
	    			g.drawImage(itemScaled[0], ((Game.getMX()/8)-shift)*Game.S, Game.getMY()/8, null);
	    		}
	    		else if(sel > 05 && sel < 10)
	    		{
	    			g.drawImage(ghostScaled[sel-6], ((Game.getMX()/8)-shift)*Game.S, Game.getMY()/8, null);
	    		}
	    		else
	    		
				place(wallScaled[type/10][rotation], Game.getMX()/8/Game.S, Game.getMY()/8/Game.S, g);
			}
			
			if(mirrorhorizontally) // visual indicator for the mirroring line/axis
			{
				g.setColor(Color.green);
				g.drawLine(0, 31*Game.S*8/2, 28*Game.S*8, 31*Game.S*8/2);
			}
			if(mirrorvertically) // visual indicator for the mirroring line/axis
			{
				g.setColor(Color.red);
				g.drawLine(28*Game.S*8/2, 0, 28*Game.S*8/2, 31*Game.S*8);
			}
			
			if(drawLines)
			{
				
				g.setColor(new Color(0,0,255, 125));
				for(int x = 0; x < 28; x++)
				{
					g.drawLine(x*8*Game.S, 0, x*8*Game.S, 31*8*Game.S);
				}
				
				for(int y = 0; y < 31; y++)
				{
					g.drawLine(0, y*8*Game.S, 28*8*Game.S, y*8*Game.S);
				}
			}
		}
		

		
	}
	
	
	private static void place(Image img, int x, int y, Graphics g)
	{
		g.drawImage(img, x*8*Game.S, y*8*Game.S, null);
	}
	
	public static void inGrid(int wall, int x, int y)
	{
		undo_stack.push(Grid);
		//System.out.println("Gird00: " + Grid[0][0]);
		redo_stack.clear();
		
		if(wall < 10 && wall > 03) Grid[x][y] = wall;
		else
		{
		
		Grid[x][y] = wall; 
		int rot = wall % 10;       // rest = rotation value
		int wll = (wall - rot); // = type value

		if(mirrorvertically && mirrorhorizontally)
		{
			int tempRot = rot;
			if(wll == 10 || wll == 30 || wll == 40 || wll == 70 || wll == 80)
			{
				if(rot == 0) tempRot = 2;
				else if(rot == 1) tempRot = 3;
				else if(rot == 2) tempRot = 0;
				else if(rot == 3) tempRot = 1;
			}
			else if(wll == 20 || wll == 50 || wll == 60)
			{
				if(rot == 0) tempRot = 2; 
				else if(rot == 1) tempRot = 3;
				else if(rot == 2) tempRot = 0;
				else if(rot == 3) tempRot = 1;
			}
			
			Grid[27-x][30-y] = wll+tempRot;
		}
		
		if(mirrorvertically)
		{	
			int tempRot = rot;
			if(wll == 10 || wll == 30 || wll == 40 || wll == 70 || wll == 80)
			{
				if(rot == 0) tempRot = 1;
				else if(rot == 1) tempRot = 0;
				else if(rot == 2) tempRot = 3;
				else if(rot == 3) tempRot = 2;
			}
			else if(wll == 20 || wll == 50 || wll == 60)
			{
				if(rot == 1) tempRot = 3; 
				else if(rot == 3) tempRot = 1;
			}
			
			Grid[27-x][y] = wll+tempRot;
		}
		
		if(mirrorhorizontally)
		{
			int tempRot = rot;
			if(wll == 10 || wll == 30 || wll == 40 || wll == 70 || wll == 80)
			{
				if(rot == 0) tempRot = 3;
				else if(rot == 1) tempRot = 2;
				else if(rot == 2) tempRot = 1;
				else if(rot == 3) tempRot = 0;
			}
			else if(wll == 20 || wll == 50 || wll == 60)
			{
				if(rot == 0) tempRot = 2; 
				else if(rot == 2) tempRot = 0;
			}
			
			Grid[x][30-y] = wll+tempRot;
		}
		}
	}
	
	public static int getGridWall(int mx, int my)
	{
		return Grid[mx/8/Game.S][my/8/Game.S];
	}
	
	public static boolean isWalkable(int x, int y)
	{
		x = Game.clamp(x, 0, Grid.length-1);
		y = Game.clamp(y, 0, Grid[0].length-1);
		//if(x > 0)
		//Grid[x][y] = 0;
		if(Grid[x][y] == 0 || Grid[x][y] == 01 || Grid[x][y] == 02)
		{
			if(x > 0 && y > 0 && x < Grid.length-1 && y < Grid[0].length-1)
			{
				/* pos:
				 * [0,0][1,0][2,0]
				 * [0,1][1,1][2,1]
				 * [0,2][1,2][2,2]
				 */
				
				// Überprüft, ob die obere linke ecke akzeptable ist
				// Linke Obere Ecke
				if(
				   isEntityOrEmpty(x-1, y-1) ||
				   Grid[x-1][y-1] == 10 ||
				   Grid[x-1][y-1] == 20 || Grid[x-1][y-1] == 23 ||
				   Grid[x-1][y-1] == 31 || Grid[x-1][y-1] == 32 || Grid[x-1][y-1] == 33 ||
				   Grid[x-1][y-1] == 41 || Grid[x-1][y-1] == 42 || Grid[x-1][y-1] == 43 ||
				   Grid[x-1][y-1] == 50 || Grid[x-1][y-1] == 53 ||
				   Grid[x-1][y-1] == 60 || Grid[x-1][y-1] == 63 ||
				   Grid[x-1][y-1] == 70 ||
				   Grid[x-1][y-1] == 80 ||
				   Grid[x-1][y-1] == 90
				  )
				{
					// Oben Mitte
					if(
					   isEntityOrEmpty(x, y-1) ||
					   Grid[x][y-1] == 20 ||
					   Grid[x][y-1] == 32 || Grid[x][y-1] == 33 ||
					   Grid[x][y-1] == 42 || Grid[x][y-1] == 43 ||
					   Grid[x][y-1] == 50 ||
					   Grid[x][y-1] == 60
							)
					{
						// Rechte Obere Ecke
						if(
						   isEntityOrEmpty(x+1, y-1) ||
						   Grid[x+1][y-1] == 11 ||
						   Grid[x+1][y-1] == 20 || Grid[x+1][y-1] == 21 ||
						   Grid[x+1][y-1] == 30 || Grid[x+1][y-1] == 32 || Grid[x+1][y-1] == 33 ||
						   Grid[x+1][y-1] == 40 || Grid[x+1][y-1] == 42 || Grid[x+1][y-1] == 43 ||
						   Grid[x+1][y-1] == 50 || Grid[x+1][y-1] == 51 ||
						   Grid[x+1][y-1] == 60 || Grid[x+1][y-1] == 61 ||
						   Grid[x+1][y-1] == 71 ||
						   Grid[x+1][y-1] == 81 ||
						   Grid[x+1][y-1] == 91
								)
						{
							// Mittle Links
							if(
							   isEntityOrEmpty(x-1, y) ||
							   Grid[x-1][y] == 23 ||
							   Grid[x-1][y] == 31 || Grid[x-1][y] == 32 ||
							   Grid[x-1][y] == 41 || Grid[x-1][y] == 42 ||
							   Grid[x-1][y] == 53 ||
							   Grid[x-1][y] == 63
								)
							{
								// Mitte Rechte
								if(
						   		   isEntityOrEmpty(x+1, y) ||
								   Grid[x+1][y] == 21 ||
								   Grid[x+1][y] == 30 || Grid[x+1][y] == 33 ||
								   Grid[x+1][y] == 40 || Grid[x+1][y] == 43 ||
								   Grid[x+1][y] == 51 ||
								   Grid[x+1][y] == 61
										)
								{
									// Untere Linke Ecke
									if(
									   isEntityOrEmpty(x-1, y+1) ||
									   Grid[x-1][y+1] == 13 ||
									   Grid[x-1][y+1] == 22 || Grid[x-1][y+1] == 23 ||
									   Grid[x-1][y+1] == 30 || Grid[x-1][y+1] == 31 || Grid[x-1][y+1] == 32 ||
									   Grid[x-1][y+1] == 40 || Grid[x-1][y+1] == 41 || Grid[x-1][y+1] == 42 ||
									   Grid[x-1][y+1] == 52 || Grid[x-1][y+1] == 53 ||
									   Grid[x-1][y+1] == 62 || Grid[x-1][y+1] == 63 ||
									   Grid[x-1][y+1] == 73 ||
									   Grid[x-1][y+1] == 83 ||
									   Grid[x-1][y+1] == 93
											)
									{
										// Unten Mittlere 
										if(
										   isEntityOrEmpty(x, y+1) ||
										   Grid[x][y+1] == 22 ||
										   Grid[x][y+1] == 30 || Grid[x][y+1] == 31 ||
										   Grid[x][y+1] == 40 || Grid[x][y+1] == 41 ||
										   Grid[x][y+1] == 52 ||
										   Grid[x][y+1] == 62
												)
										{
											// Unten Rechts
											if(
										       isEntityOrEmpty(x+1, y+1) ||
											   Grid[x+1][y+1] == 12 ||
											   Grid[x+1][y+1] == 21 || Grid[x+1][y+1] == 22 ||
											   Grid[x+1][y+1] == 30 || Grid[x+1][y+1] == 31 || Grid[x+1][y+1] == 33 ||
											   Grid[x+1][y+1] == 40 || Grid[x+1][y+1] == 41 || Grid[x+1][y+1] == 43 ||
											   Grid[x+1][y+1] == 51 || Grid[x+1][y+1] == 52 ||
											   Grid[x+1][y+1] == 61 || Grid[x+1][y+1] == 62 ||
											   Grid[x+1][y+1] == 72 ||
											   Grid[x+1][y+1] == 82 ||
											   Grid[x+1][y+1] == 92
													)
											{
												return true;
											}
											else return false;
											// Unten Rechts Ende
										}
										else return false;
									}
									else return false;
									// Untere Linke Ecke Ende
								}
								else return false;
									
								// Mittle Rechts Ende
							}
							else return false;
							// Mittle Links Ende
						}
						else return false;
						// Rechte Obere Ecke Ende
					}
					else return false;
					// Oben Mitte Ende
						
				}
				else return false;
				// Linke Obere Ecke Ende
			}
			// Check Border Cases
			// Zu Viel Arbeit; nicht lohnenswert; kann leider exploitet werden
			else return true;
		}
		
		return false;
	}
	
	public static boolean isEntityOrEmpty(int x, int y)
	{
		if(Grid[x][y] < 10 && Grid[x][y] != 03)
		{
			return true;
		}
		else if(Grid[x][y] == 15 || Grid[x][y] == 25 ||
				Grid[x][y] == 35 || Grid[x][y] == 45 ||
				Grid[x][y] == 55 || Grid[x][y] == 65 ||
				Grid[x][y] == 75) return true;
		else return false;
	}
	
	public boolean gridContains(int value)
	{
		for(int x = 0; x < Grid.length; x++)
		{
			for(int y = 0; y < Grid[0].length; y++)
			{
				if(Grid[x][y] == value) return true;
			}
		}
		return false;
	}
	
	public static void rescaleImages()
	{
		/*
		 * pre-render upscaling to save performance on render
		 */
		for(int i = 0; i < wall.length; i++)
		{
			for(int j = 0; j < wall[0].length; j++)
			{
				if(wall[i][j] != null)
				wallScaled[i][j] = wall[i][j].getScaledInstance(8*Game.S, 8*Game.S, 0);
			}
		}
		
		pacmanScaled = pacman.getScaledInstance(Game.S*15, Game.S*15, 0);
		
		for(int i = 0; i < ghost.length; i++)
		{
			ghostScaled[i] = ghost[i].getScaledInstance(Game.S*14, Game.S*14, 0);
		}
		for(int i = 0; i < item.length; i++)
		{
			itemScaled[i] = item[i].getScaledInstance(Game.S*14, Game.S*14, 0);
		}
		
		pacman_live_scaled = pacman_live.getScaledInstance(Game.S*14, Game.S*14, 0);

	}
	
	public static int[][] getGrid()
	{
		return Grid;
	}
	
	public static int getWall(int x, int y)
	{
		if(x < 0 || x > Grid.length-1 || y < 0 || y > Grid[0].length-1) return 0;
		else return Grid[x][y];
	}
	
	/*
	public static void save()
	{
		new MazeLoader().saveMaze(Grid, Menu.text);
	}
	
	public static void load()
	{
		Grid = new MazeLoader().loadMaze(Menu.text);
	}
	*/
	
	public void calculateEatables()
	{
		pacdots = 0;
		powerups = 0;
		for(int x = 0; x < Grid.length; x++)
		{
			for(int y = 0; y < Grid[0].length; y++)
			{
				if(Grid[x][y] == 01) pacdots++;
				else if(Grid[x][y] == 02) powerups++;
			}
		}
	}
	
	public static void undo()
	{
		if(!undo_stack.isEmpty())
		{
			redo_stack.push(Grid);
			//int[][] test = undo_stack.peek();
			//System.out.println(test[0][0]);
			Grid = undo_stack.pop();
		}
	}
	
	public static void redo()
	{
		if(!redo_stack.isEmpty())
		{
			undo_stack.push(Grid);
			Grid = redo_stack.pop();
		}
		
	}
	
	public static int countPacdots(int[][] Grid)
	{
		int pacdots = 0;
		for(int x = 0; x < Grid.length; x++)
		{
			for(int y = 0; y < Grid[0].length; y++)
			{
				if(Grid[x][y] == 01) pacdots++;
			}
		}
		
		return pacdots;
	}
	
	public static void save(String name)
	{
		new MazeLoader().saveMaze(Grid, name);
	}
	
	public static void save(int[][] grid, String name)
	{
		new MazeLoader().saveMaze(grid, name);
	}
	
	public static void load(String name)
	{
		undo_stack.push(Grid);
		Grid = ml.loadMaze(name);
		maxPacdots = countPacdots(Grid);

	}
	
	public static void loadSrc(String name)
	{
		undo_stack.push(Grid);
		Grid = ml.loadSrcMaze(name);
		maxPacdots = countPacdots(Grid);
	}
	
	public static void setType(int type)
	{
		MazeMaker.type = type;
	}
	
	public static int getType()
	{
		return type;
	}
	
	public static void setRotation(int rot)
	{
		rotation = rot;
	}
	
	public static int getRotation()
	{
		return rotation;
	}
	
	public static void switchCheckerboard()
	{
		if(checkerboard) checkerboard = false;
		else checkerboard = true;
	}
	
	public static void switchMirrorHorizontally()
	{
		if(mirrorhorizontally) mirrorhorizontally = false;
		else mirrorhorizontally = true;
	}
	
	public static void switchMirrorVertically()
	{
		if(mirrorvertically) mirrorvertically = false;
		else mirrorvertically = true;
	}
	
	public static boolean getCheckerboard()
	{
		return checkerboard;
	}
	
	public static boolean getMirrorHorizontally()
	{
		return mirrorhorizontally;
	}
	
	public static boolean getMirrorVertically()
	{
		return mirrorvertically;
	}
	
	public static boolean getPreview()
	{
		return preview;
	}
	
	public static void switchPreview()
	{
		if(preview) preview = false;
		else preview = true;
	}
	
	public static void switchShowWalkable()
	{
		if(showwalkable) showwalkable = false;
		else showwalkable = true;
	}
	
	public static boolean getShowWalkable()
	{
		return showwalkable;
	}
	
	public static Stack<int[][]> getRedoStack()
	{
		return redo_stack;
	}
	
	public static Stack<int[][]> getUndoStack()
	{
		return undo_stack;
	}
	
	public static void setShift(int sf)
	{
		shift = sf;
	}
	
	public int getPacdots()
	{
		return pacdots;
	}
	
	public int getPowerups()
	{
		return powerups;
	}

	public static Image getPacman_live_scaled() {
		return pacman_live_scaled;
	}
	
	public static Image getItemScaled() {
		return itemScaled[0];
	}
	
	public static Image getItemScaled(Integer index) {
		return itemScaled[index];
	}
	
	public void setMaxPacdots(int max)
	{
		maxPacdots = max;
	}
	
	public int getMaxPacdots()
	{
		return maxPacdots;
	}
}
