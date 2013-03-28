package MainPack;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTranslatef;

/*
 * Die Position des Spielfelds ist immer vom Spieler abhängig
 * die Chunks sind alle gleich groß und überlappen sich um die halbe breite des Screens,
 * d.h. das currentTileGrid ist so breit wie ein Chunk plus eventuelle Border
 * Ist der Spieler am Rand wird kein Border zum currentTileGrid hinzugefügt
*/

public class Level {
	
	public Texture tilesetTexture = null;
	int tilesetW = 0;
	int tilesetH = 0;
	float texH = 0f, texW = 0f;
	private Map<Integer,Tile> tileMap = new HashMap<Integer,Tile>();
	BufferedImage map, itemmap;
	Color c = null;
	
	private int screenDeltaX = 0, screenDeltaY = 0;
	float percentage = 32f/1024f;
	float u, v, u2, v2;
	int chunkLeftCornerX, chunkLeftCornerY, chunkBorderRight, chunkBorderBottom;
	
	Tile[][] currentTileGrid;	
	int creatingcount=0;
	boolean chunkChanged = false;
	Tile te = null;
	
	ItemHandler itemHandler;
	
	int currentX, currentY =0;
	
	
	public Level(int x, int y){
		//	Lade das tileset als eine große Textur
		try {
			tilesetTexture = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/tilesetnewalign.png"));//Tileset_neu_32-1024b
		
			texW = tilesetTexture.getTextureWidth()/32f;
			texH = tilesetTexture.getTextureHeight()/32f;
			map = ImageIO.read(getClass().getResource("/karten/grossekarte-kreise.gif"));
			itemmap = ImageIO.read(new File("src/karten/itemmap.png"));
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		currentTileGrid = new Tile[World.CHUNK_SIZE + 2*World.CHUNK_BORDER_LR][World.CHUNK_SIZE + 2*World.CHUNK_BORDER_TB];
		
		//Jetzt die Koordinaten der Einzeltiles aus der Textur holen
		createCoordMapFromTexture(tilesetTexture);
		initCurrentTileGrid();	//nötig falls unten rechts gestartet wird
		itemHandler = new ItemHandler();
		createCurrentTileGrid(x, y);
		
	}
	
	public void update(int x, int y){
		createCurrentTileGrid(x,y);
//		itemHandler.update(x, y);
		//itemhandler updaten?
	}
	
	public void draw(Player player){
		
		screenDeltaX = (int) ((int)player.getX() -chunkLeftCornerX *32 -player.screenx);
		screenDeltaY = (int) ((int)player.getY() -chunkLeftCornerY *32 -player.screeny);
		
		if ((screenDeltaX+player.screenx >= World.CHUNK_SIZE*32+World.CHUNK_BORDER_LR*32) ||(screenDeltaX+player.screenx < World.CHUNK_BORDER_LR*32 ) || (screenDeltaY+player.screeny >= World.CHUNK_SIZE*32+World.CHUNK_BORDER_TB*32) ||(screenDeltaY+player.screeny < World.CHUNK_BORDER_TB*32 )){
			update((int)player.getX(), (int)player.getY());
			screenDeltaX = (int) ((int)player.getX() - chunkLeftCornerX *32 -player.screenx);
			screenDeltaY = (int) ((int)player.getY() - chunkLeftCornerY *32 -player.screeny);
			chunkChanged = true;
		}
		
		glBindTexture(GL_TEXTURE_2D, tilesetTexture.getTextureID());
		
		glTranslatef((float)-screenDeltaX, (float)-screenDeltaY, 0f);
		
		glClear(GL_COLOR_BUFFER_BIT);
		
		glBegin(GL_QUADS);
		
		for(int a = 0; a < World.CHUNK_SIZE+World.CHUNK_BORDER_LR + chunkBorderRight; a++){
			for(int b = 0; b < World.CHUNK_SIZE+World.CHUNK_BORDER_TB + chunkBorderBottom; b++){
				te = tileMap.get((int)currentTileGrid[a][b].getType());
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
		itemHandler.draw(World.CHUNK_SIZE+World.CHUNK_BORDER_LR + chunkBorderRight, World.CHUNK_SIZE+World.CHUNK_BORDER_TB + chunkBorderBottom);
		glLoadIdentity();
	}
	
	
	private void initCurrentTileGrid() {
		for(int a = 0; a < World.CHUNK_SIZE + 2*World.CHUNK_BORDER_LR; a++){
			for(int b = 0; b< World.CHUNK_SIZE + 2*World.CHUNK_BORDER_TB; b++){
				currentTileGrid[a][b] = tileMap.get(1);
			}
		}
	}
//	public Rectangle getItemBounds(short c, short d){
//	
//	}
	
	public void checkForItem(float a, float b){
//		
	}

	public void createCurrentTileGrid(int x, int y){
		// auf welchem Chunk befindet sich der Spieler?
		chunkLeftCornerX = ((x/32) / World.CHUNK_SIZE) * World.CHUNK_SIZE;
		chunkLeftCornerY = ((y/32) / World.CHUNK_SIZE) * World.CHUNK_SIZE;
		
		// Wo werden Ränder benötigt?
		if (x < (World.WORLDSIZE - World.CHUNK_SIZE) * 32){	// gibt es einen rechten Rand?
			chunkBorderRight = World.CHUNK_BORDER_LR;
		} else {chunkBorderRight = 0;}
		if (y < (World.WORLDSIZE - World.CHUNK_SIZE) * 32){	//gibt es einen unteren Rand?
			chunkBorderBottom = World.CHUNK_BORDER_TB;
		} else {chunkBorderBottom = 0;}
		if (chunkLeftCornerX != 0){chunkLeftCornerX -= World.CHUNK_BORDER_LR;};	// gibt es einen linken Rand?
		if (chunkLeftCornerY != 0){chunkLeftCornerY -= World.CHUNK_BORDER_TB;};	// gibt es einen oberen Rand?
		
		
		
		// currentTileGrid füllen
		for(int a = 0; a < World.CHUNK_SIZE+World.CHUNK_BORDER_LR + chunkBorderRight; a++){
			for(int b = 0; b < World.CHUNK_SIZE+World.CHUNK_BORDER_TB + chunkBorderBottom; b++){
				int n = 0;
				c = new Color(map.getRGB(chunkLeftCornerX+a, chunkLeftCornerY+b));
						if((c.equals(new Color(100,200,100)) || (c.equals(new Color(255,0,0))))){
							n=19;
						}
						
				if(c.getRed() == 255){
					if(c.getGreen() == 255){
						//sand
						// grid[x][y] = 0;	//da sand, sind alle felder begehbar
						
						if(c.getBlue()==0)		 {			//vorher :  {tileArray[x][y] = new Tile(tileset.getTileImage(201));
						n=0;
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
				
				currentTileGrid[a][b] = tileMap.get(n);
				
				Color tempitemcolor = new Color(itemmap.getRGB(a,b));
				if(tempitemcolor.equals(new Color(50,50,50))){
					Item item = new Item((short)a,(short)b,0,0,28);
					itemHandler.itemsInChunk[a][b] =item;
					
				}
				else if (tempitemcolor.equals(new Color(100,100,100))){
					Item item = new Item((short)a,(short)b,0,0,29);
					itemHandler.itemsInChunk[a][b] =item;
				}
				else if (tempitemcolor.equals(new Color(150,150,150))){
					Item item = new Item((short)a,(short)b,0,0,30);
					itemHandler.itemsInChunk[a][b] =item;
				}
				else if (tempitemcolor.equals(new Color(200,200,200))){
					Item item = new Item((short)a,(short)b,0,0,31);
					itemHandler.itemsInChunk[a][b] =item;
				}
				
			}
		}
	}
	
	
	private void createCoordMapFromTexture(Texture t){
		int tindex = 0;
		
		for(float y = 0; y< t.getTextureHeight(); y+=texW){
			for(float x = 0; x< t.getTextureWidth(); x+=texW){
				x = Math.round(x * 100.0f) /100.0f;		//Rundungsfehler umgehen (vorher war's z.b. 1.000002 und so...)
				y = Math.round(y * 100.0f) /100.0f;
				if ((tindex == 39 )){ //komplett unbegehbar
					te = new Tile(x,y,texW,texW, 0, 0, texW, texW, tindex);
				} else {
					te = new Tile(x,y,texW,texW,tindex);
				}
				
				tileMap.put(tindex, te);
				tindex++;
			}
		}
	}


	public int getScreenDeltaX() {
		return screenDeltaX;
	}


	public int getScreenDeltaY() {
		return screenDeltaY;
	}
	
	
}
