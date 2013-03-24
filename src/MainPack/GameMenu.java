package MainPack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import MainPack.World.MenuOption;
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
	MenuOption option = World.MenuOption.MAINMENU;
	int posX =0, posY =0;
	
	public GameMenu(){
		try {
			menutex = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/menubackground.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		posX =  (Display.getWidth()/2)  - 320;// World..WIDTH /2
		posY = (Display.getHeight()/2)  - 320;
		
		newGame = new GameMenuItem(0, 0.625f, posX +192, posY + 160);//x 192, y 192 288 384 480
		options = new GameMenuItem(0, 0.6875f, posX + 192, posY + 256);
		credits = new GameMenuItem(0, 0.75f, posX + 192, posY + 352);
		exitGame = new GameMenuItem(0, 0.8125f, posX + 192, posY + 448);
		
	}
	
	public void draw(){
		glBindTexture(GL_TEXTURE_2D, menutex.getTextureID());
		
		glBegin(GL_QUADS);
			glTexCoord2f(0f,0f);			glVertex2f(posX,posY);//0,640
			glTexCoord2f(0.625f,0f);		glVertex2f(posX + 640,posY );
			glTexCoord2f(0.625f,0.625f);	glVertex2f(posX + 640,posY + 640);
			glTexCoord2f(0f,0.625f);		glVertex2f(posX,posY + 640);
		
		glEnd();
		
		switch(option){
		case MAINMENU:	//alle menupunkte zeichnen
			newGame.draw(Mouse.getX(), Mouse.getY());
			options.draw(Mouse.getX(), Mouse.getY());
			credits.draw(Mouse.getX(), Mouse.getY());
			exitGame.draw(Mouse.getX(), Mouse.getY());
			break;
		case OPTIONS:	//optionen zeigen
			options.setPosition(192, 192);
			options.draw(Mouse.getX(), Mouse.getY());
			break;
		case EXIT:		//überflöüssig, weil schon beendet, aber besser es erst drin zu lassen, evtl default einbauen,ka.....
			break;
		case CREDITS:	//credits zeigen
			credits.draw(Mouse.getX(), Mouse.getY());
			break;
		case NEWGAME:	//noch kA wie zu implementieren
			break;
			
		}
		
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void changeState(){
		if(newGame.isSelected){				//???
			
		}else if(options.isSelected){		//optionsbutton zeichnen und optionen anzeigen	TODO
			
		}else if(credits.isSelected){		//credits-button zeichnen und credits anzeigen  TODO
			
		}else if(exitGame.isSelected){		//sofort beenden
			Display.destroy();
			System.exit(0);
		}
	}
}
