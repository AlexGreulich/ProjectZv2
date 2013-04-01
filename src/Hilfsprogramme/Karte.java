package Hilfsprogramme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import MainPack.World;


public class Karte{
	int[][] karte;
	int width, height;
	
	public Karte(BufferedImage kartenImage){
		width = kartenImage.getWidth();
		height = kartenImage.getHeight();
		
		karte = new int [width][height];
		
		for(int x=0; x<width;x++){
			for(int y=0; y<height;y++){
				Color color = new Color(kartenImage.getRGB(x, y));
				karte[x][y]= color.getRed()*65536 + color.getGreen()*256 + color.getBlue();
			}
		}
	}
	
	public Karte(){
		width = 4000;
		height= 4000;
		karte = new int [width][height];
		for(int x=0; x<width;x++){
			for(int y=0; y<height;y++){
				karte[x][y] = 400;
			}
		}
	}
	
	public int getTileType(int x, int y){
		return karte[x][y];
	}
 
	public void setTileType(int x, int y, int tileID){
		karte[x][y] = tileID;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void calculateBorders(int xc1, int yc1, int xc2, int yc2, HashSet<Integer> typesToChange){
		System.out.println("karte change: "+xc1+" "+yc1+" "+xc2+" "+yc2+" "+typesToChange.toString());
		for (int type : typesToChange){
			System.out.println("change: "+type);
			for (int x = xc1; x <= xc2; x++){
				for (int y = yc1; y <= yc2; y++){
					calculateBorder(x, y, type*64);
				}
			}
		}
	}

	private void calculateBorder(int x, int y, int type) {
		if((x-1 >=0 ) && (y-1 >= 0) && (x+1 < width) && (y+1 < height) && (karte[x][y] == type)){
			
			// tile umgebung
			int toben = karte[x][y-1];
			int tunten = karte[x][y+1];
			int tlinks = karte[x-1][y];
			int trechts = karte[x+1][y];
			int tlioben = karte[x-1][y-1];
			int treoben = karte[x+1][y-1];
			int tliunten = karte[x-1][y+1];
			int treunten = karte[x+1][y+1];
			
			// neue werte
			int oben = type+2;
			int unten = type+7;
			int links = type+5;
			int rechts = type+4;
			int lioben = type+1;
			int reoben = type+3;
			int liunten = type+6;
			int reunten = type+8;
			int innenuntenlinks = type+9;
			int innenuntenrechts = type+10;
			int innenobenlinks = type+11;
			int innenobenrechts = type+12;
			
			if((tlinks==type) && (trechts==type)){
				karte[x][y] = type;
			}
			if((toben == type) && (tunten == type)){
				karte[x][y] = type;
			}
			if((treoben ==type) && (tliunten == type) && (treunten != type) && (tlioben != type)){
				karte[x][y] = type;
			}
			if((treunten == type) && (tlioben == type) && (treoben != type) && (tliunten != type)){
				karte[x][y] = type;
			}
			
			if((toben == type) && (tlinks == type)){
				karte[x][y] = innenuntenlinks;
			}
			if((toben == type) && (trechts == type)){
				karte[x][y] = innenuntenrechts;
			}
			if((tunten == type) && (tlinks == type)){
				karte[x][y] = innenobenlinks;
			}
			if((tunten == type) && (trechts == type)){
				karte[x][y] = innenobenrechts;
			}
					
			//	nur die 4 ecken:
			if((treoben ==type)&&(tlioben !=type)&&(tlinks !=type)&&(tliunten !=type)&&(treunten !=type)&&(trechts !=type)&&(toben !=type)&&(tunten !=type)){
				karte[x][y] = reunten;
			}
			if((treoben !=type)&&(tlioben == type)&&(tlinks !=type)&&(tliunten !=type)&&(treunten !=type)&&(trechts !=type)&&(toben !=type)&&(tunten !=type)){
				karte[x][y] = liunten;
			}
			if((treoben !=type)&&(tlioben !=type)&&(tlinks !=type)&&(tliunten == type)&&(treunten !=type)&&(trechts !=type)&&(toben !=type)&&(tunten !=type)){
				karte[x][y] = reoben;
			}
			if((treoben !=type)&&(tlioben !=type)&&(tlinks !=type)&&(tliunten !=type)&&(treunten == type)&&(trechts !=type)&&(toben !=type)&&(tunten !=type)){
				karte[x][y] = lioben;
			}

			if((trechts == type) && (tlinks !=type) && (tliunten !=type) && (tlioben !=type) && (toben !=type) && (tunten !=type)){
				karte[x][y] = links;
			}
			if((tlinks == type) && (treoben !=type) && (treunten !=type) && (trechts !=type) && (toben !=type) && (tunten !=type)){
				karte[x][y] = rechts;
			}
			if((toben == type) && (tlinks !=type) && (tliunten !=type) && (treunten !=type) && (trechts !=type) && (tunten !=type)){
				karte[x][y] = unten;
			}
			if((tunten == type) && (treoben !=type) && (tlioben !=type) && (tlinks !=type) && (trechts !=type) && (toben !=type)){
				karte[x][y] = oben;
			}
		}
	}
}
