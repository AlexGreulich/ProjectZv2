package Hilfsprogramme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashSet;


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
		for (int n : typesToChange){
			System.out.println("change: "+n);
			for (int x = xc1; x <= xc2; x++){
				for (int y = yc1; y <= yc2; y++){
					calculateBorder(x, y, n);
				}
			}
		}
	}

	private void calculateBorder(int x, int y, int n) {
		// do something
		karte[x][y] = 50 + n;
	}	
}
