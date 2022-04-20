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

	public String toString()
	{
		return(super.toString() + " benefit: " + this._benefit + " count: " + this._count);
	}
}
