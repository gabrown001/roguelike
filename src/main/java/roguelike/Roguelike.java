package roguelike;

import roguelike.entities.Creature;
import roguelike.ui.Interface;
import roguelike.world.World;
import roguelike.world.WorldBuilder;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Roguelike {

	private boolean isRunning;
	private int framesPerSecond = 60;
	private int timePerLoop = 1000000000 / framesPerSecond;
	
	private World world;
	private Creature player;
	
	private Map<Integer,Map<String, String>> creatureData;
	private Map<Integer,Map<String, String>> tileData;
	private Map<Integer,Map<String, String>> itemData;
	
	private int screenWidth;
	private int screenHeight;
	
	private Rectangle gameViewArea;
	
	private static final int mapWidth = 100;
	private static final int mapHeight = 100;
	
	private Interface ui;
	
	public Roguelike(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		gameViewArea = new Rectangle(screenWidth, screenHeight-5);
		
		ui = new Interface(screenWidth, screenHeight, new Rectangle(mapWidth, mapHeight));

		creatureData = loadData(Paths.get(Constants.ROGUELIKE, Constants.CREATURES_FILE).toString());
		tileData = loadData(Paths.get(Constants.ROGUELIKE, Constants.TILES_FILE).toString());
		itemData = loadData(Paths.get(Constants.ROGUELIKE,  Constants.ITEMS_FILE).toString());
		
		createWorld();
	}
	
	public Map<Integer, Map<String, String>> loadData(String fileName) {

		InputStream inputStream = Roguelike.class.getClassLoader().getResourceAsStream(fileName);

		Reader targetReader = new InputStreamReader(inputStream);
		Map<Integer, Map<String, String>> entityMap = new HashMap<>();
		String line = "";
		String[] attributeNames = new String[10];
		
		try (BufferedReader br = new BufferedReader(targetReader)) {

			line = br.readLine();
			
			if (line != null) {
				attributeNames = line.split(", ");
			}
			
			while ((line = br.readLine()) != null) {
				String[] data = line.split(", ");
				Map<String, String> entityData = new HashMap<>();
				
				for (int i=0; i<attributeNames.length; i++) {
					entityData.put(attributeNames[i], data[i]);
				}
				
				int id = Integer.parseInt(data[0]);
				entityMap.put(id, entityData);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entityMap;

	}
	
	private void createWorld(){
		world = new WorldBuilder(tileData, creatureData, itemData, mapWidth, mapHeight)
				    .fill(Constants.WALL_ID)
				    .createRandomWalkCave(12232, 10, 10, 10000)
				    .populateWorld(20)
					.createRandomItems(20)
					.build();
		player = new Creature(world, creatureData.get(Constants.PLAYER_ID), 10, 10, null);
		world.player = player;
		world.addEntity(player);
		world.setUI(ui);
	}
	
	public void processInput() {
	    InputEvent event = ui.getNextInput();
	    if (event instanceof KeyEvent) {
	    	KeyEvent keypress = (KeyEvent)event;
	    	switch (keypress.getKeyCode()){
				case KeyEvent.VK_LEFT: 
					player.move(-1, 0); 
					break;
				case KeyEvent.VK_RIGHT: 
					player.move(1, 0); break;
				case KeyEvent.VK_UP: 
					player.move(0, -1); 
					break;
				case KeyEvent.VK_DOWN: 
					player.move(0, 1); 
					break;
			}
	    } else if (event instanceof MouseEvent) {
	    	//
	    }
	}
	
	public void render(){
		ui.pointCameraAt(world, player.getX(), player.getY());
		ui.drawDynamicLegend(gameViewArea, world);
		ui.refresh();
	}
	
	public void update() {
		world.update();
	}
	
	public void run() {
		isRunning = true;

		while(isRunning) {
			long startTime = System.nanoTime();
			
			processInput();
			update();
			render();
			
			long endTime = System.nanoTime();
			
			long sleepTime = timePerLoop - (endTime-startTime);
			
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime/1000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Roguelike game = new Roguelike(80, 24);
		game.run();
	}
	
	public static Color stringToColor(String colorString) {
		Color color;
		try {
		    Field field = Color.class.getField(colorString);
		    color = (Color)field.get(null);
		} catch (Exception e) {
		    color = null; // Not defined
		}
		return color;
	}

}
