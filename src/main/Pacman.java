package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import level.MazeMaker;

public class Pacman extends GameObject
{
	private Handler handler;
	private AudioPlayer audioplayer;
	private int speed, tempSpeed;
	private int size;
	private int anim, animDeath;
	private BufferedImage[][] img = new BufferedImage[4][2];
	private BufferedImage[] img_death = new BufferedImage[12];
	private Image[][] imgScaled = new Image[4][2];
	private Image[] img_deathScaled = new Image[12];
	private MODE mode;
	private boolean canTurn[] = {false, false, false, false};
	private int pointerX, pointerY;
	//private int frontPointerX, frontPointerY;
	//private int centerPointerX, centerPointerY;
	
	private boolean debug = true;
	
	private int score, highscore;
	private int lives;
	private int bonusLiveCounter = 1;
	private boolean highscore_beaten = false;
	private int ghostStreak = 1;
	

	public enum MODE
	{
		DEAD,
		ALIVE,
		WAITING
	}
	
	public Pacman(int x, int y, Handler handler, AudioPlayer audioplayer)
	{
		super(x, y, ID.PACMAN);
		this.handler = handler;
		this.audioplayer = audioplayer;
		speed = 1;
		size = 15;
		tempSpeed = speed;
		anim = 0;
		animDeath = 0;
		mode = MODE.WAITING;
		
		score = 0;
		highscore = new WriteToFile().loadHighscore();
		if(highscore < 1000) highscore = 1000;
		lives = 3;
		
		
		SpriteSheet ss = new SpriteSheet(AssetManager.ss_pacman, size);
		// right
		img[0][0] = ss.grabImage(1, 1, size, size);
		img[0][1] = ss.grabImage(1, 2, size, size);
		
		// left
		img[1][0] = ss.grabImage(2, 1, size, size);
		img[1][1] = ss.grabImage(2, 2, size, size);
		
		// up
		img[2][0] = ss.grabImage(3, 1, size, size);
		img[2][1] = ss.grabImage(3, 2, size, size);
		
		// down
		img[3][0] = ss.grabImage(4, 1, size, size);
		img[3][1] = ss.grabImage(4, 2, size, size);
		
		// death
		for(int i = 0; i < img_death.length; i++)
		{
			img_death[i] = ss.grabImage(i+1, 3, size, size);
		}
		
		rescale();
	}
	
	public Rectangle getBounds()
	{
		//return new Rectangle(x, y, size*Game.S, size*Game.S);
		return new Rectangle(x+(4*Game.S), y+(4*Game.S), 1*Game.S, 1*Game.S);
	}
	
	public int getGridX()
	{
		return (x/8/Game.S);
	}
	
	public int getGridY()
	{
		return (y/8/Game.S);
	}
	
	public void tick()
	{
		if(getMode() == MODE.WAITING) return;
		
		if(lives == 0)
		{
			lives = -1;
			 if(score > highscore) highscore = score;
			new WriteToFile().saveHighscore(score);
			score = 0;
			
		}
		
		audioplayer.delay++;
		//if(Game.gameState == Game.STATE.Menu) goToMouse();
		if(getMode() == MODE.ALIVE)
		{
			if(MazeMaker.getWall(getGridX(), getGridY()) == 01)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.PACDOT);
				audioplayer.playPacmanChomp();
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 02)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.POWERUP);
				handler.scareGhosts();
				audioplayer.playPacmanChomp();
				audioplayer.playGhostSiren();
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 05)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.CHERRY);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 15)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.STRAWBERRY);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 25)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.ORANGE);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 35)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.APPLE);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 45)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.MELON);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 55)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.GALAXIAN);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 65)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.BELL);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			else if(MazeMaker.getWall(getGridX(), getGridY()) == 75)
			{
				MazeMaker.inGrid(00, getGridX(), getGridY());
				addScore(Points.KEY);
				audioplayer.playSFX("pacman_eatfruit.wav");
			}
			//else audioplayer.stopChomp();
			
			Ghost ghost_cyan = (Ghost) handler.getObject(ID.GHOST_CYAN);
			Ghost ghost_orange = (Ghost) handler.getObject(ID.GHOST_ORANGE);
			Ghost ghost_pink = (Ghost) handler.getObject(ID.GHOST_PINK);
			Ghost ghost_red = (Ghost) handler.getObject(ID.GHOST_RED);
			
			if(!ghost_cyan.isScared() && !ghost_orange.isScared() && !ghost_pink.isScared() && !ghost_red.isScared())
			{
				ghostStreak = 1;
			}
			
			if(velX == 0 && x != getGridX()*8*Game.S)
			{
				//if(x < getGridX()*8*Game.S)
				//x++;
				//else x--;
				
				//System.out.println("x:" + x%28*16*Game.S);
				//x = getGridX()*8*Game.S;
				x = x/8/Game.S*8*Game.S;
			}
			if(velY == 0 && y != getGridY()*8*Game.S)
			{
				//if(y < getGridY()*8*Game.S)
				//y++;
				//else y--;
				
				//y = getGridY()*8*Game.S;
				y = y/8/Game.S*8*Game.S;
			}
			
			Pointer();
			
			if(MazeMaker.isWalkable(getGridX(), getGridY()-1))
			{
				canTurn[0] = true;
			}
			else canTurn[0] = false;
			
			if(MazeMaker.isWalkable(getGridX()-1, getGridY()))
			{
				canTurn[1] = true;
			}
			else canTurn[1] = false;
			
			if(MazeMaker.isWalkable(getGridX(), getGridY()+1))
			{
				canTurn[2] = true;
			}
			else canTurn[2] = false;
			
			if(MazeMaker.isWalkable(getGridX()+1, getGridY()))
			{
				canTurn[3] = true;
			}
			else canTurn[3] = false;
			
			/*
			if(velX == 0 && x != pointerX*8*Game.S)
				x = pointerX*8*Game.S;
			
			if(velY == 0 && y != pointerY*8*Game.S)
				y = pointerY*8*Game.S;
			*/
			
			if(!(velX == 0 && velY == 0)) animation_move();
			//new AudioPlayer().playSFX(AssetManager.pacman_chomp);
			// Front Checker
			
			//if(MazeMaker.isWalkable(getGridX()+velX/Game.S, getGridY()+velY/Game.S))
			
			if(MazeMaker.isWalkable(pointerX+velX/Game.S, pointerY+velY/Game.S))
			{
				//System.out.println();
				//System.out.println("isWalkable["+tx+"]["+ty+"] " + MazeMaker.isWalkable(tx, ty));
				//System.out.println((x/8/Game.S)+velX/Game.S);
				x += velX;	
				y += velY;
			}
			else
			{
				velX = 0;
				velY = 0;
			}
			
			/*
			if(canTurn[2+velX/Game.S])
				x += velX;	
			else
				velX = 0;
			
			if(canTurn[1+velY/Game.S])
				y += velY;
			else
				velY = 0;
			*/
			
			if(x > (28*8)*Game.S) x = 0-size*Game.S;
			else if(x < 0-size*Game.S) x = (28*8)*Game.S;
			
			if(y > (31*8)*Game.S) y = 0-size*Game.S;
			else if(y < 0-size*Game.S) y = (31*8)*Game.S;
		}
		else if (animDeath < 12) animation_death();

	}
	
	private void Pointer()
	{
		if(velX < 0) pointerX = ((x+(7*Game.S))/8/Game.S);
		else if(velX > 0) pointerX = (x/8/Game.S);
		else pointerX = ((x+3*Game.S)/8/Game.S);
		
		if(velY < 0) pointerY = ((y+(7*Game.S))/8/Game.S);
		else if (velY > 0) pointerY = (y/8/Game.S);
		else pointerY = ((y+3*Game.S)/8/Game.S);
		
		//System.out.println(pointerX);
		/*
		if(velX < 0) frontPointerX = (x/8/Game.S);
		else if(velX > 0) frontPointerX = ((x-(7*Game.S))/8/Game.S);
		else frontPointerX = ((x+3*Game.S)/8/Game.S);
		
		if(velY < 0) frontPointerY = (y/8/Game.S);
		else if (velY > 0) frontPointerY = ((y-(7*Game.S))/8/Game.S);
		else frontPointerY = ((y+3*Game.S)/8/Game.S);
		
		centerPointerX =  Math.round(((x+3*Game.S)/8/Game.S));
		centerPointerY =  Math.round(((y+3*Game.S)/8/Game.S));
		*/
	}
	
	public void render(Graphics g)
	{
		
		//Graphics2D g2d = (Graphics2D) g;
		
		//g.setColor(Color.white);
		//g.fillRect(x, y, size, size);
		
		if(getMode() == MODE.ALIVE) g.drawImage(imgScaled[imgID][anim], x-(8/2*Game.S), y-(8/2*Game.S), null);
		else if(animDeath < 12) g.drawImage(img_deathScaled[animDeath], x-(8/2*Game.S), y-(8/2*Game.S), null);

		debug = false;
		if(debug)
		{
			g.setColor(Color.green);
			//g.drawOval(x-(size*Game.S/2), y-(size/2*Game.S), size*Game.S, size*Game.S);
			
			g.drawOval(x-(8/2*Game.S), y-(8/2*Game.S), size*Game.S, size*Game.S);
			
			//g.drawRect(x, y, Game.S*4, Game.S*4);
			//g.drawLine(x, y-(size*2), x, y+(size*2));
			//g.drawLine(x-(size*2), y, x+(size*2), y);
			//g.drawLine(x, y, Game.getMX(), Game.getMY());
			g.setColor(Color.red);
			g.drawLine(x+4*Game.S, y+4*Game.S, Game.getMX(), Game.getMY());
			
			/* 
			g.setColor(Color.cyan);
			g.drawLine(x, y, handler.getObject(ID.GHOST_CYAN).getX(), handler.getObject(ID.GHOST_CYAN).getY());
			g.setColor(Color.red);
			g.drawLine(x, y, handler.getObject(ID.GHOST_RED).getX(), handler.getObject(ID.GHOST_RED).getY());
			g.setColor(Color.orange);
			g.drawLine(x, y, handler.getObject(ID.GHOST_ORANGE).getX(), handler.getObject(ID.GHOST_ORANGE).getY());
			g.setColor(Color.pink);
			g.drawLine(x, y, handler.getObject(ID.GHOST_PINK).getX(), handler.getObject(ID.GHOST_PINK).getY());
			*/
			
			g.setFont(new Font("Arial", Font.PLAIN, 18));
			g.setColor(Color.white);
			g.drawString(getGridX() + ", " + getGridY(), x-3*Game.S, y-4*Game.S);
		}
		
	}
	
	public void rescale()
	{
		
		for(int y = 0; y < 4; y++)
		{
			for(int x = 0; x < 2; x++)
			{
				imgScaled[y][x] = img[y][x].getScaledInstance(size*Game.S, size*Game.S, 0);
			}
		}
		
		for(int y = 0; y < 12; y++)
		{
			img_deathScaled[y] = img_death[y].getScaledInstance(size*Game.S, size*Game.S, 0);
		}
		
		speed = tempSpeed*Game.S;
	}
	
	public void setSpeed(int speed)
	{
		this.tempSpeed = speed;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	int c = 0;
	public void animation_move()
	{
		//audioplayer.playPacmanChomp();
		if(c >= 8)
		{
			c = 0;
			switch(anim)
			{
				case 0: anim = 1; break;
				case 1: anim = 0; break;
			}
		}
		c++;
	}
	
	int d = 0;
	public void animation_death() {
		audioplayer.playSFX(AssetManager.pacman_death);
		velX = 0;
		velY = 0;
		if(d >= 8)
		{
			switch(animDeath)
			{
				case  0: animDeath =  1; lives--; break;
				case  1: animDeath =  2; break;
				case  2: animDeath =  3; break;
				case  3: animDeath =  4; break;
				case  4: animDeath =  5; break;
				case  5: animDeath =  6; break;
				case  6: animDeath =  7; break;
				case  7: animDeath =  8; break;
				case  8: animDeath =  9; break;
				case  9: animDeath = 10; break;
				case 10: animDeath = 11; break;
				case 11: animDeath = 12; respawn(); break;
			}
			d = 0;
		}
		d++;
	}

	public void goToMouse()
	{
			if(x+10*Game.S < Game.getMX())
			{
				velX = speed;
				velY = 0;
				setImgID(3);
			}
			else if(x-10*Game.S > Game.getMX())
			{
				velX = -speed;
				velY = 0;
				setImgID(1);
			}
			else
			{
				velX = 0;
				
				if(y+10*Game.S < Game.getMY())
				{
					velY = speed;
					setImgID(2);
				}
				else if(y-10*Game.S > Game.getMY())
				{
					velY = -speed;
					setImgID(0);
				}
				else
				{
					velY = 0;
				}
			}
			
			if(velX == 0 && velY == 0) setMode(MODE.DEAD);
			
	}

	public int getAnimDeath() {
		return animDeath;
	}
	
	public void setAnimDeath(int animDeath) {
		this.animDeath = animDeath;
	}

	public boolean[] getCanTurn() {
		return canTurn;
	}
	
	public boolean getCanTurn(int i) {
		return canTurn[i];
	}

	public void setCanTurn(boolean canTurn[]) {
		this.canTurn = canTurn;
	}

	public int getScore()
	{
		return score;
	}

	public void addScore(int score)
	{
		this.score += score;
		if(this.score >= 10000*bonusLiveCounter && this.score - score < 10000*bonusLiveCounter)
		{
			bonusLiveCounter++;
			lives++;
		}
		
		if(this.score > highscore && !highscore_beaten)
		{
			Game.pauseForTicks((int)(2*Game.TICKS));
			highscore_beaten = true;
			audioplayer.playSFX("pacman_highscore.wav");
			//System.out.println( audioplayer.getPlay().getMicrosecondLength()/10000);
		}
	}
	
	public void respawn()
	{
		if(lives > 0)
		{
			handler.resetPositions();
			Game.pauseForTicks((int)(4*Game.TICKS));
			
			handler.wakeGhosts();
			setAlive();
			anim = 0;
			audioplayer.playBeginningTune();
		}
		else
		{
			Game.paused = true;
		}
	}
	
	public void playSFXEatGhost()
	{
		audioplayer.playSFX("pacman_eatghost.wav");
	}

	public MODE getMode() {
		return mode;
	}

	public void setMode(MODE mode) {
		this.mode = mode;
	}

	public void setDead(boolean dead)
	{
		if(dead)
		{
			mode = MODE.DEAD;
		}
		else mode = MODE.ALIVE;
	}
	
	public boolean isDead()
	{
		if(mode == MODE.DEAD) return true;
		else return false;
	}
	
	public void setWaiting()
	{
		mode = MODE.WAITING;
	}
	
	public void setAlive()
	{
		animDeath = 0;
		mode = MODE.ALIVE;
	}
	
	public int getHighScore()
	{
		return highscore;
	}
	
	public void setLives(int lives)
	{
		this.lives = lives;
	}
	
	public int getLives()
	{
		return lives;
	}
	
	public void setHighscoreBeaten(boolean hsb)
	{
		highscore_beaten = hsb;
	}
	
	public boolean getHighscoreBeaten()
	{
		return highscore_beaten;
	}
	
	public boolean isWaiting()
	{
		if(mode == MODE.WAITING) return true;
		else return false;
	}
	
	public void setGhostStreak(int ghostStreak)
	{
		this.ghostStreak = ghostStreak;
	}
	
	public int getGhostStreak()
	{
		return ghostStreak;
	}
	
	
	
}
