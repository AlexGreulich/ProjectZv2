package Hilfsprogramme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PalettenPanel extends JPanel{
	
	Editor edit;
	Tileset tileset;
	public JScrollPane scroll = new JScrollPane();
	
	public PalettenPanel(Editor editor, Tileset tileset){
		this.tileset = tileset;
		edit = editor;
		setPreferredSize(new Dimension(2048,2048));
		scroll.setViewportView(this);
		scroll.setPreferredSize(new Dimension(50, 200));
		setDoubleBuffered(true);
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				setCurrentTileID(e.getX(), e.getY());
			}
		});
	}
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		int x = 0;
		int y = 0;
		for(int index = 0; index < tileset.getTileAmount(); index++){
			g2d.drawImage(tileset.getTileImage(index), x*32, y*32, this);
			if(index%64==63){
				x++;
				y=0;
			}
			else{
				y++;
			}
		}
		
		g.setColor(Color.white);
		for (int ly=32; ly<this.getHeight(); ly+=32){
			g2d.drawLine(0, ly, this.getWidth(), ly);
		}
		for (int lx=32; lx<this.getWidth(); lx+=32){
			g2d.drawLine(lx, 0, lx, this.getHeight());
		}
	}
	
	public void setCurrentTileID(int x, int y){
		if(x*y < tileset.getTileAmount()){
			tileset.setCurrentTileType(x+y*2048);
		}
	}
}