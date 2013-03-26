package MainPack;

/*
 * x,y,w,h sind Werte bezogen auf die Position auf dem Tileset-Bild
 * 
 */

public class Tile {

	float x, y, w, h;
	float xnb, ynb, wnb, hnb; // Begehbarkeit nb = nicht begehbar
	boolean teilsUnbegehbar;
	int type;
	
	public Tile(float x, float y, float w, float h, int type){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.type = type;
		teilsUnbegehbar = false;
	}
	
	public Tile(float x, float y, float w, float h, float xnb, float ynb, float wnb, float hnb, int type){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.xnb = xnb;
		this.ynb = ynb;
		this.wnb = wnb;
		this.hnb = hnb;
		this.type = type;
		teilsUnbegehbar = true;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int a){
		this.type = a;
	}
	
	public float getX(){
		return this.x;
	}
	public float getY(){
		return this.y;
	}
	public float getW(){
		return this.w;
	}
	public float getH(){
		return this.h;
	}
}
