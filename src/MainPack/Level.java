package MainPack;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
 * Die Position des Spielfelds ist immer vom Spieler abh�ngig
 * die Chunks sind alle gleich gro� und �berlappen sich um die halbe breite des Screens,
 * d.h. das currentTileGrid ist so breit wie ein Chunk plus eventuelle Border
 * Ist der Spieler am Rand wird kein Border zum currentTileGrid hinzugef�gt
*/

public class Level {
	
	public Texture tilesetTexture = null;
	int tilesetW = 0;
	int tilesetH = 0;
	float texH = 0f, texW = 0f;
	private Map<Integer,TextureEntry> textureEntryMap = null;
	
	private int screenDeltaX = 0, screenDeltaY = 0;
	float percentage = 32f/1024f;
	float u, v, u2, v2;
	int chunkLeftCornerX, chunkLeftCornerY, chunkBorderRight, chunkBorderBottom;
	
	Tile[][] tilegrid = new Tile[World.WORLDSIZE][World.WORLDSIZE];
	Tile[][] currentTileGrid = new Tile[World.CHUNK_SIZE + 2*World.CHUNK_BORDER][World.CHUNK_SIZE + 2*World.CHUNK_BORDER];	
	int creatingcount=0;
	boolean chunkChanged = false;
	
	
	public Level(int x, int y){
		//	Lade das tileset als eine gro�e Textur
		try {
			tilesetTexture = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/Tileset_neu_32-1024b.png"));
		
			texW = tilesetTexture.getTextureWidth()/32f;
			texH = tilesetTexture.getTextureHeight()/32f;
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		//Jetzt die Koordinaten der Einzeltiles aus der Textur holen
		textureEntryMap = createCoordMapFromTexture(tilesetTexture);
		createFinalMap();
		calculateTileBorders();
		initCurrentTileGrid();	//n�tig falls unten rechts gestartet wird
		createCurrentTileGrid(x, y);
	}
	
	private void initCurrentTileGrid() {
		for(int a = 0; a < World.CHUNK_SIZE + 2*World.CHUNK_BORDER; a++){
			for(int b = 0; b< World.CHUNK_SIZE + 2*World.CHUNK_BORDER; b++){
				currentTileGrid[a][b] = new Tile((short)a,(short)b,(short)1);
			}
		}
	}

	public void createCurrentTileGrid(int x, int y){
//		System.out.println("createCurrentTileGrid" + creatingcount);
		creatingcount++;
		// auf welchem Chunk befindet sich der Spieler?
		chunkLeftCornerX = ((x/32) / World.CHUNK_SIZE) * World.CHUNK_SIZE;
		chunkLeftCornerY = ((y/32) / World.CHUNK_SIZE) * World.CHUNK_SIZE;
		
		// Wo werden R�nder ben�tigt?
		if (x < (World.WORLDSIZE - World.CHUNK_SIZE) * 32){	// gibt es einen rechten Rand?
			chunkBorderRight = World.CHUNK_BORDER;
		} else {chunkBorderRight = 0;}
		if (y < (World.WORLDSIZE - World.CHUNK_SIZE) * 32){	//gibt es einen unteren Rand
			chunkBorderBottom = World.CHUNK_BORDER;
		} else {chunkBorderBottom = 0;}
		if (chunkLeftCornerX != 0){chunkLeftCornerX -= World.CHUNK_BORDER;};	// gibt es einen linken Rand?
		if (chunkLeftCornerY != 0){chunkLeftCornerY -= World.CHUNK_BORDER;};	// gibt es einen oberen Rand?
		
		// currentTileGrid f�llen
		for(int a = 0; a < World.CHUNK_SIZE+World.CHUNK_BORDER + chunkBorderRight; a++){
			for(int b = 0; b< World.CHUNK_SIZE+World.CHUNK_BORDER + chunkBorderBottom; b++){
				currentTileGrid[a][b] = tilegrid[chunkLeftCornerX +a][chunkLeftCornerY +b];
			}
		}
	}
	
	public void draw(Player player){
		screenDeltaX = (int) (player.getX() -chunkLeftCornerX *32 -player.screenx);
		screenDeltaY = (int) (player.getY() -chunkLeftCornerY *32 -player.screeny);
		
		if ((screenDeltaX+player.screenx >= World.CHUNK_SIZE*32+World.CHUNK_BORDER*32) ||(screenDeltaX+player.screenx < World.CHUNK_BORDER*32 ) || (screenDeltaY+player.screeny >= World.CHUNK_SIZE*32+World.CHUNK_BORDER*32) ||(screenDeltaY+player.screeny < World.CHUNK_BORDER*32 )){
			createCurrentTileGrid((int)player.getX(), (int)player.getY());
			screenDeltaX = (int) (player.getX() - chunkLeftCornerX *32 -player.screenx);
			screenDeltaY = (int) (player.getY() - chunkLeftCornerY *32 -player.screeny);
			chunkChanged = true;
		}
		
		glBindTexture(GL_TEXTURE_2D, tilesetTexture.getTextureID());
		
		glTranslatef((float)-screenDeltaX, (float)-screenDeltaY, 0f);
		
//		System.out.println("player: "+player.getX()+"|"+player.getY()+"    screendeltax/y: "+screenDeltaX+"|"+screenDeltaY+"     screenx/y: "+(int)player.screenx+"|"+(int)player.screeny+"    chunkLeftCornerx/Y: "+chunkLeftCornerX+"|"+chunkLeftCornerY);
		
		glBegin(GL_QUADS);
		
		for(int a = 0; a < currentTileGrid.length; a++){
			for(int b = 0; b < currentTileGrid.length; b++){
								
				TextureEntry te = textureEntryMap.get((int)currentTileGrid[a][b].getType());
				u = te.getX()/texW * percentage;//
				v = te.getY()/texW * percentage;//
				u2 = (te.getX()+texW)/texW * percentage;//
				v2 = (te.getY()+texW)/texW * percentage;//
				
			//	Texturkoord.		..	an Bildschirmausschnitt
				glTexCoord2f(u,v);		glVertex2f(a*texW,		b*texW);		
				glTexCoord2f(u2,v);		glVertex2f(a*texW+texW,	b*texW);		
				glTexCoord2f(u2,v2);	glVertex2f(a*texW+texW,	b*texW+texW);	
				glTexCoord2f(u,v2);		glVertex2f(a*texW,		b*texW+texW);
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
							if((c.equals(new Color(100,200,100)) || (c.equals(new Color(255,0,0))))){
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
//								tileArray[x][y][1] = 0; //zun�chst alle felder begehbar
							
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
					if(c.equals(new Color(255,0,0))){
						t.spawnsZombie =true;
					}
					tilegrid[x][y] = t;
				}
			}
		} catch (IOException e1) {e1.printStackTrace();}
	}
	
	private void changeTile(short a, short b, short type, int c){
		
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
				tilegrid[a][b].setType(type);
			}
			if((oben == type) && (unten == type)){
				tilegrid[a][b].setType(type);
			}
			if((reoben ==type) && (liunten == type) && (reunten != type) && (lioben != type)){
				tilegrid[a][b].setType(type);
			}
			if((reunten == type) && (lioben == type) && (reoben != type) && (liunten != type)){
				tilegrid[a][b].setType(type);
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
			
			if((oben != type) && (unten != type) && (rechts != type) && (links != type) && (reoben != type) && (reunten != type) && (lioben != type) && (liunten != type)){
				
				tilegrid[a][b].setType((short)c);
			}
		}
	}
	
	private void calculateTileBorders(){
		
		int[] randomgrass = {160,161,162,163,192,193,194,195,224,225,227,256,258,259,288,289,290,291};
		Random rnd = new Random();
		for(short a = 0; a< World.WORLDSIZE;a++){
			for(short b = 0; b< World.WORLDSIZE;b++){
				int d = randomgrass[rnd.nextInt(18)];
//				System.out.println(d);
				changeTile(a,b,(short)33, d);
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
