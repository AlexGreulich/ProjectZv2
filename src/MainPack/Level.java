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
	private short[][][] wholeMapArray = new short[400][400][4]; // ACHTUNG zu testzwecken verkleinert
	private int playerdeltax = 0, playerdeltay = 0;
	float percentage = 32f/1024f;
	int uebergangx = 32, uebergangy = 32;
	
	
	public Level(int x, int y){
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
	}
	
	
	public void draw(Player player){
		int cornerx = 0;
		if (player.screenx == (World.TILES_ON_SCREEN_WIDTH*32 / 2)){
			cornerx = player.getX()/32 - World.TILES_ON_SCREEN_WIDTH/2;
		}
		
		int cornery = 0;
		if (player.screeny == (World.TILES_ON_SCREEN_HEIGHT*32 / 2)){
			cornery = player.getY()/32 - World.TILES_ON_SCREEN_HEIGHT/2;
		} 	
		
		glBindTexture(GL_TEXTURE_2D, tilesetTexture.getTextureID());
		
		playerdeltax = player.getX()%32;
		playerdeltay = player.getY()%32;
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
								
				TextureEntry te = textureEntryMap.get((int)wholeMapArray[cornerx+a][cornery+b][0]);
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
			BufferedImage bi = ImageIO.read(getClass().getResource("/karten/grossekarte.gif"));
			Color c = Color.BLACK;
			
//			for(int x = 0; x < bi.getWidth(); x++){
//				for(int y = 0; y< bi.getHeight();y++){ // ACHTUNG zu testzwecken verkleinert
			for(int x = 0; x < 400; x++){
				for(int y = 0; y < 400;y++){
				
					c = new Color(bi.getRGB(x,y));
					
					if(c.equals(new Color(100,200,100))){
						wholeMapArray[x][y][0] = 16;
						wholeMapArray[x][y][1] = 0;
					}
					
					if(c.getRed() == 255){
						if(c.getGreen() == 255){
							
							//sand
							wholeMapArray[x][y][1] = 0;	//da sand, sind alle felder begehbar
							
							if(c.getBlue()==0)		 {wholeMapArray[x][y][0] = 			33;			
													
							}else if(c.getBlue()==10){wholeMapArray[x][y][0] = 			1;//oben
							}else if(c.getBlue()==20){wholeMapArray[x][y][0]= 			65;
							}else if(c.getBlue()==30){wholeMapArray[x][y][0] =			32;//links
							}else if(c.getBlue()==40){wholeMapArray[x][y][0] = 			34;
							}else if(c.getBlue()==50){wholeMapArray[x][y][0] = 			0;//lioben
							}else if(c.getBlue()==60){wholeMapArray[x][y][0] = 			64;
							}else if(c.getBlue()==70){wholeMapArray[x][y][0] = 			2;//reoben
							}else if(c.getBlue()==80){wholeMapArray[x][y][0] = 			66;
							}else if(c.getBlue()==90){wholeMapArray[x][y][0] = 			129;//gras_lioben
							}else if(c.getBlue()==100){wholeMapArray[x][y][0] = 		97;
							}else if(c.getBlue()==110){wholeMapArray[x][y][0] =			128;//gras_reoben
							}else if(c.getBlue()==120){wholeMapArray[x][y][0] = 		96;
							}
						}	
					}
					else if(c.getRed()==0){
						if(c.getGreen()==200){
							
							//busch
							wholeMapArray[x][y][1] = 0; //zunächst alle felder begehbar
							
							if(c.getBlue()==0)		 {wholeMapArray[x][y][0] = 201;
														wholeMapArray[x][y][1] = 1; //busch mittig, nicht begehbar
							}else if(c.getBlue()==10){wholeMapArray[x][y][0] = 200;
														wholeMapArray[x][y][1] = 1;	//busch unten, nicht begehbar
							}else if(c.getBlue()==20){wholeMapArray[x][y][0] = 202;
							}else if(c.getBlue()==30){wholeMapArray[x][y][0] = 181;
							}else if(c.getBlue()==40){wholeMapArray[x][y][0] = 221;
							}else if(c.getBlue()==50){wholeMapArray[x][y][0] = 180;
							}else if(c.getBlue()==60){wholeMapArray[x][y][0] = 182;
							}else if(c.getBlue()==70){wholeMapArray[x][y][0] = 220;
							}else if(c.getBlue()==80){wholeMapArray[x][y][0] = 222;
							}else if(c.getBlue()==90){wholeMapArray[x][y][0] = 144;
							}else if(c.getBlue()==100){wholeMapArray[x][y][0] = 143;
							}else if(c.getBlue()==110){wholeMapArray[x][y][0] = 124;
							}else if(c.getBlue()==120){wholeMapArray[x][y][0] = 123;
							}
						}
						
						if(c.getGreen()==255){
							
							//wasser
							wholeMapArray[x][y][1] = 1;	//kein feld begehbar
							
							if(c.getBlue()==255)	 {wholeMapArray[x][y][0] = 				39;	//mittig
							}else if(c.getBlue()==10){wholeMapArray[x][y][0] = 				7;	//oben
							}else if(c.getBlue()==20){wholeMapArray[x][y][0] = 				71;//unten
							}else if(c.getBlue()==30){wholeMapArray[x][y][0] = 				38;//links
							}else if(c.getBlue()==40){wholeMapArray[x][y][0] = 				40;//rechts
							}else if(c.getBlue()==50){wholeMapArray[x][y][0] = 				6;//lioben
							}else if(c.getBlue()==60){wholeMapArray[x][y][0] = 				70;//liunten
							}else if(c.getBlue()==70){wholeMapArray[x][y][0] = 				8;//reoben
							}else if(c.getBlue()==80){wholeMapArray[x][y][0] = 				72;//reunten
							}else if(c.getBlue()==90){wholeMapArray[x][y][0] = 				133;//4+5graslioben
							}else if(c.getBlue()==100){wholeMapArray[x][y][0] = 			101;//grasliunten
							}else if(c.getBlue()==110){wholeMapArray[x][y][0] = 			132;//grasreoben
							}else if(c.getBlue()==120){wholeMapArray[x][y][0] = 			100;//grasreunten
							}
						}
						
					}else if(c.getRed()==100){
						if(c.getGreen()==100){
							
							//stein
							wholeMapArray[x][y][1] = 1;
							
							if(c.getBlue()==0)		 {wholeMapArray[x][y][0] = 81;
							}else if(c.getBlue()==10){wholeMapArray[x][y][0] = 80;
							}else if(c.getBlue()==20){wholeMapArray[x][y][0] = 82;
							}else if(c.getBlue()==30){wholeMapArray[x][y][0] = 61;
							}else if(c.getBlue()==40){wholeMapArray[x][y][0] = 101;
							}else if(c.getBlue()==50){wholeMapArray[x][y][0] = 60;
							}else if(c.getBlue()==60){wholeMapArray[x][y][0] = 62;
							}else if(c.getBlue()==70){wholeMapArray[x][y][0] = 100;
							}else if(c.getBlue()==80){wholeMapArray[x][y][0] = 102;
							}else if(c.getBlue()==90){wholeMapArray[x][y][0] = 64;
							}else if(c.getBlue()==100){wholeMapArray[x][y][0] = 63;
							}else if(c.getBlue()==110){wholeMapArray[x][y][0] = 44;
							}else if(c.getBlue()==120){wholeMapArray[x][y][0] = 43;
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
