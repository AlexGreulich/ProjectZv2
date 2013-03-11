package MainPack;

public class Tile {

	short x; 
	short y;
	short type;
	
	public Tile(short x1, short y1, short type1){
		this.x = x1;
		this.y = y1;
		this.type = type1;
	}
	
	public short getType(){
		return this.type;
	}
	
	public void setType(short a){
		this.type = a;
	}
}
