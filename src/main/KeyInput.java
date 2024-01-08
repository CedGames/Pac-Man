package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import level.MazeMaker;

public class KeyInput extends KeyAdapter
{
	private Handler handler;
	private boolean[] keyDown = new boolean[4];
	
	public KeyInput(Handler handler)
	{
		this.handler = handler;
		
		keyDown[0] = false; // W
		keyDown[1] = false; // A
		keyDown[2] = false; // S
		keyDown[3] = false; // D
	}
	
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode(); // https://theasciicode.com.ar/

		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) keyDown[0] = true;
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) keyDown[1] = true;
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) keyDown[2] = true;
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) keyDown[3] = true;
		
		if(Game.gameState == Game.STATE.MazeMaker || Game.gameState == Game.STATE.Game) playerMovement();
		
		
		if(key == KeyEvent.VK_ESCAPE) Game.gameState = Game.STATE.Menu;
		
		if(key == KeyEvent.VK_P && Game.gameState == Game.STATE.Game)
		{
			if(Game.paused) Game.paused = false;
			else Game.paused = true;
		}
		if(key == KeyEvent.VK_F)
		{
			Ghost ghost = (Ghost) handler.getObject(ID.GHOST_CYAN);
			ghost.moveToPacDir();
		}
		
		if(key == KeyEvent.VK_L)
		{
			if(MazeMaker.drawLines) MazeMaker.drawLines = false;
			else MazeMaker.drawLines = true;
		}
		
		if(Game.gameState == Game.STATE.MazeMaker)
		{
			//Pacman pacman = handler.getPacman();
			//if(key == KeyEvent.VK_E) pacman.setDead(true);
			
			if(key == KeyEvent.VK_R)
			{
				if(MazeMaker.getRotation() < 3)
				{
					MazeMaker.setRotation(MazeMaker.getRotation()+1);
				}
				else MazeMaker.setRotation(0);
			}
			/*
			if(e.isShiftDown())
			{
				MazeMaker.setShift(4);
			}
			else MazeMaker.setShift(0);
			*/
			
			if(key == KeyEvent.VK_X) MazeMaker.switchMirrorHorizontally();
			if(key == KeyEvent.VK_Y) MazeMaker.switchMirrorVertically();
			
			if(key == KeyEvent.VK_0) MazeMaker.setType(00);
			if(key == KeyEvent.VK_1) MazeMaker.setType(10);
			if(key == KeyEvent.VK_2) MazeMaker.setType(20);
			if(key == KeyEvent.VK_3) MazeMaker.setType(30);
			if(key == KeyEvent.VK_4) MazeMaker.setType(40);
			if(key == KeyEvent.VK_5) MazeMaker.setType(50);
			
			if(MazeMaker.state == MazeMaker.STATE.Save)
			{
				if(key > 47 && key < 122 && Menu.text.length() < 28) // https://theasciicode.com.ar/ "normale" buchstaben und zeichen erlauben
				{
					/*
					String text = KeyEvent.getKeyText(key);
					Menu.text += text;
					System.out.print("Key: " + key);
					System.out.println(" Text: " + text);
					*/
					//Menu.text += KeyEvent.getKeyText(key);
					
					if(e.isShiftDown()) Menu.text += KeyEvent.getKeyText(KeyEvent.getExtendedKeyCodeForChar(key));
					else Menu.text += KeyEvent.getKeyText(KeyEvent.getExtendedKeyCodeForChar(key)).toLowerCase();
				}
				else if(key == KeyEvent.VK_MINUS && Menu.text.length() < 28)
				{
					if(e.isShiftDown()) Menu.text += "_";
					else Menu.text += "-";
				}
				
				
				if(key == KeyEvent.VK_BACK_SPACE) // löschen
				{
					if(Menu.text.length() > 0) Menu.text = Menu.text.substring(0, Menu.text.length()-1); // https://xenovation.com/blog/development/java/remove-last-character-from-string-java
				}
				
			}
			
			if(key == KeyEvent.VK_CONTROL && !MazeMaker.preview) MazeMaker.preview = true;
			else if (key == KeyEvent.VK_CONTROL && MazeMaker.preview) MazeMaker.preview = false;
			
			
			
		}

	}
	
	public void keyReleased(KeyEvent e)
	{
		int key = e.getKeyCode(); // https://theasciicode.com.ar/
		//Pacman pacman = handler.getPacman();
		
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) keyDown[0] = false;
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) keyDown[1] = false;
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) keyDown[2] = false;
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) keyDown[3] = false;
	}
	
	public void playerMovement()
	{
		Pacman pacman = handler.getPacman();
		
			int speed = pacman.getSpeed();
			if(keyDown[0] && pacman.getCanTurn(0))
			{
				keyDown[0] = true;
				pacman.setVelY(-speed);
				pacman.setImgID(0);
				pacman.setVelX(0);
			}
			if(keyDown[1] && pacman.getCanTurn(1))
			{
				keyDown[1] = true;
				pacman.setVelX(-speed);
				pacman.setImgID(1);
				pacman.setVelY(0);
			}
			
			if(keyDown[2] && pacman.getCanTurn(2))
			{
				keyDown[2] = true;
				pacman.setVelY(speed);
				pacman.setImgID(2);
				pacman.setVelX(0);
			}
			if(keyDown[3] && pacman.getCanTurn(3))
			{
				keyDown[3] = true;
				pacman.setVelX(speed);
				pacman.setImgID(3);
				pacman.setVelY(0);
			}
	}
	
}
