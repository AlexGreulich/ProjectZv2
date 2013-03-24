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
		
		// gew�nschte Debug-Info hier hinzuf�gen
		font.drawString(x, y, "plaxer xy="+player.getX() +" | "+player.getY());
		font.drawString(x, y+20, "player speed: "+player.getSpeed());
		font.drawString(x, y+40, "player screenxy: "+player.getScreenx()+" | "+ player.getScreeny());
		
		font.drawString(x, y+80, "level screen delta: "+level.getScreenDeltaX() +" | "+ level.getScreenDeltaY());
		
		glPopMatrix();
	}
	
	private static void setUpFonts() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.black));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

}