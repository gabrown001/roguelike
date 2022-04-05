package roguelike.entities;

import roguelike.Constants;
import roguelike.world.World;

import java.util.Map;
import java.util.Random;

public class Creature extends Entity {

	private int attack;
	private int defense;
    private int hitpoints;
    private String behaviour;

	private Item _weapon;
	private Item _shield;
	private Item _armour;

	// creatures potentially drop items.
    
    public Creature(World world, Map<String, String> creatureData, int x, int y)
    {
        super(world, creatureData, x, y);
		attack = Integer.parseInt(creatureData.get("attack"));
		defense = Integer.parseInt(creatureData.get("defense"));
		hitpoints = Integer.parseInt(creatureData.get("hitpoints"));
        behaviour = creatureData.get("behaviour");
    }
    
	private int getAttack() {
    	return attack;
    }

	protected void increaseAttack(int amount) {
		System.out.println(this + " attack is increased by " + amount);
		attack += amount;
	}

	private int getDefense() {
    	return defense;
    }

	protected void increaseDefense(int amount) {
		System.out.println(this + " defense is increased by " + amount);
		defense += amount;
	}

    protected int getHitpoints() {
    	return hitpoints;
    }
    
    private void reduceHitpoints(int amount) {
		System.out.println(this + " takes " + amount + " damage");
		hitpoints -= amount;
	}

	protected void increaseHitpoints(int amount) {
		System.out.println(this + " heals " + amount + " health");
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
			
			if (entity instanceof Item) {
				useItem((Item) entity);
			} else if (entity instanceof Creature) {
				attackCreature((Creature) entity);
			}
        }
    }
    
    public void useItem(Item item) {
		System.out.println(this + " is using item " + item);
		item.use(this);
    }
    
    public void attackCreature(Creature creature) {

		if( this.getType().equals(Constants.PLAYER) && !creature.getType().equals(Constants.PLAYER))
		{
			System.out.println(this + " attacks " + creature);
			if(this.getAttack() > creature.getDefense())
			{
				creature.reduceHitpoints(this.getAttack() - creature.getDefense() );
				if( creature.getHitpoints() <=0 )
				{
					creature.remove();
				}
			}
			else
			{
				// weapon/armour wear... maybe physicall damage?
			}
		}
		else if( !this.getType().equals(Constants.PLAYER) && creature.getType().equals(Constants.PLAYER))
		{
			System.out.println("Player takes damage from " + this);
		}

    }

	public void moveTo(int dx, int dy)
    {
		x = dx;
		y = dy;

		System.out.println(this.getType() + " is moved to " + x + ":" + y);
    }

    public void update()
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

	public String toString()
	{
		return(super.toString() + " behavior: " + behaviour + " attack: " + attack + " defense: " + defense + " hitpoints: "+ hitpoints);
	}
}
