package MainPack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class GameMenu {
	Texture menutex;
	GameMenuItem newGame, exitGame, credits,  options; 
	
	
	public GameMenu(){
		try {
			menutex = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/menubackground.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		newGame = new GameMenuItem(0, 0.625f, 192, 192);//0.1875f,0.1875f
//		options = new GameMenuItem(0,0,0,0);
//		credits = new GameMenuItem(0,0,0,0);
//		exitGame = new GameMenuItem(0,0,0,0);
		
	}
	
	public void draw(){
		glBindTexture(GL_TEXTURE_2D, menutex.getTextureID());
		
		glBegin(GL_QUADS);
			glTexCoord2f(0f,0f);			glVertex2f(0,0);
			glTexCoord2f(0.625f,0f);		glVertex2f(640,0);
			glTexCoord2f(0.625f,0.625f);	glVertex2f(640,640);
			glTexCoord2f(0f,0.625f);		glVertex2f(0,640);
		
		glEnd();
		
		newGame.draw(Mouse.getX(), Mouse.getY());
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
