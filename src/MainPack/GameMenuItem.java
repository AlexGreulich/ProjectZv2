package MainPack;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.opengl.Display;

import MainPack.World.MenuOption;



public class GameMenuItem {

	private float texPosX = 0, texPosY = 0;
	private float scrPosX = 0, scrPosY = 0;
	public boolean isSelected = false;
	private int shiftX = 1;
	
	
	public GameMenuItem(float xt, float yt, float xs, float ys){
		texPosX = xt;
		texPosY = yt;
		scrPosX = xs;
		scrPosY = ys;
		
	}
	
	public void setPosition(int x, int y){
		this.scrPosX = x;
		this.scrPosY = y;
	}
	
	public void draw(int mX, int mY){
//		System.out.println("my: "+ mY);
		mY = Display.getHeight()-mY;
		
		if((mX > scrPosX) && (mX < (scrPosX + 320)) && (mY > scrPosY) && (mY <( scrPosY + 64))){
			isSelected = true;
		}else{
			isSelected = false;
		}
		
		if(isSelected){
			shiftX *=2;
			if(shiftX >= 64){
				shiftX = 64;
			}
			glBegin(GL_QUADS);
				glTexCoord2f(0.25f, 0.625f);						glVertex2f(scrPosX - shiftX, scrPosY);
				glTexCoord2f(0.25f + 0.0625f, 0.625f);				glVertex2f(scrPosX - shiftX+ 64, scrPosY);
				glTexCoord2f(0.25f + 0.0625f, 0.625f + 0.0625f);	glVertex2f(scrPosX - shiftX+ 64, scrPosY + 64);
				glTexCoord2f(0.25f, 0.625f + 0.0625f);				glVertex2f(scrPosX - shiftX, scrPosY + 64);
			glEnd();
		}else{
			if(shiftX != 1){
				shiftX /= 2;
			}
		}
		
		glBegin(GL_QUADS);
			glTexCoord2f(texPosX, texPosY);					glVertex2f(scrPosX + shiftX, scrPosY);
			glTexCoord2f(texPosX+ 0.25f, texPosY);			glVertex2f(scrPosX + shiftX + 512, scrPosY);
			glTexCoord2f(texPosX+ 0.25f, texPosY+ 0.0625f);	glVertex2f(scrPosX + shiftX + 512, scrPosY + 128);
			glTexCoord2f(texPosX, texPosY + 0.0625f);		glVertex2f(scrPosX + shiftX, scrPosY + 128);
		glEnd();
	}
}
