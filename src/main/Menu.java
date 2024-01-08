package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import level.MazeLoader;
import level.MazeMaker;

public class Menu extends MouseAdapter
{
	private Handler handler;
	private Game game;
	private AudioPlayer audioplayer;
	private Font font;
    private Font font2;
    // private String fontPath = "src/assets/PixelMplus12-Regular.ttf";
    private String fontPath = AssetManager.font;
    private int fox = 3, foy = 13; // fontOffsetX, fontOffsetY (for monogram)
    // private int fox = 2, foy = 13; // fontOffsetX, fontOffsetY (for Comic Sans MS)
    private int scrollPos = 0;
    private boolean override = false;
    private Image pacLogo = new BufferedImageLoader().loadImage(AssetManager.logo).getScaledInstance((int)(80*Game.S*1.5), (int)(22*Game.S*1.5), Image.SCALE_SMOOTH);
	
    private Color c1 = Color.black;
    private Color c2 = Color.blue;
    
	public static String text = "";
	private String indicator = "";
	public static String warning = "";
	public Color warningColor = Color.red;
	//private String splashText = "test";
	
	private boolean[] btnClick;
	
	public Menu(Handler handler, Game game, AudioPlayer audioplayer)
	{
		this.handler = handler;
		this.game = game;
		this.audioplayer = audioplayer;
		
		btnClick = new boolean[11];
		btnClick[0] = true;
		btnClick[1] = true;
		btnClick[2] = true;
		btnClick[3] = true;
		btnClick[4] = true;
		btnClick[5] = true;
		btnClick[7] = true;
		btnClick[8] = true;
		btnClick[9] = true;
		btnClick[10] = true;
		
		try
		{
			font  = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)).deriveFont(30f*Game.S*0.95f);
			font2 = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)).deriveFont(15f*Game.S*0.95f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		if(Game.gameState == Game.STATE.Menu)
		{
			// PLAY BUTTON
			if(mouseOver(mx, my, Game.S*202/2, Game.S*122/2, Game.S*260/2, Game.S*60/2))
			{
				btnClick[0] = false;
				buttonPressed();
				Game.gameState = Game.STATE.Game;
				MazeMaker.loadSrc("level0");
				//MazeMaker.load("pass");
				Pacman pacman = handler.getPacman();
				handler.resetPositions();
				pacman.setHighscoreBeaten(false);
				pacman.setLives(3);
				pacman.setAnimDeath(0);

				Game.pauseForTicks((int)(4*Game.TICKS));
				audioplayer.playBeginningTune();
				handler.wakeGhosts();
				handler.getPacman().setAlive();
			}
		
			// Maze-Maker BUTTON
			if(mouseOver(mx, my, Game.S*202/2, Game.S*(122+75)/2, Game.S*(260)/2, Game.S*60/2))
			{
				btnClick[1] = false;
				buttonPressed();
				Game.gameState = Game.STATE.MazeMaker;
				
			}
		
			// Settings BUTTON
			if(mouseOver(mx, my, Game.S*202/2, Game.S*(122+150)/2, Game.S*(260)/2, Game.S*60/2))
			{
				btnClick[2] = false;
				buttonPressed();
				Game.gameState = Game.STATE.Settings;
				
			}
			
			// QUIT BUTTON
			if(mouseOver(mx, my, Game.S*202/2, Game.S*(122+225)/2, Game.S*(260)/2, Game.S*60/2))
			{
				btnClick[3] = false;
				buttonPressed();
				System.exit(0);
			}
			
			// Language BUTTON
			if(mouseOver(mx, my, Game.S*(202+270)/2, Game.S*(122+225)/2, Game.S*(60)/2, Game.S*60/2))
			{
				btnClick[4] = false;
				buttonPressed();
				
				if(Language.language == Language.LANG.DE)
				{
					Language.setLanguage(Language.LANG.EN);
				}
				else Language.setLanguage(Language.LANG.DE);

			}
			
			/*
			// Render-Scale BUTTON
			if(mouseOver(mx, my, Game.S*(202+270)/2, Game.S*(122+150)/2, Game.S*(60)/2, Game.S*60/2))
			{
				btnClick[5] = false;
				buttonPressed();
				if(Game.S < 4)
				{
					game.setRenderScale(Game.S+1);
				}
				else game.setRenderScale(1);
				refreshFontSize();
				MazeMaker.rescaleImages();
				handler.getPacman().rescaleImages();
				
				Ghost ghost_red = (Ghost) handler.getObject(ID.GHOST_RED);
				ghost_red.rescaleImages();
				Ghost ghost_cyan = (Ghost) handler.getObject(ID.GHOST_CYAN);
				ghost_cyan.rescaleImages();
				Ghost ghost_orange = (Ghost) handler.getObject(ID.GHOST_ORANGE);
				ghost_orange.rescaleImages();
				Ghost ghost_pink = (Ghost) handler.getObject(ID.GHOST_PINK);
				ghost_pink.rescaleImages();
			}
			*/
							
		}
		else if(Game.gameState == Game.STATE.Settings)
		{
			// Audio TickBox
			if(mouseOver(mx, my, Game.S*(120), Game.S*(63), Game.S*(16), Game.S*16))
			{
				btnClick[0] = false;
				buttonPressed();
				if(Game.audio)
				{
					Game.audio = false;
					audioplayer.stopSound();
				}
				else Game.audio = true;
							
			}
			
			// Back Button
			if(mouseOver(mx, my, Game.S*(2), Game.S*(221), Game.S*60, Game.S*24))
			{
				btnClick[1] = false;
				buttonPressed();
				Game.gameState = Game.STATE.Menu;
							
			}
			
			
			
			// Scales
			int spacing = 20;
			for(int i = 1; i <= game.getMaxRenderScale(); i++)
			{
				if(mouseOver(mx, my, Game.S*(80 + (spacing*i)), Game.S*(100), Game.S*(16), Game.S*16))
				{
					buttonPressed();
					game.setRenderScale(i);
				
				}
			}
			
			// Scales End
			
			// Colors
			
			
			// Yellow
			if(mouseOver(mx, my, Game.S*(100), Game.S*(133), Game.S*(16), Game.S*16))
			{
				buttonPressed();
				c2 = new Color(252, 202, 47);
			}
			
			// Light-Blue
			if(mouseOver(mx, my, Game.S*(120), Game.S*(133), Game.S*(16), Game.S*16))
			{
				buttonPressed();
				c2 = new Color(66, 143, 248);
			}
			
			// Red
			if(mouseOver(mx, my, Game.S*(140), Game.S*(133), Game.S*(16), Game.S*16))
			{
				buttonPressed();
				c2 = new Color(227, 27, 31);
			}
			
			// Blue
			if(mouseOver(mx, my, Game.S*(160), Game.S*(133), Game.S*(16), Game.S*16))
			{
				buttonPressed();
				c2 = Color.blue;
			}
			
			// Colors End
			
			// limitFPStoTicks Tickbox
			if(mouseOver(mx, my, Game.S*(100), Game.S*(153), Game.S*(16), Game.S*16))
			{
				btnClick[2] = false;
				buttonPressed();
				if(Game.limitFPStoTicks)
				{
					Game.limitFPStoTicks = false;
				}
				else Game.limitFPStoTicks = true;					
			}
			
		}
		else if(Game.gameState == Game.STATE.MazeMaker)
		{
			if(MazeMaker.state == MazeMaker.STATE.Base)
			{
				// Load BUTTON (MazeMaker)
				if(mouseOver(mx, my, Game.S*(270), Game.S*(3), Game.S*(120)/2, Game.S*60/2))
				{
					btnClick[0] = false;
					buttonPressed();
					//MazeMaker.load();
					scrollPos = 0;
					MazeMaker.state = MazeMaker.STATE.Load;
								
				}
				
				// Save BUTTON (MazeMaker)
				if(mouseOver(mx, my, Game.S*(270), Game.S*(216), Game.S*(120)/2, Game.S*60/2))
				{
					btnClick[1] = false;
					buttonPressed();
					//MazeMaker.save();	
					if(text.startsWith("game.")) text = text.substring(5);
					MazeMaker.state = MazeMaker.STATE.Save;
											
				}
				
				// Clear BUTTON (MazeMaker)
				if(mouseOver(mx, my, Game.S*(226), Game.S*(3), Game.S*42, Game.S*16))
				{
					btnClick[6] = false;
					buttonPressed();
					MazeMaker.loadSrc("empty");
					text = "";
											
				}
				
				// Back BUTTON (MazeMaker)
				if(mouseOver(mx, my, Game.S*(226), Game.S*(230), Game.S*42, Game.S*16))
				{
					btnClick[9] = false;
					buttonPressed();
					Game.gameState = Game.STATE.Menu;
											
				}
				
				// Mirror horizontally TickBox
				if(mouseOver(mx, my, Game.S*(226), Game.S*(40), Game.S*(16), Game.S*16))
				{
					btnClick[2] = false;
					buttonPressed();
					MazeMaker.switchMirrorHorizontally();
				}
				
				// Mirror vertically TickBox
				
				if(mouseOver(mx, my, Game.S*(226), Game.S*(58), Game.S*(16), Game.S*16))
				{
					btnClick[3] = false;
					buttonPressed();
					MazeMaker.switchMirrorVertically();			
											
				}
				
				// Checkerboard TickBox
				if(mouseOver(mx, my, Game.S*(226), Game.S*(76), Game.S*(16), Game.S*16))
				{
					btnClick[4] = false;
					buttonPressed();
					MazeMaker.switchCheckerboard();	
							
				}
				
				// Preview TickBox
				if(mouseOver(mx, my, Game.S*(226), Game.S*(94), Game.S*(16), Game.S*16))
				{
					btnClick[5] = false;
					buttonPressed();
					MazeMaker.switchPreview();	
											
				}
				
				// Show-Walkable TickBox
				if(mouseOver(mx, my, Game.S*(226), Game.S*(112), Game.S*(16), Game.S*16))
				{
					btnClick[10] = false;
					buttonPressed();
					MazeMaker.switchShowWalkable();	
										
				}
				
				// Maze Area
				if(mouseOver(mx, my, 0, 0, 28*8*Game.S, 31*8*Game.S))
				{
					//System.out.println("Button: " + e.getButton());
					// Links-Click
					if(e.getButton() == 1)
					{
						//System.out.println(MazeMaker.getType() + MazeMaker.getRotation());
						MazeMaker.inGrid(MazeMaker.getType() + MazeMaker.getRotation(), mx/8/Game.S, my/8/Game.S);
						//mazemaker.inGrid(21, mx/8/Game.S, my/8/Game.S);
						//System.out.println("w: " + mazemaker.getSelected() + ", x:" + mx + ", y: " + my);
					}
					// Mausrad-Click
					else if(e.getButton() == 2)
					{
						// MazeMaker.getGridWall(mx, my);
						MazeMaker.setType(MazeMaker.getGridWall(mx, my)-(MazeMaker.getGridWall(mx, my) % 10));
						MazeMaker.setRotation(MazeMaker.getGridWall(mx, my) % 10);
					}
					// Rechts-Click
					else if(e.getButton() == 3)
					{
						// Rechts-Click -> lösche/leere aktuelle Zelle
						MazeMaker.inGrid(00, mx/8/Game.S, my/8/Game.S);
						//System.out.println("w: 0, x:" + mx + ", y: " + my);
					}
					
				}
				
				// Select entity Area
				if(mouseOver(mx, my, 227*Game.S, 160*Game.S, 16*Game.S, 16*Game.S))
				{
					MazeMaker.setType(00);
					MazeMaker.setRotation(4);
				}
				
				// Undo Button
				if(mouseOver(mx, my, Game.S*(226), Game.S*(20), Game.S*(20), Game.S*16) && !MazeMaker.getUndoStack().isEmpty())
				{
					btnClick[7] = false;
					buttonPressed();
					MazeMaker.undo();
											
				}
				
				// Redo Button
				if(mouseOver(mx, my, Game.S*(248), Game.S*(20), Game.S*(20), Game.S*16) && !MazeMaker.getRedoStack().isEmpty())
				{
					btnClick[8] = false;
					buttonPressed();
					MazeMaker.redo();	
											
				}
			}
			else if(MazeMaker.state == MazeMaker.STATE.Load)
			{
				// Load-Leave TickBox
				if(mouseOver(mx, my, Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16))
				{
					buttonPressed();
					MazeMaker.state = MazeMaker.STATE.Base;
											
				}
				
				// .level List		
				for(int i = 0; i < getMaxListLength(); i++)
				{
					if(mouseOver(mx, my, (18+16)*Game.S, (i*18)*Game.S + 34*Game.S, 160*Game.S, 16*Game.S))
					{
						MazeMaker.load(MazeLoader.fileList(scrollPos+i).substring(0, MazeLoader.fileList(scrollPos+i).length()-6));
						//System.out.println(MazeLoader.fileList(scrollPos+i));
						buttonPressed();
						MazeMaker.state = MazeMaker.STATE.Base;
						text = MazeLoader.fileList(scrollPos+i).substring(0, MazeLoader.fileList(scrollPos+i).length()-6);
					}
					
					
				}
			}
			else if(MazeMaker.state == MazeMaker.STATE.Save)
			{
				// Save-Leave TickBox
				if(mouseOver(mx, my, Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16))
				{
					buttonPressed();
					MazeMaker.state = MazeMaker.STATE.Base;
					warning = "";
					
											
				}
				
				// Save Button (Save-Menu)
				if(mouseOver(mx, my, Game.S*(154), Game.S*(70), Game.S*(40), Game.S*16))
				{
					btnClick[7] = false;
					buttonPressed(); // here
					if(!MazeLoader.levelExists(text) || override)
					{
						MazeMaker.save(text);
						warningColor = Color.green;
						warning = Language.savesucces;
					}	
					else
					{
						warningColor = Color.red;
						warning = Language.fileexists;
					}
												
				}
				
				// Override Button (Save-Menu)
				if(mouseOver(mx, my, Game.S*(18+16), Game.S*(70), Game.S*(16), Game.S*16))
				{
					btnClick[8] = false;
					buttonPressed(); // here
					if(override) override = false;
					else override = true;
				}
			}
		}
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		btnClick[0] = true;
		btnClick[1] = true;
		btnClick[2] = true;
		btnClick[3] = true;
		btnClick[4] = true;
		btnClick[5] = true;
		btnClick[6] = true;
		btnClick[7] = true;
		btnClick[8] = true;
		btnClick[9] = true;
		btnClick[10] = true;
		
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int index = 0;
		if(e.getWheelRotation() > 0) index = getScrollPos() + 1;
		else if(e.getWheelRotation() < 0) index = getScrollPos() - 1;
		setScrollPos(Game.clamp(index, 0, MazeLoader.fileList().length - getMaxListLength()));
	}
	
	private boolean mouseOver(int mx, int my, int x, int y, int width, int height)
	{
		if(mx > x && mx < x + width)
		{
			if(my > y && my < y + height)
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	int c = 0;
	public void tick()
	{
		// if(text.endsWith("|")) tempText = text.substring(0, text.length()-1);

		if(c > 20)
		{
			if(indicator.equals("|")) indicator = "";
			else indicator = "|";
			c = 0;
		}
		c++;
		if(Game.gameState == Game.STATE.Menu)
		{
			// PLAY BUTTON
			if(mouseOver(Game.getMX(), Game.getMY(), Game.S*202/2, Game.S*122/2, Game.S*260/2, Game.S*60/2))
			{
				game.changeCursor("select");
			}
			else game.changeCursor("tri");
		}
	}
	
	public void render(Graphics g)
	{		
		
		//Graphics2D g2d = (Graphics2D) g;
	   //   g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    //                    RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		g.setColor(c2);
		
		if(Game.gameState == Game.STATE.Menu)
		{
			//drawCenteredString(Language.pacman, Game.S*40, g);
			
			g.drawImage(pacLogo,Game.S*106, Game.S*12, null);
			//g.setColor(new Color(252, 202, 47));
			//g.drawString(splashText, Game.S*200, Game.S*50);
			
			// PLAY BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*202/2, Game.S*122/2, Game.S*260/2, Game.S*60/2, btnClick[0]);
			g.fill3DRect(Game.S*202/2, Game.S*122/2, Game.S*260/2, Game.S*60/2, btnClick[0]);
			g.setColor(c1);
			drawCenteredString(Language.play, Game.S*(122+42)/2, g);
			
			// Maze-Maker BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*202/2, Game.S*(122+75)/2, Game.S*(260)/2, Game.S*60/2, btnClick[1]);
			g.fill3DRect(Game.S*202/2, Game.S*(122+75)/2, Game.S*(260)/2, Game.S*60/2, btnClick[1]);
			g.setColor(c1);
			drawCenteredString(Language.mazemaker, Game.S*(122+75+42)/2, g);
			
			// Settings BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*202/2, Game.S*(122+150)/2, Game.S*(260)/2, Game.S*60/2, btnClick[2]);
			g.fill3DRect(Game.S*202/2, Game.S*(122+150)/2, Game.S*(260)/2, Game.S*60/2, btnClick[2]);
			g.setColor(c1);
			drawCenteredString(Language.settings, Game.S*(122+150+42)/2, g);
			
			// QUIT BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*202/2, Game.S*(122+225)/2, Game.S*(260)/2, Game.S*60/2, btnClick[3]);
			g.fill3DRect(Game.S*202/2, Game.S*(122+225)/2, Game.S*(260)/2, Game.S*60/2, btnClick[3]);
			g.setColor(c1);
			drawCenteredString(Language.quit, Game.S*(122+225+42)/2, g);
			
			// Language BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*(202+270)/2, Game.S*(122+225)/2, Game.S*(60)/2, Game.S*60/2, btnClick[4]);
			g.fill3DRect(Game.S*(202+270)/2, Game.S*(122+225)/2, Game.S*(60)/2, Game.S*60/2, btnClick[4]);
			g.setColor(c1);
			g.drawString(Language.lan, Game.S*240, Game.S*(122+225+42)/2);
			
			/*
			// Render-Scale BUTTON
			g.setColor(c2);
			g.draw3DRect(Game.S*(202+270)/2, Game.S*(122+150)/2, Game.S*(60)/2, Game.S*60/2, btnClick[5]);
			g.fill3DRect(Game.S*(202+270)/2, Game.S*(122+150)/2, Game.S*(60)/2, Game.S*60/2, btnClick[5]);
			g.setColor(c1);
			g.drawString(Integer.toString(Game.S), Game.S*246, Game.S*(122+150+42)/2);
			*/
			
			g.setColor(Color.white);
			//g.setFont(new Font("Consolas", Font.PLAIN, 10*Game.S));
			g.setFont(font2);
			drawCenteredString(Language.copyright1, Game.S*228, g);
			drawCenteredString(Language.copyright2, Game.S*238, g);
		}
		else if(Game.gameState == Game.STATE.Settings)
		{
			drawCenteredString(Language.settings, Game.S*40, g);
			
			/*
			g.draw3DRect(Game.WIDTH-Game.WIDTH+15, Game.HEIGHT-75, Game.WIDTH/3, Game.HEIGHT/8, btnClick[3]);
			g.drawString(Language.back, Game.WIDTH-Game.WIDTH+15+56, Game.HEIGHT-35);
			
			g.setFont(font2);
			g.drawString("WASD -> bewegen", Game.WIDTH/2-Game.WIDTH/6-40, Game.HEIGHT/4+40);
			g.drawString("lass dich nicht treffen!", Game.WIDTH/2-Game.WIDTH/6-40, Game.HEIGHT/4+60);
			*/
			
			// Audio TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(120), Game.S*(63), Game.S*(16), Game.S*16, btnClick[0]);
			g.fill3DRect(Game.S*(120), Game.S*(63), Game.S*(16), Game.S*16, btnClick[0]);
			drawCenteredString(Language.audio, Game.S*(64+foy), g);
			g.setColor(c1);
			if(Game.audio) g.drawString("x", Game.S*(120+fox), Game.S*(63+foy));
			
			// Back Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(2), Game.S*(221), Game.S*60, Game.S*24, btnClick[1]);
			g.fill3DRect(Game.S*(2), Game.S*(221), Game.S*60, Game.S*24, btnClick[1]);
			g.setColor(c1);
			g.drawString(Language.back, Game.S*(2+fox), Game.S*(225+foy));
			
			// Renderscale
			g.setFont(font2);
			g.setColor(c2);
			g.drawString(Language.renderscale+":", Game.S*(100), Game.S*(83+foy));
			
			// Render Scales
			int spaceing = 20;
			for(int i = 1; i <= game.getMaxRenderScale(); i++)
			{
				g.setColor(c2);
				g.draw3DRect(Game.S*(80 + (spaceing*i)), Game.S*(100), Game.S*(16), Game.S*16, true);
				if(Game.S == i)
				{
					g.fill3DRect(Game.S*(80 + (spaceing*i)), Game.S*(100), Game.S*(16), Game.S*16, true);
					g.setColor(c1);
				}
				else g.setColor(c2);
				g.setFont(font);
				g.drawString(String.valueOf(i) , Game.S*(80 + (spaceing*i) + 1+fox), Game.S*(101+foy));
			}
			
			// Render Scales End
			
			// Color Selection
			g.setFont(font2);
			g.setColor(c2);
			g.drawString(Language.colorselection+":", Game.S*(100), Game.S*(117+foy));
			
			// Yellow
			g.setColor(new Color(252, 202, 47));
			g.draw3DRect(Game.S*(100), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.fill3DRect(Game.S*(100), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.setFont(font);
			g.setColor(c1);
			if(c2.equals(new Color(252, 202, 47))) g.drawString("x", Game.S*(100+fox), Game.S*(133+foy));
			
			// Light-Blue
			g.setColor(new Color(66, 143, 248));
			g.draw3DRect(Game.S*(120), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.fill3DRect(Game.S*(120), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.setFont(font);
			g.setColor(c1);
			if(c2.equals(new Color(66, 143, 248))) g.drawString("x", Game.S*(120+fox), Game.S*(133+foy));
			
			// Red
			g.setColor(new Color(227, 27, 31));
			g.draw3DRect(Game.S*(140), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.fill3DRect(Game.S*(140), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.setFont(font);
			g.setColor(c1);
			if(c2.equals(new Color(227, 27, 31))) g.drawString("x", Game.S*(140+fox), Game.S*(133+foy));
			
			// Blue
			g.setColor(Color.blue);
			g.draw3DRect(Game.S*(160), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.fill3DRect(Game.S*(160), Game.S*(133), Game.S*(16), Game.S*16, true);
			g.setFont(font);
			g.setColor(c1);
			if(c2.equals(Color.blue)) g.drawString("x", Game.S*(160+fox), Game.S*(133+foy));
			
			
			// limitFPStoTicks Tickbox
			g.setColor(c2);
			g.draw3DRect(Game.S*(100), Game.S*(153), Game.S*(16), Game.S*16, btnClick[2]);
			g.fill3DRect(Game.S*(100), Game.S*(153), Game.S*(16), Game.S*16, btnClick[2]);
			drawCenteredString(Language.limitFPStoTicks, Game.S*(154+foy), g);
			g.setColor(c1);
			if(Game.limitFPStoTicks) g.drawString("x", Game.S*(100+fox), Game.S*(153+foy));

		}
		else if(Game.gameState == Game.STATE.MazeMaker)
		{
			// Load Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(270), Game.S*(3), Game.S*(60), Game.S*30, btnClick[0]);
			g.fill3DRect(Game.S*(270), Game.S*(3), Game.S*(60), Game.S*30, btnClick[0]);
			g.setColor(c1);
			g.drawString(Language.load, Game.S*270+fox, Game.S*(10+foy));
					
			// Save Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(270), Game.S*(216), Game.S*(60), Game.S*30, btnClick[1]);
			g.fill3DRect(Game.S*(270), Game.S*(216), Game.S*(60), Game.S*30, btnClick[1]);
			g.setColor(c1);
			g.drawString(Language.save, Game.S*(270+fox), Game.S*(224+foy));
			
			g.setFont(font2);
			
			// Clear Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(3), Game.S*42, Game.S*16, btnClick[6]);
			g.fill3DRect(Game.S*(226), Game.S*(3), Game.S*42, Game.S*16, btnClick[6]);
			g.setColor(c1);
			g.drawString(Language.clear, Game.S*(226+fox), Game.S*(2+foy));
			
			// Back Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(230), Game.S*42, Game.S*16, btnClick[9]);
			g.fill3DRect(Game.S*(226), Game.S*(230), Game.S*42, Game.S*16, btnClick[9]);
			g.setColor(c1);
			g.drawString(Language.back, Game.S*(226+fox), Game.S*(230+foy));
			
			
			// Mirror horizontally TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(40), Game.S*(16), Game.S*16, btnClick[2]);
			g.fill3DRect(Game.S*(226), Game.S*(40), Game.S*(16), Game.S*16, btnClick[2]);
			g.drawString(Language.mirrorhorizontally, Game.S*(242+fox), Game.S*(38+foy));
			
			
			// Mirror vertically TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(58), Game.S*(16), Game.S*16, btnClick[3]);
			g.fill3DRect(Game.S*(226), Game.S*(58), Game.S*(16), Game.S*16, btnClick[3]);
			g.drawString(Language.mirrorvertically, Game.S*244, Game.S*(56+foy));
			
			// Checkerboard TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(76), Game.S*(16), Game.S*16, btnClick[4]);
			g.fill3DRect(Game.S*(226), Game.S*(76), Game.S*(16), Game.S*16, btnClick[4]);
			g.drawString(Language.checkerboard, Game.S*244, Game.S*(74+foy));
			
			// Preview TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(94), Game.S*(16), Game.S*16, btnClick[5]);
			g.fill3DRect(Game.S*(226), Game.S*(94), Game.S*(16), Game.S*16, btnClick[5]);
			g.drawString(Language.preview, Game.S*244, Game.S*(94+foy));
			
			// Show-Walkable TickBox
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(112), Game.S*(16), Game.S*16, btnClick[10]);
			g.fill3DRect(Game.S*(226), Game.S*(112), Game.S*(16), Game.S*16, btnClick[10]);
			g.drawString(Language.showwalkable, Game.S*244, Game.S*(112+foy));

			g.drawLine(Game.S*8*28, 0, Game.S*8*28, Game.S*8*31);
			
			g.setFont(font);
			g.setColor(c1);
			if(MazeMaker.getMirrorHorizontally()) g.drawString("x", Game.S*(226+fox), Game.S*(40+foy));
			if(MazeMaker.getMirrorVertically()) g.drawString("x", Game.S*(226+fox), Game.S*(58+foy));
			if(MazeMaker.getCheckerboard()) g.drawString("x", Game.S*(226+fox), Game.S*(76+foy));
			if(MazeMaker.getPreview()) g.drawString("x", Game.S*(226+fox), Game.S*(94+foy));
			if(MazeMaker.getShowWalkable()) g.drawString("x", Game.S*(226+fox), Game.S*(112+foy));
			
			// Undo Button
			
			g.setColor(c2);
			g.draw3DRect(Game.S*(226), Game.S*(20), Game.S*(20), Game.S*16, btnClick[7]);
			if(!MazeMaker.getUndoStack().isEmpty()) g.fill3DRect(Game.S*(226), Game.S*(20), Game.S*(20), Game.S*16, btnClick[7]);
			g.setColor(c1);
			g.drawString("<", Game.S*230, Game.S*(21+foy));
									
			// Redo Button
			g.setColor(c2);
			g.draw3DRect(Game.S*(248), Game.S*(20), Game.S*(20), Game.S*16, btnClick[8]);
			if(!MazeMaker.getRedoStack().isEmpty()) g.fill3DRect(Game.S*(248), Game.S*(20), Game.S*(20), Game.S*16, btnClick[8]);
			g.setColor(c1);
			g.drawString(">", Game.S*254, Game.S*(21+foy));
			
			
			// Info Text
			g.setFont(font2);
			g.setColor(Color.white);
			g.drawString(Language.mminfo1, 226*Game.S, 137*Game.S);
			g.drawString(Language.mminfo2, 226*Game.S, 147*Game.S);
			g.drawString(Language.mminfo3, 226*Game.S, 157*Game.S);

			if(MazeMaker.state != MazeMaker.STATE.Base)
			{
				// darkens background
				g.setColor(new Color(0, 0, 0, 125));
				g.fillRect(0, 0, Game.tWIDTH*Game.S, Game.tHEIGHT*Game.S);
			}
			
			if(MazeMaker.state == MazeMaker.STATE.Load)
			{
				g.setColor(new Color(0, 0, 0, 175));
				g.fillRect(16*Game.S, 16*Game.S, 192*Game.S, 216*Game.S);
				g.setColor(Color.WHITE);
				g.drawRect(16*Game.S, 16*Game.S, 192*Game.S, 216*Game.S);
				
				g.setColor(Color.RED);
				g.fillRect(Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16);
				g.setColor(Color.WHITE);
				g.drawRect(Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16);
				g.drawString("x", Game.S*(192+fox), Game.S*(16+foy));
				g.setFont(font2);
				if(MazeLoader.getFilePath().length() > 31) g.drawString(MazeLoader.getFilePath().substring(0, 28) + "...", Game.S*(16+fox), Game.S*(16+foy));
				else g.drawString(MazeLoader.getFilePath(), Game.S*(16+2), Game.S*(16+foy));
				g.drawRect(Game.S*(16), Game.S*(16), Game.S*(192), Game.S*16);
				
				// .level List
				for(int i = 0; i < getMaxListLength(); i++)
				{
					g.setColor(c2);
					g.fillRect((18+16)*Game.S, (i*18)*Game.S + Game.S*34, 160*Game.S, 16*Game.S);
					g.setColor(Color.WHITE);
					g.drawRect((18+16)*Game.S, (i*18)*Game.S + 34*Game.S, 160*Game.S, 16*Game.S);
					try
					{
						g.drawString(MazeLoader.fileList(scrollPos+i).substring(0, MazeLoader.fileList(scrollPos+i).length()-6) /* removes '.level' extention*/, (18+16+fox)*Game.S, (i*18)*Game.S + (35+foy)*Game.S);
						//drawCenteredString(MazeLoader.fileList(scrollPos+i).substring(0, MazeLoader.fileList(scrollPos+i).length()-6), (18+16+fox)*Game.S, (160+fox)*Game.S, (i*18)*Game.S + (35+foy)*Game.S, g);
					}
					catch(Exception e)
					{
						g.drawString(MazeLoader.fileList(scrollPos+i), (18+16+fox)*Game.S, (i*18)*Game.S + (35+foy)*Game.S);
					}
					
				}
			}
			else if(MazeMaker.state == MazeMaker.STATE.Save)
			{
				g.setFont(font);
				g.setColor(new Color(0, 0, 0, 175));
				g.fillRect(16*Game.S, 16*Game.S, 192*Game.S, 216*Game.S);
				g.setColor(Color.WHITE);
				g.drawRect(16*Game.S, 16*Game.S, 192*Game.S, 216*Game.S);
				
				g.setColor(Color.RED);
				g.fillRect(Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16);
				g.setColor(Color.WHITE);
				g.drawRect(Game.S*(192), Game.S*(16), Game.S*(16), Game.S*16);
				g.drawString("x", Game.S*(192+fox), Game.S*(16+foy));
				g.setFont(font2);
				if(MazeLoader.getFilePath().length() > 30) g.drawString(MazeLoader.getFilePath().substring(0, 31) + "...", Game.S*(16+fox), Game.S*(16+foy));
				else g.drawString(MazeLoader.getFilePath(), Game.S*(16+2), Game.S*(16+foy));
				g.drawRect(Game.S*(16), Game.S*(16), Game.S*(192), Game.S*16);
				
				g.setColor(c2);
				g.drawString("Name:", Game.S*(18+16+fox),  Game.S*(36+foy));
				g.drawRect(Game.S*(18+16), Game.S*(52), Game.S*(160), Game.S*16);
				
				g.setColor(Color.white);
				g.drawString(text, Game.S*(18+16+fox), Game.S*(52+foy));
				g.drawString(text + indicator, Game.S*(18+16+fox), Game.S*(52+foy));
				
				// Save Button
				g.setColor(c2);
				g.draw3DRect(Game.S*(154), Game.S*(70), Game.S*(40), Game.S*16, btnClick[7]);
				g.fill3DRect(Game.S*(154), Game.S*(70), Game.S*(40), Game.S*16, btnClick[7]);
				g.setColor(c1);
				g.drawString(Language.save, Game.S*156, Game.S*(68+foy));
				
				// Override TickBox
				g.setColor(c2);
				g.draw3DRect(Game.S*(18+16), Game.S*(70), Game.S*(16), Game.S*16, btnClick[8]);
				g.fill3DRect(Game.S*(18+16), Game.S*(70), Game.S*(16), Game.S*16, btnClick[8]);
				g.drawString(Language.override, Game.S*54, Game.S*(68+foy));
				g.setFont(font);
				g.setColor(c1);
				if(override) g.drawString("x", Game.S*(18+16+fox), Game.S*(70+foy));
				
				// warning text
				g.setFont(font2);
				g.setColor(warningColor);
				g.drawString(warning, Game.S*(18+16), Game.S*(84+foy));
			}
		}
		
		
		
	}
	
	private void drawCenteredString(String str, int y, Graphics g)
	{
		// http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
		
		FontMetrics fm = g.getFontMetrics();
	    int x = (Game.S*Game.tWIDTH - fm.stringWidth(str)) / 2;
		g.drawString(str, x, y);
	}
	/*
	private void drawCenteredString(String str, int minX, int maxX, int y, Graphics g)
	{
		// http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
		
		FontMetrics fm = g.getFontMetrics();
	    int x = ((maxX+minX)/3 - fm.stringWidth(str));
		g.drawString(str, x, y);
	}*/
	
	private void buttonPressed()
	{
		audioplayer.stopSound();
		//new AudioPlayer().playSFX(AssetManager.button_click);
		audioplayer.playButtonClick();
		
		try
		{
			Thread.sleep(150);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		game.changeCursor("tri");
	}
	
	public void setScrollPos(int pos)
	{
		scrollPos = pos;
	}
	
	public int getScrollPos()
	{
		return scrollPos;
	}
	
	public void refreshFontSize()
	{
		pacLogo = new BufferedImageLoader().loadImage(AssetManager.logo).getScaledInstance((int)(80*Game.S*1.5), (int)(22*Game.S*1.5), Image.SCALE_SMOOTH);
		//font  = new Font(fontPath, Font.PLAIN, 40*Game.S/2);
		//font2 = new Font(fontPath, Font.PLAIN, 18*Game.S/2);
		
		try
		{
			font  = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)).deriveFont(30f*Game.S*0.95f);
			font2 = Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)).deriveFont(15f*Game.S*0.95f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream(fontPath)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public int getMaxListLength()
	{
		int maxLength = 11;
		if((MazeLoader.fileList().length < maxLength)) maxLength = MazeLoader.fileList().length;
		
		return maxLength;
	}
	
	public Color getC1()
	{
		return c1;
	}
	
	public Color getC2()
	{
		return c2;
	}
	
	public Font getFont()
	{
		return font;
	}
	
	public Font getFont2()
	{
		return font2;
	}
	
}
