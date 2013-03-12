package MainPack;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import Entities.Player;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTranslatef;

/*
* playerdeltax/y : Position des Spielers relativ zum Tile, für die Verschiebung via Translationsmatrix
* cornerx/y : Die linke obere Ecke des zu Zeichnenden Feldes, ist anhängig von der Position des Spielers
*/

public class Level {
	
	public Texture tilesetTexture = null;
	int tilesetW = 0;
	int tilesetH = 0;
	float texH = 0f, texW = 0f;
	private Map<Integer,TextureEntry> textureEntryMap = null;
	private int playerdeltax = 0, playerdeltay = 0;
	float percentage = 32f/1024f;
	int uebergangx = 32, uebergangy = 32;
	Tile[][] tilegrid = new Tile[World.WORLDSIZE][World.WORLDSIZE];
	
	
	public Level(){
		//	Lade das tileset als eine große Textur
		try {
			tilesetTexture = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/Tileset_neu_32-1024.png"));
		
			texW = tilesetTexture.getTextureWidth()/32f;
			texH = tilesetTexture.getTextureHeight()/32f;
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		//Jetzt die Koordinaten der Einzeltiles aus der Textur holen
		textureEntryMap = createCoordMapFromTexture(tilesetTexture);
		createFinalMap();
		calculateTileBorders();
	}
	
	
	public void draw(Player player){
		int cornerx = 0;
		if (player.screenx == (World.TILES_ON_SCREEN_WIDTH*32 / 2)){
			cornerx = (int) (player.getX()/32 - World.TILES_ON_SCREEN_WIDTH/2);
		}
		
		int cornery = 0;
		if (player.screeny == (World.TILES_ON_SCREEN_HEIGHT*32 / 2)){
			cornery = (int) (player.getY()/32 - World.TILES_ON_SCREEN_HEIGHT/2);
		} 	
		
		glBindTexture(GL_TEXTURE_2D, tilesetTexture.getTextureID());
		
		playerdeltax = (int) (player.getX()%32);
		playerdeltay = (int) (player.getY()%32);
		if (cornerx == 0){
			if (player.screenx > (320-32)){
				playerdeltax = (int) ((int) 32-(World.TILE_MIDDLE_X*32 - player.screenx));
			} else {
				playerdeltax = 0;
			}
			
		}
		if (cornery == 0){
			playerdeltay = 0;
		}
		glTranslatef((float)-playerdeltax, (float)-playerdeltay, 0f);
		
		
		System.out.println("player: "+player.getX()+"|"+player.getY()+" ;  screenx/y: "+(int)player.screenx+"|"+(int)player.screeny+" ;  playerdeltax/y: "+playerdeltax+"|"+playerdeltay+" ;  cornerx/y: "+cornerx+"|"+cornery );
		
		glBegin(GL_QUADS);
		
		for(int a = 0; a < World.TILES_ON_SCREEN_WIDTH+1; a++){
			for(int b = 0; b < World.TILES_ON_SCREEN_HEIGHT+1; b++){
								
				TextureEntry te = textureEntryMap.get((int)tilegrid[cornerx+a][cornery+b].getType());
				float u = te.getX()/texW * percentage;//
				float v = te.getY()/texW * percentage;//
				float u2 = (te.getX()+texW)/texW * percentage;//
				float v2 = (te.getY()+texW)/texW * percentage;//
				
			//	Texturkoord.		..	an Bildschirmausschnitt
				glTexCoord2f(u,v);		glVertex2f(a*texW,b*texW);		
				glTexCoord2f(u2,v);		glVertex2f(a*texW+texW,b*texW);		
				glTexCoord2f(u2,v2);	glVertex2f(a*texW+texW,b*texW+texW);	
				glTexCoord2f(u,v2);		glVertex2f(a*texW,b*texW+texW);
			}
		}
		glEnd();
		glLoadIdentity();
	}
	
	
	private void createFinalMap(){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource("/karten/grossekarte.gif"));
			Color c = null;
			for(short x = 0; x < map.getWidth();x++){
				for(short y = 0; y < map.getHeight();y++){
					short n = 0;
					c = new Color(map.getRGB(x, y));
							if(c.equals(new Color(100,200,100))){
								n=19;
							}
					if(c.getRed() == 255){
						if(c.getGreen() == 255){
							//sand
							// grid[x][y] = 0;	//da sand, sind alle felder begehbar
							
							if(c.getBlue()==0)		 {			//vorher :  {tileArray[x][y] = new Tile(tileset.getTileImage(201));
							n=33;
							}else if(c.getBlue()==10){//oben
							n=1;
							}else if(c.getBlue()==20){
							n=65;
							}else if(c.getBlue()==30){//links
							n=32;
							}else if(c.getBlue()==40){
							n=34;
							}else if(c.getBlue()==50){//lioben
							n=0;
							}else if(c.getBlue()==60){
							n=64;
							}else if(c.getBlue()==70){//reoben
							n=2;
							}else if(c.getBlue()==80){
							n=66;
							}else if(c.getBlue()==90){//gras_lioben
							n=97;
							}else if(c.getBlue()==100){
							n=129;
							}else if(c.getBlue()==110){//gras_reoben
							n=96;
							}else if(c.getBlue()==120){
							n=128;
							}
						}
					}else if(c.getRed()==0){
						if(c.getGreen()==200){
							
							//busch
//								tileArray[x][y][1] = 0; //zunächst alle felder begehbar
							
							if(c.getBlue()==0) {
							n=201; //tileArray[x][y][1] = 1; //busch mittig, nicht begehbar
							}else if(c.getBlue()==10){
							n=200; //tileArray[x][y][1] = 1;	//busch unten, nicht begehbar
							}else if(c.getBlue()==20){
							n=202;
							}else if(c.getBlue()==30){
							n=181;
							}else if(c.getBlue()==40){
							n=221;
							}else if(c.getBlue()==50){
							n=180;
							}else if(c.getBlue()==60){
							n=182;
							}else if(c.getBlue()==70){
							n=220;
							}else if(c.getBlue()==80){
							n=222;
							}else if(c.getBlue()==90){
							n=144;
							}else if(c.getBlue()==100){
							n=143;
							}else if(c.getBlue()==110){
							n=124;
							}else if(c.getBlue()==120){
							n=123;
							}
						}
						
						if(c.getGreen()==255){
							//wasser
							// tileArray[x][y][1] = 1;	//kein feld begehbar
							
							if(c.getBlue()==255){
							n=39;
							}else if(c.getBlue()==10){//oben
							n=7;
							}else if(c.getBlue()==20){
							n=71;
							}else if(c.getBlue()==30){//links
							n=38;
							}else if(c.getBlue()==40){
							n=40;
							}else if(c.getBlue()==50){//lioben
							n=6;
							}else if(c.getBlue()==60){
							n=70;
							}else if(c.getBlue()==70){//reoben
							n=8;
							}else if(c.getBlue()==80){
							n=72;
							}else if(c.getBlue()==90){//gras_lioben
							n=133;
							}else if(c.getBlue()==100){
							n=101;
							}else if(c.getBlue()==110){//gras_reoben
							n=132;
							}else if(c.getBlue()==120){
							n=100;
							}
						}
					}else if(c.getRed()==100){
						if(c.getGreen()==100){
							//stein
							// tileArray[x][y][1] = 1;
							if(c.getBlue()==0)		 {
							n=81;
							}else if(c.getBlue()==10){
							n=80;
							}else if(c.getBlue()==20){
							n=82;
							}else if(c.getBlue()==30){
							n=61;
							}else if(c.getBlue()==40){
							n=101;
							}else if(c.getBlue()==50){
							n=60;
							}else if(c.getBlue()==60){
							n=62;
							}else if(c.getBlue()==70){
							n=100;
							}else if(c.getBlue()==80){
							n=102;
							}else if(c.getBlue()==90){
							n=64;
							}else if(c.getBlue()==100){
							n=63;
							}else if(c.getBlue()==110){
							n=44;
							}else if(c.getBlue()==120){
							n=43;
							}
						}
					}
					Tile t = new Tile(x,y,n);
					tilegrid[x][y] = t;
				}
			}
		} catch (IOException e1) {e1.printStackTrace();}
	}
	
	
	private void changeTile(short a, short b, short type){
		if((tilegrid[a][b].getType() == 19) && (a-1 >=0 ) && (b-1 >= 0) && (a+1 < World.WORLDSIZE) && (b+1 < World.WORLDSIZE)){
			
			short oben = tilegrid[a][b-1].getType();
			short unten = tilegrid[a][b+1].getType();
			short links = tilegrid[a-1][b].getType();
			short rechts = tilegrid[a+1][b].getType();
			short lioben = tilegrid[a-1][b-1].getType();
			short reoben = tilegrid[a+1][b-1].getType();
			short liunten = tilegrid[a-1][b+1].getType();
			short reunten = tilegrid[a+1][b+1].getType();
			
			if((links==type) && (rechts==type)){
				tilegrid[a][b].setType((short)33);
			}
			if((oben == type) && (unten == type)){
				tilegrid[a][b].setType((short)33);
			}
			if((reoben ==type) && (liunten == type) && (reunten != type) && (lioben != type)){
				tilegrid[a][b].setType((short)33);
			}
			if((reunten == type) && (lioben == type) && (reoben != type) && (liunten != type)){
				tilegrid[a][b].setType((short)33);
			}
			
			if((oben == type) && (links == type)){
				tilegrid[a][b].setType((short)96);
			}
			if((oben == type) && (rechts == type)){
				tilegrid[a][b].setType((short)97);
			}
			if((unten == type) && (links == type)){
				tilegrid[a][b].setType((short)128);
			}
			if((unten == type) && (rechts == type)){
				tilegrid[a][b].setType((short)129);
			}
					
			//	nur die 4 ecken:
			if((reoben ==type)&&(lioben !=type)&&(links !=type)&&(liunten !=type)&&(reunten !=type)&&(rechts !=type)&&(oben !=type)&&(unten !=type)){
				tilegrid[a][b].setType((short)64);
			}
			if((reoben !=type)&&(lioben == type)&&(links !=type)&&(liunten !=type)&&(reunten !=type)&&(rechts !=type)&&(oben !=type)&&(unten !=type)){
				tilegrid[a][b].setType((short)66);
			}
			if((reoben !=type)&&(lioben !=type)&&(links !=type)&&(liunten == type)&&(reunten !=type)&&(rechts !=type)&&(oben !=type)&&(unten !=type)){
				tilegrid[a][b].setType((short)2);
			}
			if((reoben !=type)&&(lioben !=type)&&(links !=type)&&(liunten !=type)&&(reunten == type)&&(rechts !=type)&&(oben !=type)&&(unten !=type)){
				tilegrid[a][b].setType((short)0);
			}

			if((rechts == type) && (links !=type) && (liunten !=type) && (lioben !=type) && (oben !=type) && (unten !=type)){
				tilegrid[a][b].setType((short)32);
			}
			if((links == type) && (reoben !=type) && (reunten !=type) && (rechts !=type) && (oben !=type) && (unten !=type)){
				tilegrid[a][b].setType((short)34);
			}
			if((oben == type) && (links !=type) && (liunten !=type) && (reunten !=type) && (rechts !=type) && (unten !=type)){
				tilegrid[a][b].setType((short)65);
			}
			if((unten == type) && (reoben !=type) && (lioben !=type) && (links !=type) && (rechts !=type) && (oben !=type)){
				tilegrid[a][b].setType((short)1);
			}
		}
	}
	
	private void calculateTileBorders(){
		for(short a = 0; a< World.WORLDSIZE;a++){
			for(short b = 0; b< World.WORLDSIZE;b++){
				changeTile(a,b,(short)33);
			}
		}
	}
	
	
	private Map<Integer, TextureEntry> createCoordMapFromTexture(Texture t){
		
		Map<Integer,TextureEntry> tileSetEntries = new HashMap<Integer,TextureEntry>();
		int tindex = 0;
		
		for(float y = 0; y< t.getTextureHeight(); y+=texW){
			for(float x = 0; x< t.getTextureWidth(); x+=texW){
				x = Math.round(x * 100.0f) /100.0f;		//Rundungsfehler umgehen (vorher war's z.b. 1.000002 und so...)
				y = Math.round(y * 100.0f) /100.0f;
				TextureEntry te = new TextureEntry(x,y,texW,texW);
				tileSetEntries.put(tindex, te);
				tindex++;
			}
		}
		return tileSetEntries;
	}
}
