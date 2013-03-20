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
	int mouseX=0, mouseY=0;
	GameMenu menu;
	
	
	public void start(){
		
		initDisplay();
		initGL();
		initGame();
		initTimer();
		
		while((!Display.isCloseRequested()) && !((Keyboard.isKeyDown(Keyboard.KEY_Q)))) {
			
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
					//Mouse.setGrabbed(true);		//versteckt den mauscursor			

					player.calcDirection(getDelta());
					
					// vorwärts gehen
					if(Keyboard.isKeyDown(Keyboard.KEY_W)){
						player.changeSpeed(0.05f);
					}
					// rückwärts gehen
					if(Keyboard.isKeyDown(Keyboard.KEY_S)){
						player.changeSpeed(-0.05f);
					}
					
					//test speed verdoppeln wenn man rennt, ausdauer als neues attribut neben energie usw
					//nur test, wird noch ausgebaut -> spieler erholt sich erst kurze zeit nachdem er gerannt ist
					if((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) && (player.getDexterity() > 0)){
						player.setMaxSpeed(0.4f);
						player.setDexterity(-0.5f);
//						System.out.println("dex: "+ player.getDexterity());
					}else{
						player.setMaxSpeed(0.2f);
					}
					player.slowDown();
					
					level.draw(player);
					player.draw();
					
					//Wenn der chunk gewechselt wurde mache folgendes:
					// items updaten, ..
					if(level.chunkChanged){
//						level.updateItems();
					}
					
					break;
					
				case MENU:
					Mouse.setGrabbed(false); //Mauscursor wieder da, alternativ könnte man auch einen eigenen cursor erstellen....
					level.draw(player);
					player.draw();
					menu.draw();
					if(Mouse.isButtonDown(0)){
						menu.changeState();
					}
					break;
			}
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		Mouse.destroy();
		System.exit(0);
	}
	
//	public void setDisplayMode(int width, int height, boolean fullscreen) {
//		 
//		// return if requested DisplayMode is already set
//		if ((Display.getDisplayMode().getWidth() == width) &&
//				(Display.getDisplayMode().getHeight() == height) &&
//				(Display.isFullscreen() == fullscreen)) {
//			return;
//		}
//		 
//		try {
//			DisplayMode targetDisplayMode = null;
//		 
//		if (fullscreen) {
//			DisplayMode[] modes = Display.getAvailableDisplayModes();
//			int freq = 0;
//		 
//			for (int i=0;i<modes.length;i++) {
//					DisplayMode current = modes[i];
//		 
//					if ((current.getWidth() == width) && (current.getHeight() == height)) {
//						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
//							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
//								targetDisplayMode = current;
//								freq = targetDisplayMode.getFrequency();
//							}
//						}
//		 
//						// if we've found a match for bpp and frequence against the
//						// original display mode then it's probably best to go for this one
//						// since it's most likely compatible with the monitor
//						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
//								(current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
//							targetDisplayMode = current;
//							break;
//						}
//					}
//			}
//		} else {
//			targetDisplayMode = new DisplayMode(width,height);
//		}
//		 
//		if (targetDisplayMode == null) {
//			System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
//			return;
//		}
//		 
//		Display.setDisplayMode(targetDisplayMode);
//		Display.setFullscreen(fullscreen);
//		 
//		} catch (LWJGLException e) {
//			System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
//		}
//		}
	public void initDisplay(){
		try {
			Display.setDisplayMode(new DisplayMode(640,480));
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
