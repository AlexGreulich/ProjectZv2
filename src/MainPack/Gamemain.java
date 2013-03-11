package MainPack;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import Entities.Player;
import static org.lwjgl.opengl.GL11.*;

public class Gamemain {

	private Gamestate state = Gamestate.INGAME;
	Level level;
	Player player;
	private static long lastFrame;
	
	
	public void start(){
		
		initDisplay();
		initGL();
		initGame();
		initTimer();
		
		while(!Display.isCloseRequested()){
			
			switch(state){
				case INGAME:
				
					if((Keyboard.isKeyDown(Keyboard.KEY_A)) || (Keyboard.isKeyDown(Keyboard.KEY_LEFT))){
						player.setX(player.getX()-1);
						player.isMoving = true;
					}else if((Keyboard.isKeyDown(Keyboard.KEY_D)) || (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))){
						player.setX(player.getX()+1);
						player.isMoving = true;
					}else if((Keyboard.isKeyDown(Keyboard.KEY_W)) || (Keyboard.isKeyDown(Keyboard.KEY_UP))){
						player.setY(player.getY()-1);
						player.isMoving = true;
					}else if((Keyboard.isKeyDown(Keyboard.KEY_S)) || (Keyboard.isKeyDown(Keyboard.KEY_DOWN))){
						player.setY(player.getY()+1);
						player.isMoving = true;
					} else {
						player.isMoving = false;
					}
					
					level.draw(player);
					player.draw();
					break;
					
				case MENU:
					break;
			}
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}
	
	
	public void initDisplay(){
		try {
			Display.setDisplayMode(new DisplayMode(640,640));
			Display.setTitle("ProjectZ v.2");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void initGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, World.WIDTH, World.HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);               
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);         
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		 
		GL11.glViewport(0, 0, World.WIDTH, World.HEIGHT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		 
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, World.WIDTH, World.HEIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	
	public void initGame(){
		level = new Level(World.PLAYERSTARTPOSITIONX, World.PLAYERSTARTPOSITIONY);
		player = new Player();
	}
	
	
	private static void initTimer() {
        lastFrame = getTime();
    }
	
	
	private static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
	

    private static int getDelta() {
        long currentTime = getTime();
        int delta = (int) (currentTime - lastFrame);
        lastFrame = getTime();
        return delta;
    }
	
	// MAIN
	public static void main(String[] args){
		Gamemain gm =new Gamemain();
		gm.start();
	}
	
}
