package MainPack;

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
		textboxY1 = 3*Display.getHeight() / 4;
		textboxY2 = Display.getHeight() -100;
		
		state = InvState.JUSTLOOKING;
		setUpFont();
	}
	
	
	public void setSelectedItemID(int i){
		this.selectedItemID = i;
	}
	
	public void pickUp(Item i){
		for(int index = 0; index < itemsInInv.length;index++){
			if(itemsInInv[index] == null){
				itemsInInv[index] = i;
				System.out.println("Item picked up.");
				return;
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
			glTexCoord2f(19*(32f/2048), 0f);			glVertex2f(0,0);
			glTexCoord2f(27*(32f/2048), 0f);			glVertex2f(Display.getWidth(),0);
			glTexCoord2f(27*(32f/2048), 6*(32f/2048));	glVertex2f(Display.getWidth(),Display.getHeight());
			glTexCoord2f(19*(32f/2048), 6*(32f/2048));	glVertex2f(0,Display.getHeight());
		glEnd();
		
		glBegin(GL_QUADS);
			glTexCoord2f(27*(32f/2048), 0f);			glVertex2f(textboxX1,textboxY1);
			glTexCoord2f(28*(32f/2048), 0f);			glVertex2f(textboxX2,textboxY1);
			glTexCoord2f(28*(32f/2048), 0.03125f/2);		glVertex2f(textboxX2,textboxY2);
			glTexCoord2f(27*(32f/2048), 0.03125f/2);	glVertex2f(textboxX1,textboxY2);
			
		glEnd();
		drawItemText(ih.getItemDescription(selectedItemID));
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, World.TILESET.getTextureID());
		glBegin(GL_QUADS);
		
		switch(state){
		case JUSTLOOKING:
			
			break;
		case CRAFTING:
			
			break;
		}
		for(int i = 0; i< itemsInInv.length; i++){
			if(itemsInInv[i] == null){
				System.out.println("item in slot " + i + ": null");
				x1 = 27*(32f/2048);
				y1 = 0.03125f/2;
				x2 = 28*(32f/2048);
				y2 = 0.03125f;
			}else{
				System.out.println("itemID: " + itemsInInv[i].getID()+ " Itemname: "+ ih.getItemName(itemsInInv[i].getID()));
				
				x1 = ih.getItemTexPosX(itemsInInv[i].getID());
				y1 = ih.getItemTexPosY(itemsInInv[i].getID());
				x2 = ih.getItemTexPosX(itemsInInv[i].getID()) + 0.03125f/2;
				y2 = ih.getItemTexPosY(itemsInInv[i].getID()) + 0.03125f/2;
				
//				System.out.println("checkmouse at "+ drawX +" " + drawY);
				if(checkmouse(drawX, drawY)){
						setSelectedItemID(itemsInInv[i].getID());
						glLoadIdentity();
						System.out.println("selecteditemid set to " + selectedItemID);
				}
			}
			if(i<6){
				drawY = space;
				drawX = (3f * space) + (i * itemsize) + (i * space);
			}else{
				drawY = space*2 +itemsize;
				drawX = (3f * space) + ((i-6) * itemsize) + ((i-6) * space);
			}
			
			glTexCoord2f(x1,y1);		glVertex2f(drawX,drawY);
			glTexCoord2f(x2,y1);		glVertex2f(drawX + 2* space, drawY);
			glTexCoord2f(x2,y2);		glVertex2f(drawX + 2* space, drawY + 2* space);	
			glTexCoord2f(x1,y2);		glVertex2f(drawX,drawY + 2* space);
		
		}
		glEnd();
		
		glBindTexture(GL_TEXTURE_2D,0);
	}
	private static void setUpFont() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 18);
        invFont = new UnicodeFont(awtFont);
        invFont.getEffects().add(new ColorEffect(java.awt.Color.white));
        invFont.addAsciiGlyphs();
        try {
        	invFont.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}