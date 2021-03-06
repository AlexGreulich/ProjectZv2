package MainPack;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.Rectangle;
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
	
	int space = World.SCREENWIDTH/33;
	int itemsize = space*2;
		
	float drawX = 0f, drawY = 0f, drawX2 = 0f, drawY2 = 0f;
	float x1 = 0f;
	float x2 = 0f;
	float y1 = 0f;
	float y2 = 0f;
	
	float useColorIndexY;
	float useColorIndexY2;
	
	int itemInUseSlotX;
	int itemInUseSlotY;
	float itemInUseTX = 0f, itemInUseTY = 0f;
	
	Rectangle itemInUseSlotRec;
	
	int textboxX1;
	int textboxX2;
	int textboxY1;
	int textboxY2;
	
	private static UnicodeFont invFont;
	
	int selectedItemID=0;
	
	Item selectedItem = null;
	boolean anItemIsSelected = false;
	String itemDescription = null;
	
	public Inventory(){
		textboxX1 = Display.getWidth() / 4;
		textboxX2 = Display.getWidth() / 4 + Display.getWidth() / 2;
		textboxY1 = 5*Display.getHeight() / 8;
		textboxY2 = Display.getHeight() -100;
		
		itemInUseSlotX = textboxX2 + space;
		itemInUseSlotY = textboxY1 ;
		itemInUseSlotRec = new Rectangle(itemInUseSlotX, itemInUseSlotY, 3* space, 3*space);
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
//		System.out.println(" check if "+mX + " > " +dx + " & " + mX + " < " +(dx+ 2*space) + " & " + mY + " > " +dy+ " & " +  mY +" < " +(dy + 2*space));
		
			if((mX > dx) && (mX < dx + 2*space)){
//				System.out.println("mouse clicked on itemX at:"+ mX+ ","+ mY);
//				System.out.println("next check: "+mY +" > "+dy+")"+ " && "+ mY + " <"+ "("+(dy+ "+2*" +space));
				if((mY > dy) && (mY < dy+ 2*space )){
//					System.out.println("mouse clicked on itemY at :"+ mX+ ","+ mY);
//					System.out.println(" check true");
					return true;
				}else{
//					System.out.println(" check false");
					return false;
				}
					
			}else{
//				System.out.println(" check false");
				return false;
			}
	}
	public void drawItemText(String itemtext){
		invFont.drawString((float)textboxX1 +10, (float)textboxY1 +10, "Description: " + itemtext);
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
			
			if(i<6){
				drawX = (3f * space) + (i * itemsize) + (i * space);
				drawY = 2*space ;
				drawX2 = drawX + 2*space;
				drawY2 = drawY + 2*space;
			}else{
				drawX = (3f * space) + ((i-6) * itemsize) + ((i-6) * space);
				drawY = space*3 +itemsize;
				drawX2 = drawX + 2*space;
				drawY2 = drawY + 2*space;
			}
			
			if(checkmouse(drawX, drawY)){		
				if(itemsInInv[i] != null){
					if(!anItemIsSelected){
						setSelectedItemID(itemsInInv[i].getID());
						if(Mouse.isButtonDown(0)){
							selectedItem = itemsInInv[i];
							itemsInInv[i] = null; 
							anItemIsSelected =true;
						}
					}
				}
			}
			x1 = 0f;
			y1 = 49* World.FLOATINDEX;
			x2 = World.FLOATINDEX;
			y2 = 50* World.FLOATINDEX;
			
			glTexCoord2f(x1,y1);		glVertex2f(drawX,drawY);
			glTexCoord2f(x2,y1);		glVertex2f(drawX2, drawY);
			glTexCoord2f(x2,y2);		glVertex2f(drawX2, drawY2);	
			glTexCoord2f(x1,y2);		glVertex2f(drawX,drawY2);
			
			if(itemsInInv[i] != null){
				x1 = ih.getItemTexPosX(itemsInInv[i].getID());
				y1 = ih.getItemTexPosY(itemsInInv[i].getID());
				x2 = ih.getItemTexPosX(itemsInInv[i].getID()) +  World.FLOATINDEX;
				y2 = ih.getItemTexPosY(itemsInInv[i].getID()) +  World.FLOATINDEX;
				
				glTexCoord2f(x1,y1);		glVertex2f(drawX,drawY);
				glTexCoord2f(x2,y1);		glVertex2f(drawX2, drawY);
				glTexCoord2f(x2,y2);		glVertex2f(drawX2, drawY2);	
				glTexCoord2f(x1,y2);		glVertex2f(drawX, drawY2);
				
				if(itemsInInv[i].isLimited){
					System.out.println("Item's usability at slot "+ i + " is limited to "+ ih.getItemUses(itemsInInv[i].getID()) + " uses.");
					int times = ih.getItemUses(itemsInInv[i].getID());
					
					int a = (2*space)/ times;
					
					for(int t =0; t< times; t++){
						if(t < itemsInInv[i].uses){
							useColorIndexY = 51* World.FLOATINDEX;
							useColorIndexY2 =52 * World.FLOATINDEX;
						}else{
							useColorIndexY = 48 *World.FLOATINDEX;
							useColorIndexY2 = 49* World.FLOATINDEX;
						}
						glTexCoord2f(0f, useColorIndexY );					glVertex2f(drawX2 + 3,drawY2 -(t * a));
						glTexCoord2f(0f, useColorIndexY2);					glVertex2f(drawX2 + 13, drawY2 -(t * a));
						glTexCoord2f(World.FLOATINDEX, useColorIndexY2);	glVertex2f(drawX2 + 13, drawY2 -(t * a) - (a-2));	
						glTexCoord2f( World.FLOATINDEX,useColorIndexY);		glVertex2f(drawX2 + 3,drawY2 -(t * a) - (a-2));
					}
				}
			}
		}
		itemInUseTX = 0f;
		itemInUseTY = 49* World.FLOATINDEX;
		
		glTexCoord2f(itemInUseTX, itemInUseTY );										glVertex2f(itemInUseSlotX, itemInUseSlotY);
		glTexCoord2f(itemInUseTX, itemInUseTY + World.FLOATINDEX);						glVertex2f(itemInUseSlotX, itemInUseSlotY + 3*space);
		glTexCoord2f(itemInUseTX + World.FLOATINDEX, itemInUseTY + World.FLOATINDEX);	glVertex2f(itemInUseSlotX + 3*space, itemInUseSlotY + 3*space);	
		glTexCoord2f(itemInUseTX + World.FLOATINDEX, itemInUseTY);						glVertex2f(itemInUseSlotX + 3*space, itemInUseSlotY);
		
		if(ih.itemInUse != null){
			itemInUseTX = ih.getItemTexPosX(ih.itemInUse.getID());
			itemInUseTY = ih.getItemTexPosY(ih.itemInUse.getID());
			
			glTexCoord2f(itemInUseTX, itemInUseTY );										glVertex2f(itemInUseSlotX, itemInUseSlotY);
			glTexCoord2f(itemInUseTX, itemInUseTY + World.FLOATINDEX);						glVertex2f(itemInUseSlotX, itemInUseSlotY + 3*space);
			glTexCoord2f(itemInUseTX + World.FLOATINDEX, itemInUseTY + World.FLOATINDEX);	glVertex2f(itemInUseSlotX + 3*space, itemInUseSlotY + 3*space);	
			glTexCoord2f(itemInUseTX + World.FLOATINDEX, itemInUseTY);						glVertex2f(itemInUseSlotX + 3*space, itemInUseSlotY);
		}
		
		if(selectedItem != null){
			glTexCoord2f(ih.getItemTexPosX(selectedItem.getID()), ih.getItemTexPosY(selectedItem.getID()));												glVertex2f(Mouse.getX(), World.SCREENHEIGHT - Mouse.getY());
			glTexCoord2f(ih.getItemTexPosX(selectedItem.getID()), ih.getItemTexPosY(selectedItem.getID()) + World.FLOATINDEX );							glVertex2f(Mouse.getX(), World.SCREENHEIGHT - Mouse.getY() + 2*space);
			glTexCoord2f(ih.getItemTexPosX(selectedItem.getID()) + World.FLOATINDEX, ih.getItemTexPosY(selectedItem.getID()) + World.FLOATINDEX );		glVertex2f(Mouse.getX() + 2*space, 	World.SCREENHEIGHT - Mouse.getY() + 2*space);	
			glTexCoord2f(ih.getItemTexPosX(selectedItem.getID()) + World.FLOATINDEX, ih.getItemTexPosY(selectedItem.getID()));							glVertex2f(Mouse.getX() + 2*space,	World.SCREENHEIGHT - Mouse.getY());
		}
		
		glEnd();
		
		if((!Mouse.isButtonDown(0)) && (selectedItem  != null)){
			Rectangle releasedItemRec = new Rectangle(Mouse.getX(),World.SCREENHEIGHT - Mouse.getY(),3* space, 3*space);
			Rectangle itemsRec = new Rectangle(3*space, 2* space, 21*space, 5*space);
			if(releasedItemRec.intersects(itemInUseSlotRec)){
				if(ih.itemInUse != null){
					insertIntoInventory(ih.itemInUse);
				}
				ih.setItemInUse(selectedItem);
			}else if(releasedItemRec.intersects(itemsRec)){
				insertIntoInventory(selectedItem);
			}
			// weltkoordinaten holen irgendwie, um item an der spielerstelle zu platzieren, sonst halt item zerstören/wegwerfen..
//			else{
//				ih.totalItems.put(new Point(),  selectedItem);
//			}
			selectedItem = null;
			anItemIsSelected = false;
		}
		if(checkmouse(itemInUseSlotX, itemInUseSlotY)){
			if(ih.itemInUse != null){
				setSelectedItemID(ih.itemInUse.getID());
				if(Mouse.isButtonDown(0) && (selectedItem ==null)){
					selectedItem = ih.itemInUse;
					ih.itemInUse = null;
				}
			}
		}
		glBindTexture(GL_TEXTURE_2D,0);
	}
	
	public void insertIntoInventory(Item item){
		for(int i = 0; i< itemsInInv.length; i++){
			if(itemsInInv[i] == null){
				itemsInInv[i] = item;
				break;
			}
		}
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