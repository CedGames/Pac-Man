package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import level.MazeLoader;
import level.MazeMaker;

// Runnable
// https://dbs.cs.uni-duesseldorf.de/lehre/docs/java/javabuch/html/k100143.html
public class Game extends Canvas implements Runnable
{
	
	private static final long serialVersionUID = 3203330472966843992L;
	
	//public static int WIDTH = 640, HEIGHT = WIDTH/12*9;
	public static int S = 2; // render-scale = Auflösung bzw. Größe des Fensters und seiner Inhalte auf dem Bildschirm
	public static int WIDTH = S*332, HEIGHT = S*248;
	public static int tWIDTH = WIDTH/S, tHEIGHT = HEIGHT/S;
	
	private Thread thread;
	private boolean running = false;
	
	public static boolean paused = false;
	private static int paused_timer = 0;
	
	private static Window w;
	private Handler handler;
	private HUD hud;
	private Menu menu;
	private MazeMaker mazemaker;
	private AudioPlayer audioplayer;
	
	public static boolean audio = true;
	private int maxRenderScale;
	
	private BufferStrategy bs;
	public Graphics g;
	
	private static int mx, my;
	
	public static boolean limitFPStoTicks = true;
	public static int FPS = 0; // FPS werden in run() überschrieben
	public static double TICKS = 60.0; // TICKS werden festgelegt, um einheitlichen Aktualisierungsintervall festzulegen
	
	public enum STATE
	{
		Menu,
		Game,
		Settings,
		End,
		MazeMaker
	};
	
	public static STATE gameState = STATE.Menu;
	
	public Game()
	{
		Language.setLanguage(Language.LANG.EN);
		AssetManager.loadSprites();
		//AssetManager.createFileStructure();
		mazemaker = new MazeMaker();
		MazeLoader.createFileStructure();
		handler = new Handler();
		audioplayer = new AudioPlayer();
		menu = new Menu(handler, this, audioplayer);
		hud = new HUD(this, menu, handler, mazemaker);
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(menu);
		this.addMouseWheelListener(menu);
		
		w = new Window(WIDTH, HEIGHT, Language.pacman, AssetManager.icon, this);
		setRenderScaleToMonitorSize(); // Render-Scale wird so hoch wie möglich geschraubt um einen Großteil des Bildschirms
									  // zu füllen
		
		//audioplayer.playBeginningTune();
		// Geister und Pacman werden instanziert, dies geschieht nur ein einziges mal bei Programmstart
		// sie werden nie gelöscht sondern nur nicht gerendert oder in einen anderen zustand versetzt
		
		handler.addObject(new Ghost(0, 0, ID.GHOST_RED, handler));
		handler.addObject(new Ghost(0, 0, ID.GHOST_ORANGE, handler));
		handler.addObject(new Ghost(0, 0, ID.GHOST_CYAN, handler));
		handler.addObject(new Ghost(0, 0, ID.GHOST_PINK, handler));
		handler.addObject(new Pacman(0, 0, handler, audioplayer));
	}
	
	// Synchronized Methods
	// https://docs.oracle.com/javase/tutorial/essential/concurrency/syncmeth.html
	
	public synchronized void start()
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop()
	{
		try
		{
			thread.join();
			running = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		this.requestFocus(); // Component (hier Canvas) erhält Input Fokus
		
		long lastTime = System.nanoTime();
		double amountOfTicks = TICKS;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1)
			{
				tick();
				updates++;
				
				delta--;

				if(limitFPStoTicks)
				{
					render();
					frames++;
				}
			}
			if(!limitFPStoTicks)
			{
				render();
				frames++;
			}
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				// JFrame Titel wird aktualisiert um Ticks pro Sekunde Und Bilder (Frames) pro Sekunde Anzuzeigen
				timer += 1000;
				FPS = frames;
				//System.out.println("FPS: " + frames + " TPS: " + updates);
				w.getFrame().setTitle("Pac-Man        TPS: " + updates + ", FPS: " + frames );
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void tick()
	{
		try
		{
			mx = getMousePosition().x;
			my = getMousePosition().y;
		}
		
		catch(NullPointerException e)
		{
			// mouse is outside of JFrame -> no action required
		}
		
		
		if(paused_timer > 0)
		{
			paused = true;
		}
		else paused = false;
		
		paused_timer--;
		
		
		if(gameState == STATE.Game)
		{
			if(!paused)
			{
				mazemaker.tick();
				handler.tick();
				hud.tick();
			}
		}
		else if(gameState == STATE.Menu)
		{
			menu.tick();
			handler.tick();
			if(handler.getPacman() != null)
			{
				if( (handler.getPacman().isDead()) && (handler.getPacman().getAnimDeath() == 12) ) 
				{
					handler.getPacman().setDead(false);
					handler.getPacman().setAnimDeath(0);
					handler.getPacman().setX(108*Game.S);
					handler.getPacman().setY(184*Game.S);
				}
			}
			
		}
		else if(gameState == STATE.MazeMaker)
		{
			menu.tick();
			mazemaker.tick();
		}
		
	}

	private void render()
	{
		bs = this.getBufferStrategy(); // https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics();
		
		
		g.setColor(Color.black);
		g.fillRect(0, 0, tWIDTH*S, tHEIGHT*S);
		
		if(gameState == STATE.Game)
		{
			mazemaker.render(g);
			handler.render(g);
			hud.render(g);
		}
		else if(gameState == STATE.Menu || gameState == STATE.Settings || gameState == STATE.End)
		{
			menu.render(g);
		}
		else if(gameState == STATE.MazeMaker)
		{
			mazemaker.render(g);	
			menu.render(g);
		}
		g.dispose();
		bs.show();
	}
	
	public static int clamp(int var, int min, int max)
	{
		// methode um eine variable des typs Integer zwischen zwei werten zu begrenzen bsp. clamp(Prozent, 0, 100)
		
		if(var >= max) return var = max;
		else if(var <= min) return var = min;
		else return var;
	}
	
	public void setRenderScale(int scale)
	{
		S = scale;
		w.rescale(S*tWIDTH, S*tHEIGHT);
		menu.refreshFontSize();
		MazeMaker.rescaleImages();
		try
		{
			handler.getPacman().rescale();
			
			Ghost ghost_red = (Ghost) handler.getObject(ID.GHOST_RED);
			ghost_red.rescaleImages();
			Ghost ghost_cyan = (Ghost) handler.getObject(ID.GHOST_CYAN);
			ghost_cyan.rescaleImages();
			Ghost ghost_orange = (Ghost) handler.getObject(ID.GHOST_ORANGE);
			ghost_orange.rescaleImages();
			Ghost ghost_pink = (Ghost) handler.getObject(ID.GHOST_PINK);
			ghost_pink.rescaleImages();
		}
		catch(Exception e)
		{
			System.out.println("Pacman and Ghosts not active; Unable to rescale");
		}
		//System.out.println("Scale: " + S);
		//System.out.println(S*tWIDTH + ", " + S*tHEIGHT);
		
	}

	public static void main(String[] args)
	{
		new Game();
	}

	public static int getMX()
	{
		return mx;
	}
	
	public static int getMY()
	{
		return my;
	}
	
	public void setRenderScaleToMonitorSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int scale = 1;
		while(tWIDTH*scale < (int) screenSize.getWidth() && tHEIGHT*scale < (int) screenSize.getHeight())
		{
			scale++;
		}
		maxRenderScale = scale-1;
		setRenderScale(scale-1);
		MazeMaker.rescaleImages();
		w.centerFrame();
		//System.out.println((int) screenSize.getWidth() + " × " + (int) screenSize.getHeight());
	}
	
	public int getMaxRenderScale()
	{
		return maxRenderScale;
	}
	
	public void changeCursor(String name)
	{
		w.changeCursor(name);
	}
	
	public static void pauseForTicks(int ticks)
	{
		// pausiert die gewisse aspekte in der tick() methode
		paused_timer = ticks;
	}
}
