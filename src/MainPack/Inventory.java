package MainPack;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import Entities.Player;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;


public class Inventory {
	public enum InvState{JUSTLOOKING, CRAFTING }
	InvState state;
	Item[] itemsInInv = new Item[12];
	
	int space = World.SCREENWIDTH/23;
	int itemsize = space*2;
		
	float drawX = 0f, drawY = 0f;
	float x1 = 0f;
	float x2 = 0f;
	float y1 = 0f;
	float y2 = 0f;
	
	int textboxX1;
	int textboxX2;
	int textboxY1;
	int textboxY2;
	
	private static UnicodeFont invFont;
	
	int selectedItemID=0;
	String itemDescription = null;
	
	public Inventory(){
		textboxX1 = Display.getWidth() / 4;
		textboxX2 = Display.getWidth() / 4 + Display.getWidth() / 2;
		textboxY1 = 5*Display.getHeight() / 8;
		textboxY2 = Display.getHeight() -100;
		
		state = InvState.JUSTLOOKING;
		setUpFont();
	}
	
	public void setSelectedItemID(int i){
		this.selectedItemID = i;
	}
	
	public boolean invIsFull(){
		boolean isFull = false;
		for(int i = 0; i< itemsInInv.length; i++){
			if(itemsInInv[i] != null){
				isFull = true;
			}
			else isFull = false;
		}
		return isFull;
	}
	
	public void pickUp(Item i){
		for(int index = 0; index < itemsInInv.length;index++){
			if(itemsInInv[index] == null){
				itemsInInv[index] = i;
				System.out.println("Item picked up.");
				break;
			}else{
				System.out.println("Itemslot already full");
			}
		}
	}
	
	private boolean checkmouse(float dx, float dy){
		int mX = Mouse.getX();
		int mY = Display.getHeight()-Mouse.getY();
//		System.out.println("mouseXY to check for: " + mX + " " +mY + " with DXY: "+ dx + " " +dy);
		System.out.println(" check if "+mX + " > " +dx + " & " + mX + " < " +(dx+ 2*space) + " & " + mY + " > " +dy+ " & " +  mY +" < " +(dy + 2*space));
		
			if((mX > dx) && (mX < dx + 2*space)){
				System.out.println("mouse clicked on itemX at:"+ mX+ ","+ mY);
				System.out.println("next check: "+mY +" > "+dy+")"+ " && "+ mY + " <"+ "("+(dy+ "+2*" +space));
				if((mY > dy) && (mY < dy+ 2*space )){
					System.out.println("mouse clicked on itemY at :"+ mX+ ","+ mY);
					System.out.println(" check true");
					return true;
				}else{
					System.out.println(" check false");
					return false;
				}
					
			}else{
				System.out.println(" check false");
				return false;
			}
	}
	public void drawItemText(String itemtext){
		glPushMatrix();
//		glLoadIdentity();
		System.out.println("draw '"+ itemtext +"' at: "+ (float)textboxX1 +10+" "+ (float)textboxY1 +10);
		invFont.drawString((float)textboxX1 +10, (float)textboxY1 +10, "Description: " + itemtext);
		
		glPopMatrix();
	}
	public void draw(ItemHandler ih){
		
		glLoadIdentity();
		glBegin(GL_QUADS);
			glTexCoord2f(0f,					52* World.FLOATINDEX);	glVertex2f(0,0);
			glTexCoord2f(16* World.FLOATINDEX,	52* World.FLOATINDEX);	glVertex2f(Display.getWidth(),0);
			glTexCoord2f(16* World.FLOATINDEX, 	64* World.FLOATINDEX);	glVertex2f(Display.getWidth(),Display.getHeight());
			glTexCoord2f(0f, 					64* World.FLOATINDEX);	glVertex2f(0,Display.getHeight());
		glEnd();
		
		glBegin(GL_QUADS);
			glTexCoord2f(27* World.FLOATINDEX, 0f);			glVertex2f(textboxX1,textboxY1);
			glTexCoord2f(28* World.FLOATINDEX, 0f);			glVertex2f(textboxX2,textboxY1);
			glTexCoord2f(28* World.FLOATINDEX,  World.FLOATINDEX);		glVertex2f(textboxX2,textboxY2);
			glTexCoord2f(27* World.FLOATINDEX,  World.FLOATINDEX);	glVertex2f(textboxX1,textboxY2);
			
		glEnd();
		drawItemText(ih.getItemDescription(selectedItemID));
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, World.TILESET.getTextureID());
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glBegin(GL_QUADS);
		
		switch(state){
		case JUSTLOOKING:
			
			break;
		case CRAFTING:
			
			break;
		}
		for(int i = 0; i< itemsInInv.length; i++){
			if(checkmouse(drawX, drawY)){
				if(itemsInInv[i] != null){
					setSelectedItemID(itemsInInv[i].getID());
					glLoadIdentity();
					System.out.println("selecteditemid set to " + selectedItemID);
				}
			}
			if(i<6){
				drawY = 2*space ;
				drawX = (3f * space) + (i * itemsize) + (i * space);
			}else{
				drawY = space*3 +itemsize;
				drawX = (3f * space) + ((i-6) * itemsize) + ((i-6) * space);
			}
			x1 = 0f;
			y1 = 49* World.FLOATINDEX;
			x2 = World.FLOATINDEX;
			y2 = 50* World.FLOATINDEX;
			
			glTexCoord2f(x1,y1);		glVertex2f(drawX,drawY);
			glTexCoord2f(x2,y1);		glVertex2f(drawX + 2* space, drawY);
			glTexCoord2f(x2,y2);		glVertex2f(drawX + 2* space, drawY + 2* space);	
			glTexCoord2f(x1,y2);		glVertex2f(drawX,drawY + 2* space);
			
			if(itemsInInv[i] != null){
				x1 = ih.getItemTexPosX(itemsInInv[i].getID());
				y1 = ih.getItemTexPosY(itemsInInv[i].getID());
				x2 = ih.getItemTexPosX(itemsInInv[i].getID()) +  World.FLOATINDEX;
				y2 = ih.getItemTexPosY(itemsInInv[i].getID()) +  World.FLOATINDEX;
				
				glTexCoord2f(x1,y1);		glVertex2f(drawX,drawY);
				glTexCoord2f(x2,y1);		glVertex2f(drawX + 2* space, drawY);
				glTexCoord2f(x2,y2);		glVertex2f(drawX + 2* space, drawY + 2* space);	
				glTexCoord2f(x1,y2);		glVertex2f(drawX,drawY + 2* space);
			}
			
			
		}
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D,0);
	}
	private void setUpFont() {
		try {
			Font awtFont = Font.createFont( Font.TRUETYPE_FONT, new FileInputStream("src/tilesets/TravelingTypewriter.ttf")).deriveFont(36f);// happydays=24f		Zombified	=48f	TravelingTypewriter =36
			invFont = new UnicodeFont(awtFont);
	        invFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
	        invFont.addAsciiGlyphs();
			invFont.loadGlyphs();
			
		} catch (FontFormatException e1) {e1.printStackTrace();} catch (IOException e1) {e1.printStackTrace();}catch (SlickException e) {e.printStackTrace(); }
	        
    }
}