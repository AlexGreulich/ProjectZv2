package Entities;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

//import MainPack.Item;
import MainPack.World;
import MainPack.World.PlayerDirection;

/*
 * Der Spieler hat eine absolute position auf der Karte.
 * Er hat auch eine Position relativ zum Screen, diese �ndert sich nur, wenn der Spieler an den Rand kommt.
*/

public class Player extends AbstractMovableEntity {
	
	public float lifeEnergy =100f, dexterity = 100f, hunger = 100f, thirst = 100f;
	
	float speed;
	float maxSpeed, minSpeed;
	float texposx, texposy;
	public float screenx;
	public float screeny;
	int changeState;
	public boolean isMoving;
	float cursorX = 0, cursorY = 0;
	float velocityX = 0f, velocityY = 0f;
	float calcSpeed;
	double distX, distY;

//	List<Item> inventory = new ArrayList<Item>();
	public PlayerDirection direction;
	
	public Player() {
		// x, y, width, height, file-name
        super(World.PLAYERSTARTPOSITIONX, World.PLAYERSTARTPOSITIONY, 32, 64, "charset.gif");//newcharset2_32-2erPot.gif
        this.texposx = 0;
        this.texposy = 0;
        this.changeState = 0;
        this.screenx = World.TILE_MIDDLE_X *32;
        this.screeny = World.TILE_MIDDLE_Y *32;
        speed = 0f;
    	maxSpeed = 0.2f;
    	minSpeed = -0.2f;
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
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//        
		glBegin(GL_QUADS);
			glTexCoord2f(texposx,     	  texposy);			glVertex2f( screenx, 				screeny);		
			glTexCoord2f(texposx+0.0625f, texposy);			glVertex2f( screenx + (float)width, screeny);		
			glTexCoord2f(texposx+0.0625f, texposy+0.125f);	glVertex2f( screenx + (float)width, screeny + (float)height);	
			glTexCoord2f(texposx,     	  texposy+0.125f);	glVertex2f( screenx,				screeny + (float)height );
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
		
		drawStats();
		
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
		}else{				// spieler erholt sich wenn er mal gerannt ist oder so, wird evtl noch komplexer und dann woanders hin, testweise aber hier
			if(getDexterity() < 100){
				setDexterity(0.2f);
			}
		}
	}
	
	public void drawStats(){
		//background
		glBegin(GL_QUADS);
			glColor4f(0f, 0f, 0f, 0.5f);
			glVertex2f(0, screeny + 180);
			glVertex2f(640, screeny +180);
			glVertex2f(640, screeny + 320);
			glVertex2f(0, screeny + 320);
		glEnd();
		
		//healthbar
		glBegin(GL_QUADS);
			glColor3f(0f, 0f, 0f);
			glVertex2f(screenx -202, screeny + 198);
			glVertex2f(screenx +2, screeny +198);
			glVertex2f(screenx +2, screeny +222);
			glVertex2f(screenx -202, screeny + 222);
		glEnd();
		glBegin(GL_QUADS);
			glColor3f(1f, 0f, 0f);
			glVertex2f(screenx - 200, screeny + 200);
			glVertex2f(screenx - 200 + lifeEnergy*2, screeny + 200);
			glVertex2f(screenx - 200 + lifeEnergy*2, screeny + 200 +20);
			glVertex2f(screenx - 200, screeny + 200 + 20);
		glEnd();
		
		//energybar
		glBegin(GL_QUADS);
			glColor3f(0f, 0f, 0f);
			glVertex2f(screenx -202, screeny +228);
			glVertex2f(screenx +2, screeny +228);
			glVertex2f(screenx +2, screeny +252);
			glVertex2f(screenx -202, screeny +252);
		glEnd();
		glBegin(GL_QUADS);
			glColor3f(0f, 0f, 1f);
			glVertex2f(screenx - 200, screeny + 230);
			glVertex2f(screenx - 200 + dexterity*2, screeny + 230);
			glVertex2f(screenx - 200 + dexterity*2, screeny + 230 +20);
			glVertex2f(screenx - 200, screeny + 230 + 20);
		glEnd();
		
		//hungerbar
		glBegin(GL_QUADS);
			glColor3f(0f, 1f, 0f);
			glVertex2f(screenx - 200, screeny + 260);
			glVertex2f(screenx - 200 + hunger*0.9f, screeny + 260);
			glVertex2f(screenx - 200 + hunger*0.9f, screeny + 260 +10);
			glVertex2f(screenx - 200, screeny + 260 + 10);
		glEnd();
		
		//thirstbar
		glBegin(GL_QUADS);
			glColor3f(0f, 1f, 0f);
			glVertex2f(screenx - 200, screeny + 280);
			glVertex2f(screenx - 200 + thirst *0.9f, screeny + 280);
			glVertex2f(screenx - 200 + thirst *0.9f, screeny + 280 +10);
			glVertex2f(screenx - 200, screeny + 280 + 10);
		glEnd();
		
		glColor3f(1f, 1f, 1f);//farbe zur�cksetzen
	}
	
	public float getDexterity(){
		return this.dexterity;
	}
	public void setDexterity(float dext){
		this.dexterity += dext;
	}
	
	
	public void calcDirection(float delta){
		calcSpeed = speed*delta;
				
		// berechne mouseposition abh�ngig von der position des Spielers auf dem Screen
		if (Mouse.getX() < screenx){
			distX = -(screenx - Mouse.getX());
		} else {
			distX = Mouse.getX() - screenx;
		}
		if (World.TILES_ON_SCREEN_HEIGHT*32-Mouse.getY() < screeny){
			distY = -(screeny - (World.HEIGHT -Mouse.getY()));
		} else {
			distY = World.HEIGHT-Mouse.getY() - screeny;
		}
		System.out.println("screenx:"+ screenx + "   screeny:" + screeny+ "   distX:"+distX+"   distY:"+distY+ "   MouseX:"+Mouse.getX()+ "   MouseY:"+Mouse.getY());
		
		//f�r jeden viertelkreis/quadranten bestimme den winkel und passe die richtung an
		if ((distX > 0) && (distY > 0)){			//maus unten rechts
			double angle = Math.toDegrees(Math.atan((distX / distY)));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.RIGHT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.RIGHTDOWN;
			}else{
				direction = World.PlayerDirection.DOWN;
			}			
			cursorX = (float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = (float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
		} 
		else if((distX > 0) && (distY < 0)){	//maus oben rechts
			double angle = Math.toDegrees(Math.atan((distX / distY*-1)));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.RIGHT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.RIGHTUP;
			}else{
				direction = World.PlayerDirection.UP;
			}			
			cursorX = (float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = -(float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
		} 
		else if((distX < 0) && (distY > 0)){	//maus unten links
			double angle = Math.toDegrees(Math.atan((distX*-1 / distY)));
			
			if(angle > 67.5){
				direction = World.PlayerDirection.LEFT;
			}else if((angle <= 67.5) && (angle >= 22.5)){
				direction = World.PlayerDirection.LEFTDOWN;
			}else{
				direction = World.PlayerDirection.DOWN;
			}			
			cursorX = -(float) (World.playerMouseRadius * Math.sin(Math.toRadians(angle)));
			cursorY = (float) (World.playerMouseRadius * Math.cos(Math.toRadians(angle)));
		} 
		else if((distX < 0) && (distY < 0)){	//maus oben links
			double angle = Math.toDegrees(Math.atan((distX*-1 / distY*-1)));
			
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

		setX(x + calcSpeed * (cursorX/64)) ;
		setY(y + calcSpeed * (cursorY/64));
	}
	
	@Override
	public void setX(float f) {
		//nicht �ber den rand laufen
		if (f <= 0){
			x = 0;
		} else if (f >= World.WORLDSIZE*32 - width ){
			x = (float) (World.WORLDSIZE*32 - width);
		} else {
			x = f;
		}
		
		// screenx ermitteln
		if (x < (World.WIDTH/2)){ 	// links oben
			screenx = x -16;
		} else if (x > ((World.WORLDSIZE - World.CHUNK_BORDER)*32)){	// unten rechts
			System.out.println("change screenx");
			screenx = x - ((World.WORLDSIZE*32 - World.WIDTH)) -16;
		} else {
			screenx = (World.WIDTH / 2) -16;	// standard
		}
	}

	/*.TILES_ON_SCREEN_WIDTH*32
	 * -World.CHUNK_BORDER
	 * 
	 * 
	 * 
	 * */
	
	@Override
	public void setY(float f) {
		//nicht �ber den rand laufen
		if (f <= 0){
			y = 0;
		} else if (f >= World.WORLDSIZE*32-height ){
			y = (float) (World.WORLDSIZE*32 - height);
		} else {
			y = f;
		}
		
		// screenx ermitteln
		if (y < (World.HEIGHT/2)){ 	// links oben
			screeny = y - 128;
		} else if (y > ((World.WORLDSIZE  - World.CHUNK_BORDER)*32)){	// unten rechts
			System.out.println("change screeny");
			screeny = y - ((World.WORLDSIZE*32 - World.HEIGHT)) - 128;
		} else {
			screeny = World.HEIGHT / 2 -128;	// standard
		}
	}
	
	
	public void changeSpeed(float f) {
		speed += f;	
		checkMovementAndSpeed();
	}
	
	public void slowDown() {
		speed*=0.9f;	
		checkMovementAndSpeed();
	}
	
	private void checkMovementAndSpeed() {
		// Speed
		if(speed > maxSpeed){
			speed = maxSpeed;
		}
		if(speed < minSpeed){ // quasi minSpeed
			speed = minSpeed;
		}
		// Movement
		if ((speed < -0.0025f) || (speed > 0.0025f) ){
			isMoving = true;
		} else {
			speed = 0;
			isMoving = false;
		}
	}

	public void setMaxSpeed(float f){
		maxSpeed = f;
	}

	public float getSpeed() {
		return speed;
	}
	
	public float getScreenx() {
		return screenx;
	}

	public float getScreeny() {
		return screeny;
	}
}
