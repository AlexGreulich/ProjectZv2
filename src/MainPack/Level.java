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
	
//	public Texture tilesetTexture = null;
	int tilesetW = 0;
	int tilesetH = 0;
	float texH = 0f, texW = 0f;
	private Map<Integer,Tile> tileMap = new HashMap<Integer,Tile>();
	BufferedImage map, itemmap;
	Color c = null;
	
	private int screenDeltaX = 0, screenDeltaY = 0;
	float percentage = 32f/2048;
	float u, v, u2, v2;
	int chunkLeftCornerX, chunkLeftCornerY, chunkBorderRight, chunkBorderBottom;
	
	Tile[][] currentTileGrid;	
	int creatingcount = 0;
	boolean chunkChanged = false;
	Tile te = null;
	
	ItemHandler itemHandler;
	
	int currentX, currentY = 0;
	
	
	public Level(int x, int y){
		//	Lade das tileset als eine große Textur
		try {
//			tilesetTexture = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/tilesetnewalign.png"));//Tileset_neu_32-1024b
		
			texW = World.TILESET.getTextureWidth()/64f;
			texH = World.TILESET.getTextureHeight()/64f;
			map = ImageIO.read(getClass().getResource("/karten/grossekarte-new.png"));
			itemmap = ImageIO.read(new File("src/karten/itemmap-new.png"));
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
		
		currentTileGrid = new Tile[World.CHUNK_SIZE + 2*World.CHUNK_BORDER_LR][World.CHUNK_SIZE + 2*World.CHUNK_BORDER_TB];
		
		//Jetzt die Koordinaten der Einzeltiles aus der Textur holen
		createCoordMapFromTexture(World.TILESET);
		initCurrentTileGrid();	//nötig falls unten rechts gestartet wird
		itemHandler = new ItemHandler();
		initTotalItems();
		createCurrentTileGrid(x, y);
		
	}
	
	public void update(int x, int y){
		createCurrentTileGrid(x,y);
//		itemHandler.update(x, y);
		//itemhandler updaten?
	}
	public void initTotalItems(){
				
		for(int a = 0;a < itemmap.getWidth(); a++){
			for(int b = 0;b < itemmap.getHeight(); b++){
				
				Color tempitemcolor = new Color(itemmap.getRGB(a,b));
				
				if(tempitemcolor.equals(new Color(50,50,50))){
					Item item = new Item((short)a,(short)b,0,0,28);
					itemHandler.totalItemsOnMap[a][b]= 28;
					
					
				}
				else if (tempitemcolor.equals(new Color(100,100,100))){
					Item item = new Item((short)a,(short)b,0,0,29);
					itemHandler.totalItemsOnMap[a][b] = 29;
				}
				else if (tempitemcolor.equals(new Color(150,150,150))){
					Item item = new Item((short)a,(short)b,0,0,30);
					itemHandler.totalItemsOnMap[a][b] = 30;
				}
				else if (tempitemcolor.equals(new Color(200,200,200))){
					Item item = new Item((short)a,(short)b,0,0,31);
					itemHandler.totalItemsOnMap[a][b] = 31;
				}else{
					
				}
			}
		}
		
		
		
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
		
		glBindTexture(GL_TEXTURE_2D, World.TILESET.getTextureID());
		
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
	public Item getItemAt (int x, int y){
		System.out.println("tried to pick up at playerXY: "+ x + ","+y);
		Item i = null;
		int index =itemHandler.totalItemsOnMap[x/32][y/32];
		System.out.println("itemhandler.totalitemsonmaps[x/32][y/32]:"+ x/32 + ","+y/32+"-> index: "+index);
		if(index != 0){
			i = new Item((short)0,(short) 0, 0, 0, index);
			itemHandler.totalItemsOnMap[x/32][y/32] =0;
			System.out.println("tried to create new item with index "+index); 
		}
		
		return i;
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
				n = c.getRed()*65536 + c.getGreen()*256 + c.getBlue();
				
				currentTileGrid[a][b] = tileMap.get(n);
				
				Color tempitemcolor = new Color(itemmap.getRGB(a,b));
				if(tempitemcolor.equals(new Color(0,0,31))){
					Item item = new Item((short)a,(short)b,0,0,28);
					itemHandler.itemsInChunk[a][b] = item;
					
					
				}
				else if (tempitemcolor.equals(new Color(0,0,28))){
					Item item = new Item((short)a,(short)b,0,0,29);
					itemHandler.itemsInChunk[a][b] = item;
				}
				else if (tempitemcolor.equals(new Color(0,0,30))){
					Item item = new Item((short)a,(short)b,0,0,30);
					itemHandler.itemsInChunk[a][b] = item;
				}
				else if (tempitemcolor.equals(new Color(0,0,29))){
					Item item = new Item((short)a,(short)b,0,0,31);
					itemHandler.itemsInChunk[a][b] = item;
				}
				
			}
		}
	}
	
	
	private void createCoordMapFromTexture(Texture t){
		int tindex = 0;
		
		for(float y = 0; y < t.getTextureHeight(); y+=World.TILE_SIZE){
			for(float x = 0; x < t.getTextureWidth(); x+=World.TILE_SIZE){
				x = Math.round(x * 100.0f) /100.0f;		//Rundungsfehler umgehen (vorher war's z.b. 1.000002 und so...)
				y = Math.round(y * 100.0f) /100.0f;
				if ((tindex == 39 )){ //komplett unbegehbar
					te = new Tile(x,y,World.TILE_SIZE,World.TILE_SIZE, 0, 0, World.TILE_SIZE, World.TILE_SIZE, tindex);
				} else {
					te = new Tile(x,y,World.TILE_SIZE,World.TILE_SIZE,tindex);
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
