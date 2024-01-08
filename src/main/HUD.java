package main;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;

import level.MazeMaker;

public class HUD
{
	private ArrayList<Integer> fruitList;
	
	private boolean drawFPS;
	
	private int level = 1;
	
	private Game game;
	private Menu menu;
	private Handler handler;
	private MazeMaker mazemaker;
	private STATE ingameState;
	
	public enum STATE
	{
		STARTING,
		PLAYING,
		GAMEOVER
	}
	
	public HUD(Game game, Menu menu, Handler handler, MazeMaker mazemaker)
	{
		this.game = game;
		this.menu = menu;
		this.handler = handler;
		this.mazemaker = mazemaker;
		ingameState = STATE.STARTING;
		
		fruitList = new ArrayList<Integer>();
		drawFPS = false;
	}
	
	public void tick()
	{
		if(Game.paused && handler.getPacman().getLives() <= 0) ingameState = STATE.GAMEOVER;
		else if(!handler.getPacman().isDead()) ingameState = STATE.PLAYING;
		else if(Game.paused && !handler.getPacman().isDead()) ingameState = STATE.STARTING;
		
		drawFPS = false;
		
		if(game.gameState == Game.STATE.Game)
		{
			if(mazemaker.getPacdots() == 0 && mazemaker.getPowerups() == 0)
			{
				MazeMaker.loadSrc("level0");
				//MazeMaker.load("pass");
				Pacman pacman = handler.getPacman();
				handler.resetPositions();
				pacman.setHighscoreBeaten(false);
				pacman.setLives(3);
				pacman.setAnimDeath(0);

				Game.pauseForTicks((int)(4*Game.TICKS));
				
				handler.wakeGhosts();
				handler.getPacman().setAlive();
			}
		}
		
	}
	
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(28*8*Game.S, 0, 28*8*Game.S, 31*8*Game.S);
		g.setFont(menu.getFont2());
		//g.setColor(menu.getC2());
		//g.fillRect(10,10, 200, 32);
		
		g.setColor(Color.white);
		g.drawString("HIGH SCORE", (28*8+5)*Game.S, 10*Game.S);
		g.drawString(String.valueOf(handler.getPacman().getHighScore()), (28*8+15)*Game.S, 20*Game.S);
		
		g.drawString("SCORE", (28*8+5)*Game.S, 35*Game.S);
		g.drawString(String.valueOf(handler.getPacman().getScore()), (28*8+15)*Game.S, 45*Game.S);
		
		for(int i = 0; i < handler.getPacman().getLives(); i++)
		{
			g.drawImage(MazeMaker.getPacman_live_scaled(), (28*8+5)*Game.S+(i*12*Game.S), 55*Game.S, null);
		}
		
		for(int i = 0; i < fruitList.size(); i++)
		{
			g.drawImage(MazeMaker.getItemScaled(fruitList.get(i)),(28*8+5)*Game.S+(i*12*Game.S), 70*Game.S, null);
		}
		
		if(ingameState == STATE.STARTING)
		{
			//g.setColor(new Color(0, 0, 0, 125));
			//g.fillRect(68*Game.S, 132*Game.S, 88*Game.S, 16*Game.S);
			g.setColor(Color.yellow);
			drawCenteredString(Language.ready, 143*Game.S, g);
		}
		else if(ingameState == STATE.GAMEOVER)
		{
			g.setColor(Color.red);
			drawCenteredString(Language.gameover, 143*Game.S, g);
		}
		
		
		if(drawFPS)
		{
			g.setColor(Color.green);
			g.drawString("FPS: " + Game.FPS, (28*8+5)*Game.S, 10*Game.S);
			g.drawString("Score: " + handler.getPacman().getScore(), (28*8+5)*Game.S, 20*Game.S);
			g.drawString("Level: " + level, (28*8+5)*Game.S, 30*Game.S);
			g.drawString("VelX: " + handler.getPacman().getVelX(), (28*8+5)*Game.S, 40*Game.S);
			g.drawString("VelY: " + handler.getPacman().getVelY(), (28*8+5)*Game.S, 50*Game.S);
			g.drawString("PUPS: " + mazemaker.getPowerups(), (28*8+5)*Game.S, 60*Game.S);
			g.drawString("DOTS: " + mazemaker.getPacdots(), (28*8+5)*Game.S, 70*Game.S);
			g.drawString("Paused: " + Game.paused, (28*8+5)*Game.S, 80*Game.S);
		}
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public void nextLevel()
	{
		level++;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	private void drawCenteredString(String str, int y, Graphics g)
	{
		// http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
		
		FontMetrics fm = g.getFontMetrics();
	    int x = (28*8*Game.S - fm.stringWidth(str)) / 2;
		g.drawString(str, x, y);
	}
	
}
