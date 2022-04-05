package roguelike.entities;

import java.util.Map;

import roguelike.world.World;

public class Item extends Entity {

	private String _benefit;
	private int _count;
	
	public Item(World world, Map<String, String> itemData, int x, int y) {
		super(world, itemData, x, y);
        _benefit = itemData.get("benefit");
		_count = Integer.parseInt(itemData.get("count"));
	}
	
	public String getBenefit() {
		return (this._benefit);
	}

	public int getCount() {
		return (this._count);
	}

	public void use( Creature creature )
	{
		this.remove();
    	if ("health".equals(this._benefit) && creature.getHitpoints() <= 90) {
    		creature.increaseHitpoints(this._count);
    	}
		else if ("attack".equals(this._benefit)) {
    		creature.increaseAttack(this._count);
    	}
		else if ("defense".equals(this._benefit)) {
    		creature.increaseDefense(this._count);
    	}
		else {
			System.out.println( "Unknown effect: " + this._count);
		}

	}

	public String toString()
	{
		return(super.toString() + " benefit: " + this._benefit + " count: " + this._count);
	}
}
