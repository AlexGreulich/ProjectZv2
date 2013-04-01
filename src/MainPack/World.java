package MainPack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class World {

	public final static int WORLDSIZE = 4000;
	public static int TILES_ON_SCREEN_WIDTH;
	public static int TILES_ON_SCREEN_HEIGHT;
	
	public final static int TILE_SIZE = 32;
	
	public final static float PLAYERSTARTPOSITIONX = 10*32;
	public final static float PLAYERSTARTPOSITIONY = 10*32;
	
	//Berechnete Werte
	public static int TILE_MIDDLE_X; 
	public static int INT_MIDDLE_X; 
	public static int TILE_MIDDLE_Y; 
	public static int INT_MIDDLE_Y; 
	public static int CHUNK_BORDER_LR;
	public static int CHUNK_BORDER_TB;
	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;


	public final static int CHUNK_SIZE = WORLDSIZE/80; //100
	
	public final static int playerMouseRadius = 64;
	
	public static Texture TILESET;


	public enum StateofGame { INGAME, MENU, INVENTORY }
	public enum PlayerDirection { UP,DOWN,LEFT,RIGHT,LEFTUP,LEFTDOWN,RIGHTUP,RIGHTDOWN }
	public enum ItemType { TOOL, WEAPON, MEDIC, COLLECTABLE, OTHER }
	public enum MenuOption { NEWGAME, OPTIONS, CREDITS, EXIT, MAINMENU }
	
	public static void setTILES_ON_SCREEN_WIDTH(int i) {
		TILES_ON_SCREEN_WIDTH = (((i/TILE_SIZE)/2)*2)+2;
		CHUNK_BORDER_LR = TILES_ON_SCREEN_WIDTH/2;
		TILE_MIDDLE_X = TILES_ON_SCREEN_WIDTH / 2; 
		INT_MIDDLE_X = TILES_ON_SCREEN_WIDTH * TILE_SIZE / 2;
		SCREENWIDTH =i;

	}
	public static void setTILES_ON_SCREEN_HEIGHT(int i) {
		TILES_ON_SCREEN_HEIGHT = (((i/TILE_SIZE)/2)*2)+2;
		CHUNK_BORDER_TB = TILES_ON_SCREEN_HEIGHT/2;
		TILE_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT / 2;
		INT_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT * TILE_SIZE / 2;
		SCREENHEIGHT =i;

	}
	public static void loadTextures(){
		try {
			TILESET = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/tilesetnewalign.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

}
