package roguelike.entities;

import roguelike.Constants;
import roguelike.Inventory;
import roguelike.world.World;

import java.util.Map;
import java.util.Random;

public class Creature extends Entity {

	private int attack;
	private int defense;
    private int hitpoints;
    private String behaviour;
	private String _type;
	private int _move;

	private Item _weapon;
	private Item _shield;
	private Item _armour;

	private Inventory _inventory;


	// creatures potentially drop items.
    
    public Creature(World world, Map<String, String> creatureData, int x, int y, Item[] treasure)
    {
        super(world, creatureData, x, y);

		this._inventory = new Inventory(10);

		attack = Integer.parseInt(creatureData.get("attack"));
		defense = Integer.parseInt(creatureData.get("defense"));
		hitpoints = Integer.parseInt(creatureData.get("hitpoints"));
        behaviour = creatureData.get("behaviour");
		this._type = creatureData.get("type");
		this._move = Integer.parseInt(creatureData.get("move"));
    }

	public void dies()
	{
		if( Constants.PLAYER_TYPE.equals( this._type) ) { 
			this._world.displayMessage("The player has died");
		}
		else {
			super.remove();
			for (Item item : this._inventory) {
				this.dropItem(item);
			}
		}	
	}
    
	private int getAttack() {
    	return attack;
    }

	protected void increaseAttack(int amount) {
		//System.out.println(this + " attack is increased by " + amount);
		attack += amount;
	}

	private int getDefense() {
    	return defense;
    }

	protected void increaseDefense(int amount) {
		//System.out.println(this + " defense is increased by " + amount);
		defense += amount;
	}

    protected int getHitpoints() {
    	return hitpoints;
    }
    
    private void reduceHitpoints(int amount) {
		//System.out.println(this + " takes " + amount + " damage");
		hitpoints -= amount;
	}

	protected void increaseHitpoints(int amount) {
		//System.out.println(this + " heals " + amount + " health");
		hitpoints += amount;
	}
 
    public void move(int dx, int dy)
    {
        if (!_world.isBlocked(x + dx, y + dy))
        {
            x += dx;
            y += dy;

        } else {
			Entity entity = _world.getEntityAt(x + dx, y + dy);
			System.out.println(this + " found during move: " + ((entity != null) ? entity : "none"));
			if (entity instanceof Item) {
				useItem((Item) entity);
			} else if (entity instanceof Creature) {
				attackCreature((Creature) entity);
			}
        }
    }
    
    public void useItem(Item item) {
		this._world.displayMessage( "The player is using " + item);
		item.remove();
    	if ("health".equals(item.getBenefit()) && this.getHitpoints() <= 90) {
    		this.increaseHitpoints(item.getCount());
    	}
		else if ("attack".equals(item.getBenefit())) {
    		this.increaseAttack(item.getCount());
    	}
		else if ("defense".equals(item.getBenefit())) {
    		this.increaseDefense(item.getCount());
    	}
		// else {
		// 	System.out.println( "Unknown effect: " + this._count);
		// }
    }

	// move to creature
	public void dropItem(Item item)
	{
		System.out.println( this.getName() + " is dropping " + item.getName() );
		World world = super._world;
		int width = world.getWidth();
		int height = world.getHeight();

		int x = this.getX();
		int y = this.getY();

		Random rnd = new Random(123123133);
		do 
		{
			int direction = rnd.nextInt(4);
			if (direction == 0 && (x+1) < (width-1)) {
				x += 1;
			} else if (direction == 1 && (x-1) > 0) {
				x -= 1;
			} else if (direction == 2 && (y+1) < (height-1)) {
				y += 1;
			} else if (direction == 3 && (y-1) > 0) {
				y -= 1;
			}
		} while( world.isBlocked(x, y));

		super.x = x;
		super.y = y;
		world.addEntity(item);
		//System.out.println( this.getName() + " is dropping " + item.getName() );
	}
    
    public void attackCreature(Creature creature) {

		// System.out.println( this + " is considering attacking " + creature );
		// if( this.getType().equals(Constants.PLAYER_TYPE) && !creature.getType().equals(Constants.PLAYER_TYPE))
		// {
			this._world.displayMessage(this + " attacks " + creature);
			if(this.getAttack() > creature.getDefense())
			{
				creature.reduceHitpoints(this.getAttack() - creature.getDefense() );
				if( creature.getHitpoints() <=0 )
				{
					creature.dies();
				}
			}
			else
			{
				// weapon/armour wear... maybe physicall damage?
			}
		// }
		// else if( !this.getType().equals(Constants.PLAYER_TYPE) && creature.getType().equals(Constants.PLAYER_TYPE))
		// {
		// 	System.out.println("Player takes damage from " + this);
		// }

    }

	public void moveTo(int dx, int dy)
    {
		x = dx;
		y = dy;

		System.out.println(this.getType() + " is moved to " + x + ":" + y);
    }

    public void update()
    {
		for( int i=0; i < this._move; ++i)
		{
			Random rnd = new Random();
			int performAction = rnd.nextInt(100);
			if (behaviour.equals("docile") && performAction > 98) {
				// walk around and flee if attacked
				
				int rndNr = rnd.nextInt(3);
				
				if (rndNr == 0) {
					move(1, 0);
				} else if (rndNr == 1) {
					move(-1, 0);
				} else if (rndNr == 2) {
					move(0, 1);
				} else if (rndNr == 3) {
					move(0, -1);
				}

			} else if (behaviour.equals("aggressive") && performAction > 98) {
				// look for other npcs and hunt them

				// temp - just to have movement.
				int rndNr = rnd.nextInt(3);
							
				if (rndNr == 0) {
					move(1, 0);
				} else if (rndNr == 1) {
					move(-1, 0);
				} else if (rndNr == 2) {
					move(0, 1);
				} else if (rndNr == 3) {
					move(0, -1);
				}
			}
		}
    }

	public String toString()
	{
		return(super.toString() + " behavior: " + behaviour + " attack: " + attack + " defense: " + defense + " hitpoints: "+ hitpoints);
	}
}
