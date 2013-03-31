package Hilfsprogramme;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Tileset {
	
	ArrayList<BufferedImage> images;
	
	public Tileset(){
		images = new ArrayList<BufferedImage>();
		try{
			BufferedImage tileset = ImageIO.read(getClass().getResource("../tilesets/Tileset_2048.png"));
			for(int y = 0; y < (tileset.getWidth()/32) ;y++){
				for(int x = 0; x < (tileset.getHeight()/32) ;x++){
					BufferedImage bi = tileset.getSubimage(x*32, y*32, 32, 32);
					images.add(bi);
				}
			}
		}catch(IOException e){e.printStackTrace();}
	}

	public BufferedImage getTileImage(int type) {
		return images.get(type);
	}
	public int getTileAmount(){
		return images.size();
	}
}
