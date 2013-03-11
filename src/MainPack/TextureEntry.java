package MainPack;

public class TextureEntry {

	float x,y,w,h;
	
	public TextureEntry(float xa,float ya, float wa, float ha){
		this.x = xa;
		this.y = ya;
		this.w = wa;
		this.h = ha;
		
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
