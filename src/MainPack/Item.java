package MainPack;

import org.jdom2.Element;

import MainPack.World.ItemType;


public class Item{

	ItemHandler ih;
	public ItemType itemType;
	String name;
	int dx = 0, dy = 0;
	short x = 0, y = 0;
	float texPosX =0f, texPosY =0f;
	int type;
	boolean isLimited;
	int uses;
	
	public Item( short x, short y, int dx, int dy, int id, boolean limit){
		
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.type = id;
		this.isLimited = limit;
	}
	public short getX() {
		return x;
	}
	public short getY() {
		return y;
	}
	public int getDX() {
		return dy;
	}
	public int getDY() {
		return dy;
	}
	public int getID(){
		return this.type;
	}
	public void setUses(int u){
		this.uses =u;
	}
}
