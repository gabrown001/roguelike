package roguelike.entities;

import roguelike.Roguelike;
import roguelike.world.World;

import java.awt.*;
import java.util.Map;

public class Entity {

	protected World _world;

	protected int x;
	protected int y;
	
	protected int _id;
	protected String _name;
	protected String type;
	protected char glyph;
	protected Color color;
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getID() {return this._id;}
    public char getGlyph() {return this.glyph;}
	public String getName() {return this._name;}
    public String getType() {return this.type;}
    public Color getColor() {return this.color;}
    
    public Entity(World world, Map<String, String> entityData, int xPos, int yPos) {
		this._world = world;
    	x = xPos;
    	y = yPos;
		
		this._id = Integer.parseInt(entityData.get("id"));
		this._name = entityData.get("name");
		this.type = entityData.get("type");
        this.glyph = entityData.get("glyph").charAt(0);
		this.color = Roguelike.stringToColor(entityData.get("color"));
    }

	public void remove()
	{
		if( this._world != null )
		{
	 		this._world.removeEntity(this);
		}
	}

	public String toString()
	{
		return(this.getName() +  " of type " + this.getType() + " at " + x + ":" + y);
	}

	public boolean equals(Object object)
	{
		boolean retVal = false;
		if (this == object )
		{
			retVal = true;
		}
		else
		{
			if( object instanceof Entity )
			{
				retVal = ( this.hashCode() == object.hashCode());
			}
			else
			{
				retVal = false;
			}
		}
		return( retVal );
	}

    @Override
    public int hashCode() {
        return getID() + (getName() != null ? getName().hashCode() : 0);
	}
}
