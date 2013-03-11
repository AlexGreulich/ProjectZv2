package Entities;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import MainPack.World;


public class Player extends AbstractMovableEntity {
	
	float texposx, texposy;
	public float screenx;
	public float screeny;
	int changeState;
	public boolean isMoving;

	public Player() {
		// x, y, width, height, file-name
        super(World.PLAYERSTARTPOSITIONX, World.PLAYERSTARTPOSITIONY, 32, 64, "newcharset2_32-2erPot.gif");
        this.texposx = 0;
        this.texposy = 0.125f+ 0.125f;
        this.changeState = 0;
        this.screenx = World.TILE_MIDDLE_X;
        this.screeny = World.TILE_MIDDLE_Y;
        isMoving = false;
	}

	@Override
	public void draw() {
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		
		glBegin(GL_QUADS);
		// 128 x 512	
			glTexCoord2f(texposx,       texposy);			glVertex2f( screenx, screeny);		
			glTexCoord2f(texposx+0.25f, texposy);			glVertex2f( screenx + (float)width, screeny);		
			glTexCoord2f(texposx+0.25f, texposy+0.125f);	glVertex2f( screenx + (float)width, screeny + (float)height);	
			glTexCoord2f(texposx,       texposy+0.125f);	glVertex2f( screenx, screeny + (float)height);
		glEnd();
		
		if (isMoving){
			changeState++;
			if (changeState == 10){
				texposx += 0.25;
				changeState = 0;
				if(texposx>= 1){
					texposx = 0;
				}
			}
		}
	}

	@Override
	public void setX(int x) {
		if (x < (World.TILES_ON_SCREEN_WIDTH/2*32)){
			screenx = (World.TILES_ON_SCREEN_WIDTH*32 / 2) + x-World.TILES_ON_SCREEN_WIDTH*32/2;
		} else {
			screenx = World.TILES_ON_SCREEN_WIDTH*32 / 2;
		}
		//nicht über den rand laufen
		if (x <= 0){
			this.x = 0;
		} else if (x >= World.WORLDSIZE*32 ){
			this.x = (int) (World.WORLDSIZE*32 - width);
		} else {
			this.x = x;
		}
	}

	@Override
	public void setY(int y) {
		if (y < (World.TILES_ON_SCREEN_HEIGHT/2*32)){
			screeny = (World.TILES_ON_SCREEN_HEIGHT*32 / 2) + y-World.TILES_ON_SCREEN_HEIGHT*32/2;
		} else {
			screeny = World.TILES_ON_SCREEN_HEIGHT*32 / 2;
		}
		//nicht über den rand laufen
		if (y < 1){
			this.y = 1;
		} else if (y >= World.WORLDSIZE*32 ){
			this.y = (int) (World.WORLDSIZE*32 - height);
		} else {
			this.y = y;
		}
	}
	
}
