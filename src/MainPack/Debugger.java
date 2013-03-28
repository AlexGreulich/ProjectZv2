package MainPack;

import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import Entities.Player;

public class Debugger {

	public static float x, y;
	private static UnicodeFont font;
	private Player player;
	private Level level;
	
	public Debugger(Player player, Level level){
		x = 20f;
		y = 20f;
		this.player = player;
		this.level = level;
		setUpFonts();
	}
	
	
	public void draw(){
		glPushMatrix();
		glLoadIdentity();
		
		// gewünschte Debug-Info hier hinzufügen
		font.drawString(x, y, "plaxer xy: "+player.getX() +" | "+player.getY());
		font.drawString(x, y+20, "player speed: "+player.getSpeed());
		font.drawString(x, y+40, "player screenxy: "+player.getScreenx()+" | "+ player.getScreeny());
		
		font.drawString(x, y+80, "level screendeltaxy: "+level.getScreenDeltaX() +" | "+ level.getScreenDeltaY());
		
		font.drawString(x,y+120,"World.TILES_ON_SCREEN_WIDTH/HEIGHT: " + World.TILES_ON_SCREEN_WIDTH + " | " + World.TILES_ON_SCREEN_HEIGHT);
		font.drawString(x,y+140,"World.TILES_ON_SCREEN_LR/TB: " + World.CHUNK_BORDER_LR + " | " + World.CHUNK_BORDER_TB);

		font.drawString(x, y+160,"items in chunk[]"+level.itemHandler.itemsInChunk.length);
		glPopMatrix();
		
	}
	
	private static void setUpFonts() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.blue));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}
