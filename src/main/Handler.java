package main;

import java.awt.Graphics;
import java.util.LinkedList;

public class Handler
{
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	public void tick()
	{
		for(int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			
			tempObject.tick();
		}
	}
	
	public void render(Graphics g)
	{
		for(int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			
			tempObject.render(g);
		}
	}
	
	public void addObject(GameObject object)
	{
		this.object.add(object);
	}
	
	public void removeObject(GameObject object)
	{
		this.object.remove(object);
	}
	
	public void clearEnemies()
	{
		int length = object.size();
		for(int i = 0; i < length; i++) {
			this.object.remove(0);
		}
	}
	
	public int countEnemyOfId(ID id)
	{
		int amount = 0;
		int length = object.size();
		for(int i = 0; i < length; i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getID() == id) amount++;
		}
		return amount;
	}
	
	public Pacman getPacman()
	{
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getID() == ID.PACMAN) return (Pacman) tempObject;
		}
		return null;
	}
	
	public GameObject getObject(ID id)
	{
		for(int i = 0; i <  object.size(); i++) {
			GameObject tempObject = object.get(i);
			if(tempObject.getID() == id) return tempObject;
		}
		return null;
	}
	
	public void scareGhosts()
	{
		for(int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			if(tempObject.getID() == ID.GHOST_CYAN ||
			   tempObject.getID() == ID.GHOST_RED ||
			   tempObject.getID() == ID.GHOST_ORANGE ||
			   tempObject.getID() == ID.GHOST_PINK
			  )
			{
				Ghost ghost = (Ghost) tempObject;
				ghost.setMode("SCARED");
			}
		}
	}
	
	public void wakeGhosts()
	{
		for(int i = 0; i < object.size(); i++)
		{
			GameObject tempObject = object.get(i);
			if(tempObject.getID() == ID.GHOST_CYAN ||
			   tempObject.getID() == ID.GHOST_RED ||
			   tempObject.getID() == ID.GHOST_ORANGE ||
			   tempObject.getID() == ID.GHOST_PINK
			  )
			{
				Ghost ghost = (Ghost) tempObject;
				ghost.setMode("ALIVE");
			}
		}
	}
	
	public void resetPositions()
	{
		getPacman().setX((104+4)*Game.S);
		getPacman().setY(184*Game.S);
		
		getObject(ID.GHOST_RED).setX((104+4)*Game.S);
		getObject(ID.GHOST_RED).setY(88*Game.S);
		/*
		getObject(ID.GHOST_CYAN).setX((88+4)*Game.S);
		getObject(ID.GHOST_CYAN).setY(112*Game.S);
		
		getObject(ID.GHOST_ORANGE).setX((104+4)*Game.S);
		getObject(ID.GHOST_ORANGE).setY(112*Game.S);
		
		getObject(ID.GHOST_PINK).setX((120+4)*Game.S);
		getObject(ID.GHOST_PINK).setY(112*Game.S);
		*/
		getObject(ID.GHOST_CYAN).setX((104+4)*Game.S);
		getObject(ID.GHOST_CYAN).setY(88*Game.S);
		
		getObject(ID.GHOST_ORANGE).setX((104+4)*Game.S);
		getObject(ID.GHOST_ORANGE).setY(88*Game.S);
		
		getObject(ID.GHOST_PINK).setX((104+4)*Game.S);
		getObject(ID.GHOST_PINK).setY(88*Game.S);
	}
}
