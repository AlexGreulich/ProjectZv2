package Entities;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import org.lwjgl.input.Mouse;

import MainPack.World;
import MainPack.World.PlayerDirection;

/*
 * Der Spieler hat eine absolute position auf der Karte.
 * Er hat auch eine Position relativ zum Screen, diese ändert sich nur, wenn der Spieler an den Rand kommt.
*/

public class Player extends AbstractMovableEntity {
	
	float texposx, texposy;
	public float screenx;
	public float screeny;
	int changeState;
	public boolean isMoving;
	float cursorX = 0, cursorY = 0;
	float velocityX = 0f, velocityY = 0f;

	private PlayerDirection direction;
	
	public Player() {
		// x, y, width, height, file-name
        super(World.PLAYERSTARTPOSITIONX, World.PLAYERSTARTPOSITIONY, 32, 64, "charset.gif");//newcharset2_32-2erPot.gif
        this.texposx = 0;
        this.texposy = 0;
        this.changeState = 0;
        this.screenx = World.TILE_MIDDLE_X*32;
        this.screeny = World.TILE_MIDDLE_Y*32;
        isMoving = false;
        direction = PlayerDirection.DOWN;
	}

	@Override
	public void draw() {
		
		switch(direction){
		case UP:
			texposy = 3*0.125f;
			break;
		case DOWN:
			texposy = 0;
			break;
		case LEFT:
			texposy = 0.125f;
			break;
		case RIGHT:
			texposy = 2*0.125f;
			break;
		case LEFTUP:
			texposy = 6*0.125f;
			break;
		case LEFTDOWN:
			texposy = 4*0.125f;
			break;
		case RIGHTUP:
			texposy = 7*0.125f;
			break;
		case RIGHTDOWN:
			texposy = 5*0.125f;
			break;
		}
		
		// zeichne Spieler
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glBegin(GL_QUADS);
			glTexCoord2f(texposx,       texposy);			glVertex2f( screenx, 				screeny);		
			glTexCoord2f(texposx+0.0625f, texposy);			glVertex2f( screenx + (float)width, screeny);		
			glTexCoord2f(texposx+0.0625f, texposy+0.125f);	glVertex2f( screenx + (float)width, screeny + (float)height);	
			glTexCoord2f(texposx,       texposy+0.125f);	glVertex2f( screenx,				screeny + (float)height);
		glEnd();
		
		// zeichne Cursor
		
		glPushMatrix();
		glTranslatef(cursorX, cursorY, 0);
		glBegin(GL_QUADS);
		
			glTexCoord2f(0.5f, 0f);				glVertex2f(screenx+16, screeny+16);
			glTexCoord2f(0.53125f, 0f);			glVertex2f(screenx+26, screeny+16);
			glTexCoord2f(0.53125f, 0.03125f);	glVertex2f(screenx+26, screeny+ 26);
			glTexCoord2f(0.5f, 0.03125f);		glVertex2f(screenx+16, screeny+26);
		
		glEnd();
		glPopMatrix();
		glBindTexture(GL_TEXTURE_2D, 0);
		// spieler gehbewegung
		if (isMoving){
			changeState++;
			if (changeState == 10){
				texposx += 0.0625;
				changeState = 0;
				if(texposx>= 0.5){
					texposx = 0;
				}
			}
		}
	}

	public void calcDirection(){
		
		// berechne mouseposition abhängig von der bildschirmmitte
		double distX = 1.0 * Mouse.getX() - screenx ;									//0 bis 640 -> -320 bis +320
		double distY = -1.0 * Mouse.getY() + screeny;								//0 bis -640 -> -320 bis +320
		
		//für jeden viertelkreis/quadranten bestimme den winkel und passe die richtung an
		if ((distX > 0) && (distY > 0)){			//maus unten rechts
			double angle = Math.toDegrees(Math.atan((distX / distY)));
			System.out.println("angle: "+ (angle));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.RIGHT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.RIGHTDOWN;
			}else{
				direction = World.PlayerDirection.DOWN;
			}			
			cursorX = (float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = (float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
			
		} else if((distX > 0) && (distY < 0)){	//maus oben rechts
			double angle = Math.toDegrees(Math.atan((distX / distY*-1)));
			System.out.println("angle: "+ (angle));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.RIGHT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.RIGHTUP;
			}else{
				direction = World.PlayerDirection.UP;
			}			
			cursorX = (float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = -(float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
			
		} else if((distX < 0) && (distY > 0)){	//maus unten links
			double angle = Math.toDegrees(Math.atan((distX*-1 / distY)));
			System.out.println("angle: "+(angle));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.LEFT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.LEFTDOWN;
			}else{
				direction = World.PlayerDirection.DOWN;
			}			
			cursorX = -(float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = (float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
			
		} else if((distX < 0) && (distY < 0)){	//maus oben links
			double angle = Math.toDegrees(Math.atan((distX*-1 / distY*-1)));
			System.out.println("angle: "+ (angle));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.LEFT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.LEFTUP;
			}else{
				direction = World.PlayerDirection.UP;
			}
			cursorX = -(float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = -(float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
		}
	}
	
	@Override
	public void setX(float f) {
		//nicht über den rand laufen
		if (f <= 0){
			x = 0;
		} else if (f >= World.WORLDSIZE*32 - width ){
			x = (float) (World.WORLDSIZE*32 - width);
		} else {
			x = f;
		}
		
		// screenx ermitteln
		if (x < (World.TILES_ON_SCREEN_WIDTH/2*32)){ 	// links oben
			screenx = x;
		} else if (x > ((World.WORLDSIZE-World.CHUNK_BORDER)*32)){	// unten rechts
			System.out.println("change screenx");
			screenx = x - ((World.WORLDSIZE-World.TILES_ON_SCREEN_WIDTH)*32);
		} else {
			screenx = World.TILES_ON_SCREEN_WIDTH*32 / 2;	// standard
		}
	}

	@Override
	public void setY(float f) {
		//nicht über den rand laufen
		if (f <= 0){
			y = 0;
		} else if (f >= World.WORLDSIZE*32-height ){
			y = (float) (World.WORLDSIZE*32 - height);
		} else {
			y = f;
		}
		
		// screenx ermitteln
		if (y < (World.TILES_ON_SCREEN_HEIGHT/2*32)){ 	// links oben
			screeny = y;
		} else if (y > ((World.WORLDSIZE-World.CHUNK_BORDER)*32)){	// unten rechts
			System.out.println("change screeny");
			screeny = y - ((World.WORLDSIZE-World.TILES_ON_SCREEN_HEIGHT)*32);
		} else {
			screeny = World.TILES_ON_SCREEN_HEIGHT*32 / 2;	// standard
		}
	}
}
