package roguelike.world;

import roguelike.Constants;
import roguelike.entities.Creature;
import roguelike.entities.Entity;
import roguelike.entities.Item;
import roguelike.entities.Tile;
import roguelike.ui.Interface;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class World {
	
	//TODO:  Add layers - depth
	private Tile[][] _tiles;

	private int width;
	private int height;
	public Creature player;
	public Set<Creature> _creatures;
	public Set<Item> _items;
	public Interface _ui;
	
	public int width() { return width; }
	public int height() { return height; }
	public void setUI(Interface ui) { _ui = ui;};
	public void displayMessage(String message) {_ui.displayMessage(message);}
	
	public World(Tile[][] tiles){
		this(tiles, null, null);
	}
	
	public World(Tile[][] tiles, Set<Creature> creatures, Set<Item> items){
		this._creatures = new HashSet<>();

		if( creatures != null ) 
		{
			this._creatures.addAll(creatures);
		}

		this._items = new HashSet<>();
		if( items != null ) 
		{
			this._items.addAll(items);
		}

		this._tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
	}

	public int getWidth() { return( this.width ); }
	public int getHeight() { return( this.height ); }

    public Set<Creature> getCreatures() {
		return this._creatures;
	}

	public Set<Item> getItems() {
		return this._items;
	}
	
	public void addEntity(Entity entity) {
		if (entity instanceof Item) {
			this._items.add((Item)entity);
		} else if (entity instanceof Creature) {
			this._creatures.add((Creature)entity);
		}
	}

	public void removeEntity(Entity entity) {
		if (entity instanceof Item) {
			this._items.remove(entity);
		} else if (entity instanceof Creature) {
			this._creatures.remove(entity);
		}
	}
	
	public Tile tile(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return null;
		else
			return _tiles[x][y];
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
		Entity entity = this._creatures.stream()
				.filter(creature -> creature.getX() == x && creature.getY() == y)
				.findFirst()
				.orElse(null);

		if( entity == null )
		{
				entity = this._items.stream()
				.filter(item -> item.getX() == x && item.getY() == y)
				.findFirst()
				.orElse(null);
		}
		return( entity );
	}
	
	public boolean isBlocked(int x, int y) {
		return (_tiles[x][y].isBlocked() || getEntityAt(x, y) != null);
	}
	
	public void update() {
		this._creatures.stream()
		.filter(creature -> creature.getID() != Constants.PLAYER_ID)
		.forEach(creature -> creature.update());
	}
	
	public Set<Tile> getTileInArea(Rectangle rectangle) {
		Set<Tile> tiles = new HashSet<>();
		Tile tile;
		
		for (int y=(int) rectangle.getY(); y < rectangle.getMaxY(); y += 1) {
			for (int x=(int) rectangle.getX(); x < rectangle.getMaxX(); x += 1) {
				tile = this._tiles[x][y];
				if (tile != null) {
					//hashCode/equals used to ensure singleton added.
					tiles.add(tile);
				}
			}
		}
		return tiles;
	}
	
	public Set<Creature> getCreaturesInArea(Rectangle rectangle) {
		Set<Creature> creatures = new HashSet<>();
		
		creatures.add(player);
		
		for (Creature creature : this._creatures) {
			if (creature.getX() > rectangle.getX() && creature.getX() < rectangle.getMaxX() &&
					creature.getY() > rectangle.getY() && creature.getY() < rectangle.getMaxY()) {
				creatures.add(creature);
			}
		}
		
		return creatures;
	}

	public Set<Item> getItemsInArea(Rectangle rectangle) {
		Set<Item> items = new HashSet<>();
		
		for (Item item : this._items) {
			if (item.getX() > rectangle.getX() && item.getX() < rectangle.getMaxX() &&
			item.getY() > rectangle.getY() && item.getY() < rectangle.getMaxY()) {
				items.add(item);
			}
		}
		return items;
	}

}
