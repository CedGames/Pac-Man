package main;

import java.awt.image.BufferedImage;

public class SpriteSheet
{
	private BufferedImage sprite;
	private int size;
	
	public SpriteSheet(BufferedImage sprite, int size)
	{
		this.sprite = sprite;
		this.size = size;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height)
	{
		BufferedImage img = sprite.getSubimage((row * size) - size, (col * size) - size, width, height);
		return img;
	}
}
