package roguelike.world;

import roguelike.Constants;
import roguelike.entities.Creature;
import roguelike.entities.Item;
import roguelike.entities.Tile;

import java.util.*;

public class WorldBuilder {
	private int width;
	private int height;
	private Tile[][] tiles;
	private Map<Integer, Map<String, String>> tileData;
	private Map<Integer, Map<String, String>> creatureData;
	private Map<Integer, Map<String, String>> itemData;

	private int nrOfCreatures = 0;
	private int nrOfItems = 0;

	public WorldBuilder(Map<Integer, Map<String, String>> tileData, Map<Integer, Map<String, String>> creatureData, Map<Integer, Map<String, String>> itemData,int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
		this.tileData = tileData;
		this.creatureData = creatureData;
		this.itemData = itemData;
	}

	public WorldBuilder load(String file) {
		// Loads map from file
		return this;
	}
	
	private Tile createTile(int id, int x, int y) {
		return new Tile(tileData.get(id), x, y);
	}
	
	private Creature createCreature(World world, int id, int x, int y) {
		Item[] treasure = randomItems(world, 4);
		return new Creature(world, creatureData.get(id), x, y, treasure);
	}

	private Item createItem(World world, int id, int x, int y) {
		return new Item(world, itemData.get(id), x, y);
	}
	
	public WorldBuilder fill(int tileID) {
		for (int x=0; x < width; x++) {
			for (int y=0; y < height; y++) {
				tiles[x][y] = new Tile(tileData.get(tileID), x, y);
			}
		}
		return this;
	}
	
	public WorldBuilder addBorders() {
		for (int x=0; x<width; x++) {
			tiles[x][0] = createTile(Constants.WALL_ID, x, 0);
			tiles[x][height-1] = createTile(Constants.WALL_ID, x, height-1);
		}
		
		for (int y=0; y<height; y++) {
			tiles[0][y] = createTile(Constants.WALL_ID, 0, y);
			tiles[width-1][y] = createTile(Constants.WALL_ID, width-1, y);
		}
		return this;
	}
	
	public WorldBuilder carveOutRoom(int topX, int topY, int width, int height) {
		for (int x=topX; x < topX+width; x++) {
			for (int y=topY; y < topY+height; y++) {
				tiles[x][y] = createTile(Constants.GROUND_ID, x, y);
			}
		}
		return this;
	}
	
	public WorldBuilder populateWorld(int nrOfCreatures) {
		this.nrOfCreatures = nrOfCreatures;
		return this;
	}

	public WorldBuilder createRandomItems(int nrOfItems) {
		this.nrOfItems = nrOfItems;
		return this;
	}
	
	public WorldBuilder createRandomWalkCave(int seed, int startX, int startY, int length) {
		Random rnd = new Random(seed);
		int direction;
		int x = startX;
		int y = startY;
		
		for (int i=0; i<length; i++) {
			direction = rnd.nextInt(4);
			if (direction == 0 && (x+1) < (width-1)) {
				x += 1;
			} else if (direction == 1 && (x-1) > 0) {
				x -= 1;
			} else if (direction == 2 && (y+1) < (height-1)) {
				y += 1;
			} else if (direction == 3 && (y-1) > 0) {
				y -= 1;
			}
			
			tiles[x][y] = createTile(Constants.GROUND_ID, x, y);
		}

		return this;
	}


	private Item[] randomItems(World world, int nrOfItems)
	{
		Item[] items = new Item[nrOfItems];
		Random rnd = new Random();
		for (int i=0; i < nrOfItems; i++) {
			
			List<Integer> itemIDs = new ArrayList<>(itemData.keySet());
			int itemID = itemIDs.get(rnd.nextInt(itemIDs.size()));
			
			items[i] = createItem(world, itemID, 0, 0);
			
		}
		
		return( items );
	}
	
	public World build() {

		World world = new World(tiles);

		if( this.nrOfCreatures > 0 )
		{
			Random rnd = new Random();
			int rndX;
			int rndY;
			
			for (int i=0; i < nrOfCreatures; i++) {
				
				do {
					rndX = rnd.nextInt(width);
					rndY = rnd.nextInt(height);
				} while (tiles[rndX][rndY].isBlocked());
				
				List<Integer> creatureIDs = new ArrayList<>(creatureData.keySet());
				creatureIDs.remove(Constants.PLAYER_ID);
				int creatureID = creatureIDs.get(rnd.nextInt(creatureIDs.size()));
				
				world.addEntity(createCreature(world, creatureID, rndX, rndY));
				
			}
		}	

		if( this.nrOfItems > 0 )
		{
			Item[] items = randomItems(world, nrOfItems);
			for (Item item : items) {
				world.addEntity(item);
			}
		}

		return (world);
	}

}