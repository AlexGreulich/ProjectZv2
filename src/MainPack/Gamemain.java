package MainPack;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import Entities.Player;
import MainPack.World.StateofGame;
import static org.lwjgl.opengl.GL11.*;

public class Gamemain {

	private StateofGame state = StateofGame.INGAME;
	Level level;
	Player player;
	private static long lastFrame;
	float velocityX =0.0f, velocityY =0.0f;
	int mouseX=0, mouseY=0;
	GameMenu menu;
	
	
	public void start(){
		
		initDisplay();
		initGL();
		initGame();
		initTimer();
		
		while(!Display.isCloseRequested()){
			
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				switch(state){
				case INGAME:
					this.state = World.StateofGame.MENU;
					break;
				case MENU:
					this.state = World.StateofGame.INGAME;
					break;
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			switch(state){
				case INGAME:
					Mouse.setGrabbed(true);		//versteckt den mauscursor			
					player.calcDirection();
					
					if ((Keyboard.isKeyDown(Keyboard.KEY_A)) || (Keyboard.isKeyDown(Keyboard.KEY_LEFT))){
						if((player.direction.equals(World.PlayerDirection.LEFT)
							|| (player.direction.equals(World.PlayerDirection.LEFTDOWN)
							|| (player.direction.equals(World.PlayerDirection.LEFTUP))))){
							
							velocityX -= 0.05f;
						}else{
							velocityX -= 0.01f;
						}
					}
					if ((Keyboard.isKeyDown(Keyboard.KEY_D)) || (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))){
						if((player.direction.equals(World.PlayerDirection.RIGHT) 
							|| (player.direction.equals(World.PlayerDirection.RIGHTDOWN) 
							|| (player.direction.equals(World.PlayerDirection.RIGHTUP))))){
							
							velocityX += 0.05f;
						}else{
							velocityX += 0.01f;
						}
					}
					if ((Keyboard.isKeyDown(Keyboard.KEY_W)) || (Keyboard.isKeyDown(Keyboard.KEY_UP))){
						if((player.direction.equals(World.PlayerDirection.UP) 
							|| (player.direction.equals(World.PlayerDirection.RIGHTUP) 
							|| (player.direction.equals(World.PlayerDirection.LEFTUP))))){
							
							velocityY -= 0.05f;
						}else{
							velocityY -= 0.01f;
						}
					}
					if ((Keyboard.isKeyDown(Keyboard.KEY_S)) || (Keyboard.isKeyDown(Keyboard.KEY_DOWN))){
						if((player.direction.equals(World.PlayerDirection.DOWN) 
							|| (player.direction.equals(World.PlayerDirection.RIGHTDOWN) 
							|| (player.direction.equals(World.PlayerDirection.LEFTDOWN))))){
							
							velocityY += 0.05f;
							
						}else{
							velocityY += 0.01f;
						}				
					}
					
					if(velocityX >= 0.2f){
						velocityX = 0.2f;
					}
					else if(velocityX < -0.2f){
						velocityX =- 0.2f;
					}
					if(velocityY >= 0.2f){
						velocityY = 0.2f;
					}
					else if(velocityY < -0.2f){
						velocityY =- 0.2f;
					}
					
					if ((velocityX < -0.0025f) || (velocityX > 0.0025f) || (velocityY < -0.0025f) || (velocityY > 0.0025f)){
						player.isMoving = true;
					} else {
						player.isMoving = false;
					}
					
					int delta = getDelta();
					
					player.setX(player.getX() +velocityX *delta);
					player.setY(player.getY() +velocityY *delta);
					velocityX *= 0.9f;
					velocityY *= 0.9f;
					
					level.draw(player);
					player.draw();
					
					break;
					
				case MENU:
					Mouse.setGrabbed(false); //Mauscursor wieder da, alternativ könnte man auch einen eigenen cursor erstellen....
					level.draw(player);
					player.draw();
					menu.draw();
					
					break;
			}
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		Mouse.destroy();
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
		GL11.glLoadIdentity();
	}
	
	
	public void initGame(){
		player = new Player();
		level = new Level((int)player.getX(), (int)player.getY());
		menu = new GameMenu();
		
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
