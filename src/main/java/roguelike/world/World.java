package roguelike.world;

import roguelike.Constants;
import roguelike.entities.Creature;
import roguelike.entities.Entity;
import roguelike.entities.Item;
import roguelike.entities.Tile;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class World {
	
	//TODO:  Add layers - depth
	private Tile[][] tiles;



	private int width;
	private int height;
	public Creature player;
	public Set<Creature> creatures;
	public Set<Item> items;
	
	public int width() { return width; }
	public int height() { return height; }
	
	public World(Tile[][] tiles){
		this(tiles, null, null);
	}
	
	public World(Tile[][] tiles, Set<Creature> creatures, Set<Item> items){
		this.creatures = new HashSet<>();

		if( creatures != null ) 
		{
			this.creatures.addAll(creatures);
		}

		this.items = new HashSet<>();
		if( items != null ) 
		{
			this.items.addAll(items);
		}

		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
	}
	
	public void addEntity(Entity entity) {
		if (entity instanceof Item) {
			this.items.add((Item)entity);
		} else if (entity instanceof Creature) {
			this.creatures.add((Creature)entity);
		}
	}

	public void removeEntity(Entity entity) {
		if (entity instanceof Item) {
			this.items.remove(entity);
		} else if (entity instanceof Creature) {
			this.creatures.remove(entity);
		}
	}
	
	public Tile tile(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		else
			return tiles[x][y];
	}
	
	public char glyph(int x, int y){
		return tile(x, y).getGlyph();
	}
	
	public Color glyphColor(int x, int y){
		return tile(x, y).getColor();
	}	
	
	public Color backgroundColor(int x, int y){
		return tile(x, y).getBackgroundColor();
	}
	
	public Entity getEntityAt(int x, int y) {
		Entity entity = creatures.stream()
				.filter(creature -> creature.getX() == x && creature.getY() == y)
				.findFirst()
				.orElse(null);

		if( entity == null )
		{
				entity = items.stream()
				.filter(item -> item.getX() == x && item.getY() == y)
				.findFirst()
				.orElse(null);
		}
		return( entity );
	}
	
	public boolean isBlocked(int x, int y) {
		return (tiles[x][y].isBlocked() || getEntityAt(x, y) != null);
	}
	
	public void update() {
		creatures.stream()
		.filter(creature -> creature.getType() != Constants.PLAYER)
		.forEach(creature -> creature.update());
	}
	
	public Set<String> getTileTypesInArea(Rectangle rectangle) {
		Set<String> tileTypes = new HashSet<String>();
		Tile tile;
		
		for (int y=(int) rectangle.getY(); y < rectangle.getMaxY(); y += 1) {
			for (int x=(int) rectangle.getX(); x < rectangle.getMaxX(); x += 1) {
				tile = this.tiles[x][y];
				if (tile != null) {
					tileTypes.add(tile.getType());
				}
			}
		}
		return tileTypes;
	}
	
	public Set<String> getCreatureTypesInArea(Rectangle rectangle) {
		Set<String> creatureTypes = new HashSet<>();
		
		creatureTypes.add(player.getType());
		
		for (Creature creature : this.creatures) {
			if (creature.getX() > rectangle.getX() && creature.getX() < rectangle.getMaxX() &&
					creature.getY() > rectangle.getY() && creature.getY() < rectangle.getMaxY()) {
				creatureTypes.add(creature.getType());
			}
		}
		
		return creatureTypes;
	}

	public Set<String> getItemTypesInArea(Rectangle rectangle) {
		Set<String> itemTypes = new HashSet<String>();
		Tile tile;
		
		for (Item item : this.items) {
			if (item.getX() > rectangle.getX() && item.getX() < rectangle.getMaxX() &&
			item.getY() > rectangle.getY() && item.getY() < rectangle.getMaxY()) {
				itemTypes.add(item.getType());
			}
		}
		return itemTypes;
	}

		
	public Item getItem() {
		items.stream()
		.filter(item -> item.getType()Constants.PLAYER)
		.forEach(creature -> creature.update());
	}
}
