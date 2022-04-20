package roguelike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import roguelike.entities.Item;

public class Inventory implements Iterable<Item> {
    
    int _size;
	List<Item> _items;

    public Inventory(int size)
    {
        this._items = new ArrayList<>(size);
        this._size = size;
    }

    public boolean isEmpty()
    {
        return( this._items.size() == 0 );
    }

    public boolean isFull()
    {
        return( this._items.size() == this._size );
    }

    public int size()
    {
        return( this._items.size());
    }

    public int maxSize()
    {
        return( this._size );
    }

    // public Item findItemBy(int id)
    // {
    //     return(null);
    // }

    public void add( Item item )
    {
        this._items.add(item);
    }

    public void addItems( Item[] items )
    {
        this._items  = (items != null ? new ArrayList<>(Arrays.asList(items)) : new ArrayList<>());
    }

    public void remove( Item item )
    {
        this._items.remove(item);
    }

    @Override
    public Iterator<Item> iterator() {
        return (this._items.iterator());
    }

}
