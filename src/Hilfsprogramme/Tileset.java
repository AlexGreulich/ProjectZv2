package Hilfsprogramme;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Tileset {
	
	ArrayList<BufferedImage> images, zoomImages;
	
	public Tileset(int scale){
		images = new ArrayList<BufferedImage>();
		try{
			BufferedImage tileset = ImageIO.read(getClass().getResource("../tilesets/Tileset_2048.png"));
			for(int y = 0; y < (tileset.getWidth()/32) ;y++){
				for(int x = 0; x < (tileset.getHeight()/32) ;x++){
					BufferedImage bi = tileset.getSubimage(x*32, y*32, 32, 32);
					images.add(bi);
				}
			}
			zoomImages = new ArrayList<BufferedImage>();
			for (int i = 0; i<images.size(); i++){
				zoomImages.add(resize(images.get(i),scale,scale));
			}
		}catch(IOException e){e.printStackTrace();}
	}
	
	public void updateZoomTiles(int scale){
		for (int i = 0; i<images.size(); i++){
			zoomImages.set(i, resize(images.get(i),scale,scale));
		}
	}

	public BufferedImage getTileImage(int type) {
		return images.get(type);
	}
	public BufferedImage getZoomTileImage(int type) {
		return zoomImages.get(type);
	}
	public int getTileAmount(){
		return images.size();
	}
	
	private BufferedImage resize (BufferedImage img, int newW, int newH) {  
        int w = img.getWidth();  
        int h = img.getHeight();  
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
        Graphics2D g = dimg.createGraphics();  
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
        g.dispose();  
        return dimg;
    } 
}
