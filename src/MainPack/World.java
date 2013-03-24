package MainPack;

public class World {

	public final static int WORLDSIZE = 4000;
	public static int TILES_ON_SCREEN_WIDTH;
	public static int TILES_ON_SCREEN_HEIGHT;
	
	public final static int TILE_SIZE = 32;
	
	public final static float PLAYERSTARTPOSITIONX = 2000*32;
	public final static float PLAYERSTARTPOSITIONY = 2000*32;
	
	//Berechnete Werte
	public final static int TILE_MIDDLE_X = TILES_ON_SCREEN_WIDTH / 2; 
	public final static int INT_MIDDLE_X = TILES_ON_SCREEN_WIDTH * TILE_SIZE / 2; 
	public final static int TILE_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT / 2; 
	public final static int INT_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT * TILE_SIZE / 2; 
	public final static int CHUNK_BORDER_LR = (TILES_ON_SCREEN_WIDTH/2)+1;
	public final static int CHUNK_BORDER_TB = (TILES_ON_SCREEN_HEIGHT/2)+1;

	public final static int CHUNK_SIZE = WORLDSIZE/40;
	
	public final static int playerMouseRadius = 64;

	public enum StateofGame { INGAME, MENU }
	public enum PlayerDirection { UP,DOWN,LEFT,RIGHT,LEFTUP,LEFTDOWN,RIGHTUP,RIGHTDOWN }
	public enum ItemType { TOOL, WEAPON, MEDIC, COLLECTABLE }
	public enum MenuOption { NEWGAME, OPTIONS, CREDITS, EXIT, MAINMENU }
}
