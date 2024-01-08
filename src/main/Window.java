package main;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window
{
	
	private JFrame frame;
	private Cursor tri;
	private Cursor select;
	
	public Window(int WIDTH, int HEIGHT, String title, String icon, Game game)
	{
		frame = new JFrame(title);
		frame.pack();
		WIDTH = WIDTH + frame.getInsets().left + frame.getInsets().right;
		HEIGHT = HEIGHT + frame.getInsets().top + frame.getInsets().bottom;

		frame.setSize(new Dimension(WIDTH, HEIGHT));

		tri = Toolkit.getDefaultToolkit()
		.createCustomCursor(new ImageIcon(getClass().getResource(AssetManager.cursor))
		.getImage() , new Point(0, 0), "cursor");
		select = Toolkit.getDefaultToolkit()
				.createCustomCursor(new ImageIcon(getClass().getResource("/assets/cursor/glove.png"))
						.getImage() , new Point(0, 0), "glove");
		
		frame.setCursor(tri);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(game);
		

		try
	    {
			frame.setIconImage(new ImageIcon(getClass().getResource(icon)).getImage());
	    }
	    catch(Exception e)
	    {
	      System.out.println("Datei '" + icon + "' nicht gefunden! Starte ohne ImageIcon...");
	    }
		
		frame.setVisible(true);
		game.start();
	}
	
	public void rescale(int WIDTH, int HEIGHT)
	{
		WIDTH = WIDTH + frame.getInsets().left + frame.getInsets().right;
		HEIGHT = HEIGHT + frame.getInsets().top + frame.getInsets().bottom;
		
		frame.pack();
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
	}
	
	public void centerFrame()
	{
		frame.setLocationRelativeTo(null);
	}
	
	public void changeCursor(String name)
	{
		if(name.equals("tri")) frame.setCursor(tri);	
		else if(name.equals("select")) frame.setCursor(select);
		else if(name.equals("text")) frame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
	}
	
	public JFrame getFrame()
	{
		return frame;
	}

}
