package Hilfsprogramme;

import java.awt.Color;
import java.awt.image.BufferedImage;


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
	
}
