package MainPack;

import MainPack.World.ItemType;

public class Item {

	private ItemType itemType;
	String name;
	byte dx = 0, dy = 0;
	short x = 0, y = 0;
	
	public Item(short x, short y, byte dx, byte dy, ItemType type, String name){
		this.x = x;//...
		this.y = y;//...
		this.dx = dx;
		this.dy = dy;
		this.itemType = type;
		this.name = name;
	}
	
	public void use(){
		switch(itemType){
		case TOOL:
			break;
		case MEDIC:
			break;
		case WEAPON:
			break;
		case COLLECTABLE:
			break;
		}
	}
}
