package Hilfsprogramme;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

public class TileMonitorPanel extends JPanel{
	
	Tileset tileset;
	RepaintManager m;
	Editor editor;
	
	public TileMonitorPanel(Tileset tileset, Editor editor){
		this.tileset = tileset;
		m = RepaintManager.currentManager(editor);
		setPreferredSize(new Dimension(500,500));
		setBounds(20, 220, 42, 70);
	}
	
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, 42, 70);
		g.setColor(Color.white);
		g.drawString("Tile: ", 5, 13);
		g.drawString("" + tileset.getCurrentTileType(), 5, 25);
		BufferedImage tile = tileset.getCurrentTileImage();
		g.drawImage(tile, 5, 32, this);	
	}
}
