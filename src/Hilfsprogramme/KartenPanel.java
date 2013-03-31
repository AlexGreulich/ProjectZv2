package Hilfsprogramme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;

public class KartenPanel extends JPanel{
	
	Karte karte;
	Editor editor;
	EditorController controller;
	JScrollPane scroll;
	RepaintManager m;
	int mouseX, mouseY;
	 
	public KartenPanel(EditorController controller){
		this.controller = controller;
		scroll = new JScrollPane();
		scroll.setViewportView(this);
		
		setDoubleBuffered(true); //verhindert Flackern
		initCleanMap();
		setPanelDimensions();
		
		editor = controller.getEditor();
		m = RepaintManager.currentManager(editor);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				zeichneTile(e.getX(), e.getY());
			}			
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				zeichneTile(e.getX(), e.getY());
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint(); // geht nicht anders
			}
		});
	}
	
	public void initCleanMap(){
		karte = new Karte();
	}
	
	public void setMap(BufferedImage kartenImage){
		karte = new Karte(kartenImage);
	}
	
	
	public void paintComponent(Graphics g){
		// there is a bit of a problem with the transparency
		// with "super..." > transparent tiles have a white background
		// without > black lines appear in the transparent part of the tile
		// super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		Rectangle r = g2d.getClipBounds();
		int startx = r.x;
		int starty = r.y;
		int endx = startx+r.width;
		int endy = starty+r.height;
 
		startx = startx/32;
		starty = starty/32;
		endx = endx/32;
		endy = endy/32;
		
		if(endx < karte.getWidth()){
			endx++;
		}
 
		if(endy < karte.getHeight()){
			endy++;
		}
 
		for(int x = startx; x < endx; x++){
			for(int y = starty; y < endy; y++){
				BufferedImage tile = controller.getTileImage(karte.getTileType(x, y));
				g2d.drawImage(tile, x*32, y*32, this);
			}
		}
		
		g2d.drawImage(controller.getCurrentTileImage(), (mouseX/32)*32, (mouseY/32)*32, this);
	}
		
 
	public void setPanelDimensions(){
		//Hier wird nun eine Feste größe des JPanel gesetzt.
		setPreferredSize(new Dimension(karte.getWidth()*32, karte.getHeight()*32));
		scroll.setViewportView(this);
	}
	
	
	public void zeichneTile(int x, int y){
		x = x/32;
		y = y/32;
		
		if ((x >= 0) && (y >= 0)){
			karte.setTileType(x,y,controller.getCurrentTileType());
						
			Rectangle rec = scroll.getViewport().getViewRect();
		
			int dx = this.scroll.getLocation().x + editor.getInsets().left - rec.x;
			int dy = this.scroll.getLocation().y + editor.getInsets().top - rec.y + editor.menubar.getHeight();
			//zeichnet jframe inhalte neu dx und dy sind die offsets innerhalb des jframes

			m.addDirtyRegion(editor , dx+x*32, dy+y*32, 33, 33);
		}
	}
	
	
	public BufferedImage getSaveableImage(){
		BufferedImage bufferedImage = new BufferedImage(karte.getWidth(), karte.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Color color;
		int rgb,r,g,b,type;
		for(int x=0; x<karte.getWidth();x++){
			for(int y=0; y<karte.getHeight();y++){
				type = karte.getTileType(x,y);
				r = type/65536;
				g = (type-r*65536)/256;
				b = type - (g*256 + r*65536);
				color = new Color(r, g, b);
				bufferedImage.setRGB(x, y, color.getRGB());
			}
		}
		return bufferedImage;
	}
}
