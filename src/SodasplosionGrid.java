import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The grid for Sodasplosion
 *
 * @author Alexander Shah
 * @version May 27, 2015
 */
public class SodasplosionGrid extends JPanel
{
	private final int IMAGE_WIDTH;
	private final int IMAGE_HEIGHT;

	private Image gridImages[];
	private Image playerImages[];
	private Image border;

	private int[][] grid;
	private int currentRowOne;
	private int currentColOne;
	private int currentRowTwo = 0;
	private int currentColTwo = 0;
	
	final int TIRE;
	final int MENTOS;
	final int CAN;
	final int BUILDING;
	final int CRATE;
	final int SODASPLOSION;

	/**
	 * Constructs a new grid
	 */
	public SodasplosionGrid()
	{
		// Loads up the player and breakable block images
		playerImages = new Image[2];
		playerImages[0] = new ImageIcon("RedTruck.png").getImage();
		playerImages[1] = new ImageIcon("BlueTruck.png").getImage();
		gridImages = new Image[6];
		// Leaves gridImages[0] blank so that the default image for each tile is nothing
		gridImages[1] = new ImageIcon("Tire.png").getImage();
		TIRE = 1;
		gridImages[2] = new ImageIcon("Mentos.png").getImage();
		MENTOS = 2;
		gridImages[3] = new ImageIcon("Can.png").getImage();
		CAN = 3;
		gridImages[4] = new ImageIcon("Building.png").getImage();
		BUILDING = 4;
		gridImages[5] = new ImageIcon("Crate.png").getImage();
		CRATE = 5;
		border = new ImageIcon("Border.png").getImage();
		SODASPLOSION = 6;

		// Starts a new game and loads up the grid (sets size of grid array)
		newGame();

		// Set the image height and width based on the path image size
		// Also sizes this panel based on the image and grid size
		IMAGE_WIDTH = 64;
		IMAGE_HEIGHT = 64;
		Dimension size = new Dimension(1024, 768);
		this.setPreferredSize(size);

		// Sets up for keyboard input (arrow keys) on this panel
		this.setFocusable(true);
		this.addKeyListener(new KeyHandler());
		this.requestFocusInWindow();
	}

	public void newGame()
	{
		// Initial position of the player
		currentRowOne = 0;
		currentColOne = 0;
		currentRowTwo = 10;
		currentColTwo = 12;

		int noOfRows = 11;
		int noOfColumns = 13;

		// Set up the array
		grid = new int[noOfRows][noOfColumns];

		// Adds the buildings to the grid
		for (int row = 1; row < grid.length; row ++)
			for (int column = 1; column < grid[0].length; column ++)
			{
				if (row % 2 != 0 && column % 2 != 0)
					grid[row][column] = BUILDING;
			}
		
		// Adds the crates to the grid
		for (int row = 0; row < grid.length; row++)
			for (int column = 0; column < grid[0].length; column++)
			{
				if (grid[row][column] != BUILDING  
						 &&  (row > 1 || column > 1)
						 &&  (row < 9 || column < 11))
					grid[row][column] = CRATE;
			}
		
	}

	/**
	 * Repaint the drawing panel
	 * @param g The Graphics context
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(border, 128, 0, this);

		// Redraw the grid with current images
		for (int row = 0; row < grid.length; row++)
			for (int column = 0; column < grid[0].length; column++)
			{
				g.drawImage(gridImages[grid[row][column]], column * IMAGE_WIDTH
						+ 160,
						row * IMAGE_HEIGHT + 32, this);
			}

		// Draw the moving player on top of the grid
		g.drawImage(playerImages[0],
				currentColOne * IMAGE_WIDTH + 160,
				currentRowOne * IMAGE_HEIGHT + 32, this);

		g.drawImage(playerImages[1],
				currentColTwo * IMAGE_WIDTH + 160,
				currentRowTwo * IMAGE_HEIGHT + 32, this);
	}

	// Inner class to handle key events
	private class KeyHandler extends KeyAdapter
	{
		public void keyPressed(KeyEvent event)
		{
			// Change the currentRow and currentColumn of the first player
			// based on the key pressed
			// If the player is trying to move, make sure player cannot move
			// past border or go through an unbreakable block
			if (event.getKeyCode() == KeyEvent.VK_A && currentColOne > 0
					&& grid[currentRowOne][currentColOne - 1] != BUILDING
					&& grid[currentRowOne][currentColOne - 1] != CRATE)
			{
				currentColOne--;
			}
			else if (event.getKeyCode() == KeyEvent.VK_D
					&& currentColOne < grid[0].length - 1
					&& grid[currentRowOne][currentColOne + 1] != BUILDING
					&& grid[currentRowOne][currentColOne + 1] != CRATE)
			{
				currentColOne++;
			}
			else if (event.getKeyCode() == KeyEvent.VK_W && currentRowOne > 0
					&& grid[currentRowOne - 1][currentColOne] != BUILDING
					&& grid[currentRowOne - 1][currentColOne] != CRATE)
			{
				currentRowOne--;
			}
			else if (event.getKeyCode() == KeyEvent.VK_S
					&& currentRowOne < grid.length - 1
					&& grid[currentRowOne + 1][currentColOne] != BUILDING
					&& grid[currentRowOne + 1][currentColOne] != CRATE)
			{
				currentRowOne++;
			}

			// Change the currentRow and currentColumn of the second player
			// based on the key pressed
			// If the player is trying to move, make sure player cannot move
			// past border or go through an unbreakable block
			if (event.getKeyCode() == KeyEvent.VK_LEFT && currentColTwo > 0
					&& grid[currentRowTwo][currentColTwo - 1] != BUILDING
					&& grid[currentRowTwo][currentColTwo - 1] != CRATE)
			{
				currentColTwo--;
			}
			else if (event.getKeyCode() == KeyEvent.VK_RIGHT
					&& currentColTwo < grid[0].length - 1
					&& grid[currentRowTwo][currentColTwo + 1] != BUILDING
					&& grid[currentRowTwo][currentColTwo + 1] != CRATE)
			{
				currentColTwo++;
			}
			else if (event.getKeyCode() == KeyEvent.VK_UP && currentRowTwo > 0
					&& grid[currentRowTwo - 1][currentColTwo] != BUILDING
					&& grid[currentRowTwo - 1][currentColTwo] != CRATE)
			{
				currentRowTwo--;
			}
			else if (event.getKeyCode() == KeyEvent.VK_DOWN
					&& currentRowTwo < grid.length - 1
					&& grid[currentRowTwo + 1][currentColTwo] != BUILDING
					&& grid[currentRowTwo + 1][currentColTwo] != CRATE)
			{
				currentRowTwo++;
			}

			// Repaint the screen after the change
			repaint();
		}
	}
}