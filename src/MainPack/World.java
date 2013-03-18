package MainPack;

public class World {

	public final static int WORLDSIZE = 4000;
	public final static int TILES_ON_SCREEN_WIDTH = 20;
	public final static int TILES_ON_SCREEN_HEIGHT = 20;
	public final static int TILE_SIZE = 32;
	final static int WIDTH = 640;
	final static int HEIGHT = 640;
	public final static float PLAYERSTARTPOSITIONX = 2000*32;
	public final static float PLAYERSTARTPOSITIONY = 2000*32;
	
	//Berechnete Werte
	public final static int TILE_MIDDLE_X = TILES_ON_SCREEN_WIDTH / 2; 
	public final static int INT_MIDDLE_X = TILES_ON_SCREEN_WIDTH * TILE_SIZE / 2; 
	public final static int TILE_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT / 2; 
	public final static int INT_MIDDLE_Y = TILES_ON_SCREEN_HEIGHT * TILE_SIZE / 2; 
	public final static int CHUNK_BORDER = TILES_ON_SCREEN_WIDTH/2;
	public final static int CHUNK_SIZE = WORLDSIZE/40;
	
	public final static int playerMouseRadius = 64;
	public final static float PLAYER_MAX_VELOCITY = 0.5f;
	public final static float PLAYER_VELOCITY_FORWARD = 0.05f;
	public final static float PLAYER_VELOCITY_BACKWARD = 0.01f;

	public enum StateofGame { INGAME, MENU }
	public enum PlayerDirection { UP,DOWN,LEFT,RIGHT,LEFTUP,LEFTDOWN,RIGHTUP,RIGHTDOWN }
}
