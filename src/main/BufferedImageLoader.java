package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageLoader
{
	BufferedImage image;
	
	public BufferedImage loadImage(String path)
	{
		try
		{
			image = ImageIO.read(getClass().getResource(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return image;
	}
	
	public BufferedImage loadExternalImage(String path)
	{
		try
		{
			image = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return image;
	}
	
}
