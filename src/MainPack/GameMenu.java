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
	
	public GameMenu(){
		try {
			menutex = TextureLoader.getTexture("PNG", new FileInputStream("src/tilesets/menubackground.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		newGame = new GameMenuItem(32* World.FLOATINDEX, 52* World.FLOATINDEX, Display.getWidth()/2 - 256, Display.getHeight()/2 - 128);//0.1875f,0.1875f, World.MenuOption.NEWGAME, World.MenuOption.OPTIONS, World.MenuOption.CREDITS, World.MenuOption.EXIT
		options = new GameMenuItem(32* World.FLOATINDEX, 54* World.FLOATINDEX, Display.getWidth()/2 - 256, Display.getHeight()/2 );
		credits = new GameMenuItem(32* World.FLOATINDEX, 56* World.FLOATINDEX, Display.getWidth()/2 - 256, Display.getHeight()/2 + 128);
		exitGame = new GameMenuItem(32* World.FLOATINDEX, 58* World.FLOATINDEX, Display.getWidth()/2 - 256, Display.getHeight()/2 + 256);
		
	}
	
	public void draw(){
//		glBindTexture(GL_TEXTURE_2D, menutex.getTextureID());
		glBindTexture(GL_TEXTURE_2D, World.TILESET.getTextureID());
		glBegin(GL_QUADS);
			glTexCoord2f(16* World.FLOATINDEX, 52*  World.FLOATINDEX);	glVertex2f(0,0);
			glTexCoord2f(32* World.FLOATINDEX, 52*  World.FLOATINDEX);	glVertex2f(Display.getWidth(),0);
			glTexCoord2f(32* World.FLOATINDEX, 64* World.FLOATINDEX);	glVertex2f(Display.getWidth(),Display.getHeight());
			glTexCoord2f(16* World.FLOATINDEX, 64* World.FLOATINDEX);	glVertex2f(0,Display.getHeight());
		
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
