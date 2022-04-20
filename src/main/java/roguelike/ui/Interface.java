package roguelike.ui;

import asciiPanel.AsciiPanel;
import roguelike.entities.Creature;
import roguelike.entities.Item;
import roguelike.entities.Tile;
import roguelike.graphics.AsciiCamera;
import roguelike.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Interface extends JFrame implements KeyListener, MouseListener {

	private static final long serialVersionUID = 6408617006915516474L;
	
	private AsciiPanel terminal;
	private AsciiCamera camera;
	private Queue<InputEvent> inputQueue;
	
	private int screenWidth;
	private int screenHeight;

	// temp to 
	Rectangle _gameViewArea;

	
    public Interface(int screenWidth, int screenHeight, Rectangle mapDimensions) {
    	super("Roguelike");
    	
    	this.screenWidth = screenWidth;
    	this.screenHeight = screenHeight;
    	inputQueue = new LinkedList<>();

    	Rectangle gameViewArea = new Rectangle(screenWidth, screenHeight-5);
    	terminal = new AsciiPanel(screenWidth, screenHeight);
    	camera = new AsciiCamera(mapDimensions, gameViewArea);
    	
        super.add(terminal);
        super.addKeyListener(this);
        super.addMouseListener(this);
        super.setSize(screenWidth*9, screenHeight*16);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.repaint();
    }
    
    // public AsciiPanel getTerminal() {
    // 	return terminal;
    // }
    
    public InputEvent getNextInput() {
    	return inputQueue.poll();
    }
    
    public void pointCameraAt(World world, int xfocus, int yfocus) {
    	camera.lookAt(terminal, world, xfocus, yfocus);
    }
    
    public void refresh() {
    	terminal.repaint();
    }
    
	@Override
	public void keyPressed(KeyEvent e) {
		inputQueue.add(e);
	}

	public void displayMessage( String message )
	{
		int x = 5;
		int y = this._gameViewArea.height;
		//System.out.println(this._gameViewArea);

		//terminal.write(message, x, y);
		terminal.write("test");
	}

	public void drawDynamicLegend(Rectangle gameViewArea, World world) {
		int x = 5;
		int y = gameViewArea.height;

		//System.out.println( "drawDynamicLegend: " + gameViewArea);

		char glyph;

		this._gameViewArea = gameViewArea;
		
		// for (Tile tile : world.getTileInArea(gameViewArea)) {
		// 	glyph = tile.getGlyph();
		// 	terminal.write(glyph + " " + tile.getName(), x, y);
		// 	y += 1;
			
		// 	if (y == gameViewArea.height+2) {
		// 		x += 15;
		// 		y = gameViewArea.height;
		// 	}
		// }
		
		// for (Creature creature : world.getCreaturesInArea(gameViewArea)) {
		// 	glyph = creature.getGlyph();
		// 	terminal.write(glyph + "   " + creature.getName(), x, y);
		// 	y += 1;
			
		// 	if (y == gameViewArea.height+5) {
		// 		x += 15;
		// 		y = gameViewArea.height;
		// 	}
		// }

		// for (Item item : world.getItemsInArea(gameViewArea)) {
		// 	glyph = item.getGlyph();
		// 	System.out.println("printing: " + glyph + " " + item.getName());
		// 	//terminal.write(glyph + "   " + item.getName(), x, y);
		// 	y += 1;
			
		// 	if (y == gameViewArea.height+5) {
		// 		x += 15;
		// 		y = gameViewArea.height;
		// 	}
		// }
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		inputQueue.add(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}
