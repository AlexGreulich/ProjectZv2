package MainPack;

import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import MainPack.World.ItemType;



public class ItemHandler {

	Item[][] itemsInChunk = new Item[World.CHUNK_SIZE + 2*World.CHUNK_BORDER_LR][World.CHUNK_SIZE + 2*World.CHUNK_BORDER_TB];
	
	Map<Integer, String> itemDescriptions = new HashMap<Integer, String>();
	Map<Integer, String> itemNames = new HashMap<Integer, String>();
	Map<Integer, Integer> energyValues = new HashMap<Integer, Integer>();
	Map<Integer, Integer> damageValues = new HashMap<Integer, Integer>();
	Map<Integer,Integer> dexterityValues = new HashMap<Integer, Integer>();
	Map<Integer, String> healthValues = new HashMap<Integer, String>();
	Map<Integer, Float> texPosXValues = new HashMap<Integer, Float>();
	Map<Integer, Float> texPosYValues = new HashMap<Integer, Float>();
	Map<Integer, Boolean> isUsableMap = new HashMap<Integer, Boolean>();
	Map<Integer, Integer> uses = new HashMap<Integer, Integer>();
	
	Map<Point, Item> totalItems = new HashMap<Point, Item>();
	
	BufferedImage itemmap;
	Color tempitemcolor;
	
	File itemXML;
	
	Document itemList;
	int chunkLeftCornerX, chunkLeftCornerY, chunkBorderRight, chunkBorderBottom;
	
	int tempID=0;
	
	public ItemHandler(){
		/*
		 * Items spawnen in/ um Häuser und an anderen (angemessenen) Stellen.  
		 * - Sie haben eine x,y- koordinate und eine dx,dy-k., um versetzt zu zeichnen.
		 * - unterscheiden sich durch ItemType-> waffe, tool etc...
		 * - 
		 * 
		 * Der itemhandler hat eine Liste mit objekten, die beim erstellen des momentanen grids aktualisiert wird.
		 * Befindet sich der spieler auf einem item und klickt *aufheben wird das item aus der liste entfernt und im nächstfreien itemslot des spielers platziert.
		 * 
		 * TODO: bzw TOTHINKABOUT: wenn zb eine pistole aufgehoben wurde, sollte sie beim nächsten betreten des chunks nicht wieder auftauchen
		 * 
		 * Die items im spiel werden in Level gezeichnet.
		 * 
		 * Es muss jeden framewechsel geschaut werden ob der spieler auf einem item steht.  
		 * - liste iterieren?
		 * - hashmap mit koordinaten als schlüssel?
		 * - 
		 * 
		 * item hat ID: werte wie munition, healthpunkte, anzahl benutzungen und sowas werden als vektor gespeichert -> überlegen was man tatsächlich braucht
		 * in einer map werden <id, vektor> gespeichert, so kann man die werte einfach im itemhandler nachschlagen statt alles im item zu speichern
		 * 
		 * ItemDaten
		 * 
		 * */
		
		
		try {
			itemXML = new File("src/tilesets/items.xml");
			SAXBuilder builder = new SAXBuilder();
			itemList = (Document) builder.build(itemXML);
			Element xmlitems = itemList.getRootElement();
			
			List<Element> itemlist = xmlitems.getChildren();
			System.out.println(""+itemlist.size());
			
			for(int i = 0; i < itemlist.size(); i++){
				int id = Integer.parseInt(itemlist.get(i).getChildText("id"));
				
				System.out.println("Ich bin ein " + itemlist.get(i).getChildText("name"));
				System.out.println("... meine Beschreibung: "+ itemlist.get(i).getChildText("description"));
				System.out.println("und meine ID: " + itemlist.get(i).getChildText("id"));
				
				itemNames.put(id,itemlist.get(i).getChildText("name"));
				itemDescriptions.put(id, itemlist.get(i).getChildText("description"));
				
				boolean limiteduse = false;
				if(itemlist.get(i).getChildText("limitedUse").equals("true")){
					limiteduse = true;
					int timestouse = Integer.parseInt(itemlist.get(i).getChildText("uses"));
					uses.put(id, timestouse);
				}
				isUsableMap.put(id, limiteduse);
				
				float tX =Float.parseFloat(itemlist.get(i).getChildText("texcoordsX"));
				float tY =Float.parseFloat(itemlist.get(i).getChildText("texcoordsY"));
				texPosXValues.put(id,tX);
				texPosYValues.put(id,tY);
				
				switch(itemlist.get(i).getName()){
				case "tool":
					
					break;
				case "weapon":
					
					damageValues.put(id, Integer.parseInt(itemlist.get(i).getChildText("damage")));
					dexterityValues.put(id, Integer.parseInt(itemlist.get(i).getChildText("dexterityLoss")) );
					
					break;
				case "medic":
//					
					healthValues.put(id, itemlist.get(i).getChildText("healthGain"));
					
					break;
				case "collectable":
//					
					break;
				case "other":
//					
					break;
				}
			}
			itemDescriptions.put(0, "null Item");
			texPosXValues.put(0, 0.84375f);
			texPosYValues.put(0,0f);
		} catch (JDOMException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}	
	}
	
	public String getItemName(int id){
		return itemNames.get(id);
	}
	public String getItemDescription(int id){
		return itemDescriptions.get(id);
	}
	public int getItemDamage(int id){
		return damageValues.get(id);
	}
	public String getItemHealthGain(int id){
		return healthValues.get(id);
	}
	public float getItemTexPosX(int id){
		return texPosXValues.get(id);
	}
	public float getItemTexPosY(int id){
		return texPosYValues.get(id);
	}
	public String getDescription(int id){
		return itemDescriptions.get(id);
	}
	public int getItemUses(int id){
		return uses.get(id);
	}
	public void draw(int x, int y){
		glBegin(GL_QUADS);
		for(int a =0; a< x; a++){
			for(int b = 0; b<y; b++){
				if(totalItems.get(new Point(a,b)) != null){
					tempID = totalItems.get(new Point(a,b)).getID();
				
					float iu = texPosXValues.get(tempID);
					float iv = texPosYValues.get(tempID);
					float iu2 = texPosXValues.get(tempID)+ World.FLOATINDEX;
					float iv2 = texPosYValues.get(tempID)+ World.FLOATINDEX;
						
					glTexCoord2f(iu,iv);		glVertex2f(a*32,		b*32);		
					glTexCoord2f(iu2,iv);		glVertex2f(a*32+32,		b*32);		
					glTexCoord2f(iu2,iv2);		glVertex2f(a*32+32,		b*32+32);	
					glTexCoord2f(iu,iv2);		glVertex2f(a*32,		b*32+32);
				}
			}
		}
		glEnd();
	}
}
