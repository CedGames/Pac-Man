package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import level.MazeMaker;

public class Ghost extends GameObject
{
	private Handler handler;
	private int speed, tempSpeed;
	private int size;
	private int anim, animDeath;
	private BufferedImage[] img = new BufferedImage[8];
	private Image[] imgScaled = new Image[8];
	
	private BufferedImage[] img_scared = new BufferedImage[4];
	private Image[] img_scaredScaled = new Image[4];
	
	private BufferedImage[] img_eyes = new BufferedImage[4];
	private Image[] img_eyesScaled = new Image[4];
	private int color, facing;
	private MODE mode;
	private boolean canTurn[] = {false, false, false, false};
	private int lastGridX, lastGridY;
	
	private int scaredTicker;
	private int whiteBlink;
	private int pointerX, pointerY;
	
	private enum MODE
	{
		DEAD,
		SCARED,
		ALIVE,
		WAITING
	}
	
	public Ghost(int x, int y, ID id, Handler handler)
	{
		super(x, y, id);
		this.handler = handler;
		speed = 1;
		size = 14;
		tempSpeed = speed;
		anim = 0;
		animDeath = 0;
		mode = MODE.WAITING;
	
		scaredTicker = 0;
		whiteBlink = 0;
		lastGridX = 0;
		lastGridY = 0;
		
		switch(id)
		{
			case GHOST_CYAN: color = 0; break; // inky
			case GHOST_ORANGE: color = 1; break; // clyde
			case GHOST_PINK: color = 2; break; // pinky
			case GHOST_RED: color = 3; break; // blinky
			default: break;
		}
		
		SpriteSheet ss = new SpriteSheet(AssetManager.ss_ghosts_items, size);
		
		// down
		img[0] = ss.grabImage(1, color+1, size, size);
		img[1] = ss.grabImage(2, color+1, size, size);
		
		// left
		img[2] = ss.grabImage(3, color+1, size, size);
		img[3] = ss.grabImage(4, color+1, size, size);
		
		// right
		img[4] = ss.grabImage(5, color+1, size, size);
		img[5] = ss.grabImage(6, color+1, size, size);
		
		// up
		img[6] = ss.grabImage(7, color+1, size, size);
		img[7] = ss.grabImage(8, color+1, size, size);
		
		// white
		img_scared[0] = ss.grabImage(1, 5, size, size);
		img_scared[1] = ss.grabImage(2, 5, size, size);
		
		// blue
		img_scared[2] = ss.grabImage(3, 5, size, size);
		img_scared[3] = ss.grabImage(4, 5, size, size);
		
		// down
		img_eyes[0] = ss.grabImage(1, 6, size, size);
		
		// left
		img_eyes[1] = ss.grabImage(2, 6, size, size);
		
		// right
		img_eyes[2] = ss.grabImage(3, 6, size, size);
		
		// up
		img_eyes[3] = ss.grabImage(4, 6, size, size);
		
		
		rescaleImages();
	}

	public Rectangle getBounds()
	{
		return new Rectangle(x, y, size*Game.S, size*Game.S);
	}

	int u = 0;
	int walkingTimeout = 0;
	public void tick()
	{
		if(mode == MODE.WAITING) return; // wenn Ghost wartet soll keine Logik überprüft werden
		
		
		if(velX == 0 && x != getGridX()*8*Game.S) x = getGridX()*8*Game.S;
		if(velY == 0 && y != getGridY()*8*Game.S) y = getGridY()*8*Game.S;
			
			//speed = 1;
			speed = tempSpeed*Game.S;
			animation_move();
			animation_facing();
			imgID = facing+anim;
			
			if(mode == MODE.SCARED)
			{
				scaredTicker++;
				if(scaredTicker >= (int) Game.TICKS*8)
				{
					scaredTicker = 0;
					whiteBlink = 2;
					mode = MODE.ALIVE;
					AudioPlayer.stopMusic();
				}
				else if(scaredTicker >= (int) Game.TICKS*4) // 4 Sekunden
				{
					
					// weißes blinken
					//System.out.println(anim);
					if(u >= 16)
					{
						u = 0;
						switch(whiteBlink)
						{
							case 0: whiteBlink = 1; break;
							case 1: whiteBlink = 2; break;
							case 2: whiteBlink = 0; break;
						}
					}
					u++;
				}
			}
			
			if(MazeMaker.isWalkable(getGridX(), getGridY()-1))
			{
				// UP
				canTurn[0] = true;
			}
			else canTurn[0] = false;
			
			if(MazeMaker.isWalkable(getGridX()-1, getGridY()))
			{
				// LEFT
				canTurn[1] = true;
			}
			else canTurn[1] = false;
			
			if(MazeMaker.isWalkable(getGridX(), getGridY()+1))
			{
				// DOWN
				canTurn[2] = true;
			}
			else canTurn[2] = false;
			
			if(MazeMaker.isWalkable(getGridX()+1, getGridY()))
			{
				// RIGHT
				canTurn[3] = true;
			}
			else canTurn[3] = false;
			
			if(mode == mode.ALIVE)
			{
				/*if(lastGridX != getGridX() || lastGridY != getGridY())*/ moveToPacDir();
			}
			else if(mode == mode.SCARED)
			{
				int px = handler.getPacman().getGridX();
				int py = handler.getPacman().getGridY();
				
				goFromTo(getGridX(), getGridY(), 28-px, 31-py);
			}
			else if(mode == mode.DEAD)
			{
				if(getGridX() == 13 && getGridY() == 11)
				{
					mode = MODE.ALIVE;
				}
				else goFromTo(getGridX(), getGridY(), 13, 11);
			}
			/*
			if(lastGridX == getGridX() && lastGridY == getGridY())
			{
				if(walkingTimeout >= 40) moveToPacDir();
				walkingTimeout++;
			}
			*/
			Pointer();
			if(MazeMaker.isWalkable(pointerX+velX/Game.S, pointerY+velY/Game.S))
			{
				x += velX;	
				y += velY;
			}
			else
			{
				velX = 0;
				velY = 0;
			}
			
			if(x > (28*8)*Game.S) x = 0-size*Game.S;
			else if(x < 0-size*Game.S) x = (28*8)*Game.S;
			
			if(y > (31*8)*Game.S) y = 0-size*Game.S;
			else if(y < 0-size*Game.S) y = (31*8)*Game.S;
			collision();
		
	}
	
	private void collision()
	{
		for(int i = 0; i < handler.object.size(); i++)
		{
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getID() == ID.PACMAN)
			{
				if(getBounds().intersects(tempObject.getBounds()))
				{
					Pacman pacman = (Pacman) tempObject;
					
					if(mode == MODE.SCARED)
					{
						pacman.playSFXEatGhost();
						mode = MODE.DEAD;
						int streak = pacman.getGhostStreak();
						pacman.addScore(Points.GHOST*streak);
						pacman.setGhostStreak(streak++);
					}
					else if(mode == MODE.ALIVE)
					{
						pacman.setDead(true);
					}
					
				}

			}
			
		}
		
	}
	
	private void Pointer()
	{
		if(velX < 0) pointerX = ((x+(7*Game.S))/8/Game.S);
		else if(velX > 0) pointerX = (x/8/Game.S);
		else pointerX = ((x+4*Game.S)/8/Game.S);
		
		if(velY < 0) pointerY = ((y+(7*Game.S))/8/Game.S);
		else if (velY > 0) pointerY = (y/8/Game.S);
		else pointerY = ((y+7*Game.S)/8/Game.S);
		
		//System.out.println(pointerX);
	}
	
	public int getGridX()
	{
		return ((x+4*Game.S)/8/Game.S);
	}
	
	public int getGridY()
	{
		return ((y+4*Game.S)/8/Game.S);
	}

	public void render(Graphics g)
	{
		//Graphics2D g2d = (Graphics2D) g;
		
		//g.setColor(Color.white);
		//g.fillRect(x, y, size, size);
		if(handler.getPacman().isDead()) return;
		else if(mode == MODE.SCARED) g.drawImage(img_scaredScaled[anim+whiteBlink], x-(8/2*Game.S), y-(8/2*Game.S), null);
		else if(mode == MODE.ALIVE) g.drawImage(imgScaled[imgID], x-(8/2*Game.S), y-(8/2*Game.S), null);
		else if(mode == MODE.DEAD) g.drawImage(img_eyesScaled[facing/2], x-(8/2*Game.S), y-(8/2*Game.S), null);
		else if(mode == MODE.WAITING) g.drawImage(imgScaled[imgID], x-(8/2*Game.S), y-(8/2*Game.S), null);
		
		//else if(animDeath < 12) g.drawImage(img_deathScaled[animDeath], x, y, null);
		
		//g.setColor(Color.green);
		//g2d.draw(getBounds());
		//g.drawRect(x, y, size*Game.S, size*Game.S);
		/*
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		g.setColor(Color.white);
		g.drawString(getGridX() + ", " + getGridY(), x-3*Game.S, y-4*Game.S);
		*/
		
	}
	
	public void rescaleImages()
	{
		
		for(int y = 0; y < img.length; y++)
		{
			imgScaled[y] = img[y].getScaledInstance(size*Game.S, size*Game.S, 0);
		}
		for(int y = 0; y < img_scared.length; y++)
		{
			img_scaredScaled[y] = img_scared[y].getScaledInstance(size*Game.S, size*Game.S, 0);
		}
		for(int y = 0; y < img_eyes.length; y++)
		{
			img_eyesScaled[y] = img_eyes[y].getScaledInstance(size*Game.S, size*Game.S, 0);
		}
		
		speed = tempSpeed * Game.S;
		
	}
	
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public void animation_facing()
	{
		if(velY > 0) facing = 0; // down
		else if(velX < 0) facing = 2; // left
		else if (velX > 0) facing = 4; // right
		else if (velY < 0) facing = 6; // up
		else facing = 0;
	}
	
	int c = 0;
	public void animation_move()
	{
		if(c >= 10)
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
	
	int currentDir = -1;
	public void goFromTo(int fromX, int fromY, int toX, int toY)
	{
		
		//int[][] Grid = MazeMaker.getGrid();
		
		//int px = handler.getPacman().getGridX();
		//int py = handler.getPacman().getGridY();
		boolean[] valid = {false, false, false, false};
		
		if(canTurn[0] && fromY > toY)
		{
			// UP
			valid[0] = true;
		}
		else valid[0] = false;
		if(canTurn[1] && fromX > toX)
		{
			// LEFT
			valid[1] = true;
		}
		else valid[1] = false;
		if(canTurn[2] && fromY < toY)
		{
			// DOWN
			valid[2] = true;
		}
		else valid[2] = false;
		if(canTurn[3] && fromX < toX)
		{
			// RIGHT
			valid[3] = true;
		}
		else valid[3] = false;
		
		Random r = new Random();
		
		
			int dir = r.nextInt(4);
			
			//currentDir = dir;
			
			if(valid[0] || valid[1] || valid[2] || valid[3] )
			{
				while(!valid[dir])
					dir = r.nextInt(4);
					
					if(dir == 0)
					{
						setVelY(-speed);
						setVelX(0);
					}
					else if(dir == 1)
					{
						setVelX(-speed);
						setVelY(0);
					}
					else if(dir == 2)
					{
						setVelY(speed);
						setVelX(0);
					}
					else if(dir == 3)
					{
						setVelX(speed);
						setVelY(0);
					}	
			}
			else if(valid[0] && valid[1] && valid[2] && valid[3] )
			{
					
					if(dir == 0)
					{
						setVelY(-speed);
						setVelX(0);
					}
					else if(dir == 1)
					{
						setVelX(-speed);
						setVelY(0);
					}
					else if(dir == 2)
					{
						setVelY(speed);
						setVelX(0);
					}
					else if(dir == 3)
					{
						setVelX(speed);
						setVelY(0);
					}	
			}
			//else randomMovement();
		
	}
	
	public void randomMovement()
	{
		Random r = new Random();
		
		int dir = r.nextInt(4);
		
		if(dir == currentDir) dir = -1;
		else if(dir == 2 && currentDir == 0) dir = -1;
		else if(dir == 0 && currentDir == 2) dir = -1;
		else if(dir == 1 && currentDir == 3) dir = -1;
		else if(dir == 3 && currentDir == 1) dir = -1;
		
		if(dir != -1)
		{
			if(canTurn[dir])
			{
				if(dir == 0)
				{
					setVelY(-speed);
					setVelX(0);
					currentDir = 0;
				}
				else if(dir == 1)
				{
					setVelX(-speed);
					setVelY(0);
					currentDir = 1;
				}
				else if(dir == 2)
				{
					setVelY(speed);
					setVelX(0);
					currentDir = 2;
				}
				else if(dir == 3)
				{
					setVelX(speed);
					setVelY(0);
					currentDir = 3;
				}
			}
		}
		else randomMovement();
		
	}
	
	public int[][] getPath(int fromX, int fromY, int toX, int toY)
	{
		boolean pathFound = false;
		int dx = toX - fromX;
		int dy = toY - fromY;
		int heuristic = dx+dy;
		//System.out.println(heuristic);
		
		int currentX = fromX;
		int currentY = fromY;
		
		return null;
	}
	
	public boolean[] canTurn(int gridX, int gridY)
	{
		boolean[] cTurn = {false, false, false, false};
		if(MazeMaker.isWalkable(gridX, gridY-1))
		{
			// UP
			cTurn[0] = true;
		}
		else cTurn[0] = false;
		
		if(MazeMaker.isWalkable(gridX-1, gridY))
		{
			// LEFT
			cTurn[1] = true;
		}
		else cTurn[1] = false;
		
		if(MazeMaker.isWalkable(gridX, gridY+1))
		{
			// DOWN
			cTurn[2] = true;
		}
		else cTurn[2] = false;
		
		if(MazeMaker.isWalkable(gridX+1, gridY))
		{
			// RIGHT
			cTurn[3] = true;
		}
		else cTurn[3] = false;
		
		return cTurn;
	}
	
	public int moveToPacDir()
	{
		walkingTimeout = 0;
		int goX = 0, goY = 0;
		
		Pacman pacman = handler.getPacman();
		int px = pacman.getGridX();
		int py = pacman.getGridY();
		
		if(color == 0) // cyan
		{
			int poffsetX = pacman.getVelX()/Game.S*2;
			int poffsetY = pacman.getVelY()/Game.S*2;
			int rx = handler.getObject(ID.GHOST_RED).getX()/8/Game.S;
			int ry = handler.getObject(ID.GHOST_RED).getY()/8/Game.S;
			int diffX = rx - px;
			int diffY = ry - py;
			
			goX = px+poffsetX+diffX;
			goY = py+poffsetY+diffY;
			
		}
		else if(color == 1)
		{
			int diffX = px-x;
			int diffY = py-y;
			if(diffX < 5 || diffY < 5)
			{
				goX = 0;
				goY = 31;
			}
			else
			{
				goX = px;
				goY = py;
			}
			
		}
		else if(color == 2)
		{
			int poffsetX = pacman.getVelX()/Game.S*4;
			int poffsetY = pacman.getVelY()/Game.S*4;
			goX = px+poffsetX;
			goY = py+poffsetY;
		}
		else
		{
			goX = px;
			goY = py;
		}
		//int nextCellX, nextCellY;
		
		lastGridX = getGridX();
		lastGridY = getGridY();
		
		//randomMovement();
		goFromTo(getGridX(), getGridY(), goX, goY);
		//getPath(getGridX(), getGridY(), px, py);
		/*
		if(MazeMaker.isWalkable(getGridX(), getGridY()))
		{
			
		}
		*/
		return 0;
	}

	public int getAnimDeath() {
		return animDeath;
	}
	
	public void setAnimDeath(int animDeath) {
		this.animDeath = animDeath;
	}
	
	public void setMode(String mode)
	{
		mode = mode.toLowerCase();
		if(mode.equals("scared")) 
		{
			if(this.mode != MODE.DEAD)
			this.mode = MODE.SCARED;
			scaredTicker = 0;
			whiteBlink = 2;
		}
		else if(mode.equals("alive")) this.mode = MODE.ALIVE;
		else if(mode.equals("dead")) this.mode = MODE.DEAD;
		else if(mode.equals("waiting")) this.mode = MODE.WAITING;
	}
	
	public boolean isScared()
	{
		if(mode == MODE.SCARED) return true;
		else return false;
	}
	
}
