package Hilfsprogramme;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.HashSet;

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
	boolean selectionOn, smthSelected;
	int selX1, selY1, selX2, selY2;
	HashSet<Integer> typesToChange;
	

	public KartenPanel(EditorController controller){
		selX1 = 0;
		selX2 = 0;
		selY1 = 0;
		selY2 = 0;
		typesToChange = new HashSet<Integer>();
		this.controller = controller;
		scroll = new JScrollPane();
		scroll.setViewportView(this);
		selectionOn = false;
		smthSelected = false;
		
		setDoubleBuffered(true); //verhindert Flackern
		initCleanMap();
		setPanelDimensions();
		
		editor = controller.getEditor();
		m = RepaintManager.currentManager(editor);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				onMouseClicked(e);
			}
			public void mousePressed(MouseEvent e){
				onMousePressed(e);
			}			
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e) {
				onMouseDragged(e);
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				onMouseMoved(e);
			}
		});
	}
	
	public void onMouseClicked(MouseEvent e) {
		if (!selectionOn) {
			zeichneTile(e.getX(), e.getY());
		}
		if (selectionOn) {
			System.out.println("select all");
			selX1 = 0;
			selY1 = 0;
			selX2 = karte.getWidth();
			selY2 = karte.getHeight();
			smthSelected = true;
			controller.setCalculateable(true);
			repaint();
		}
	}
	
	public void onMouseDragged(MouseEvent e) {
		if (!selectionOn){
			zeichneTile(e.getX(), e.getY());
		}
		if (selectionOn){
			selX2 = e.getX()/controller.getCurrentZoom();
			selY2 = e.getY()/controller.getCurrentZoom();
			smthSelected = true;
			controller.setCalculateable(true);
			repaint();
		}
	}
	
	public void onMousePressed(MouseEvent e) {
		if (selectionOn){
			selX1 = e.getX()/controller.getCurrentZoom();
			selY1 = e.getY()/controller.getCurrentZoom();
		}
	}
	
	public void onMouseMoved(MouseEvent e) {
		if (!selectionOn){
			mouseX = e.getX();
			mouseY = e.getY();
			repaint(); // geht nicht anders
		}
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
		int cs = controller.getCurrentZoom();
 
		startx = startx/cs;
		starty = starty/cs;
		endx = endx/cs;
		endy = endy/cs;
		
		if(endx < karte.getWidth()){
			endx++;
		}
 
		if(endy < karte.getHeight()){
			endy++;
		}
 
		for(int x = startx; x < endx; x++){
			for(int y = starty; y < endy; y++){
				BufferedImage tile = controller.getZoomTileImage(karte.getTileType(x, y));
				g2d.drawImage(tile, x*cs, y*cs, this);
			}
		}
		
		if (!selectionOn){
			g2d.drawImage(controller.getZoomCurrentTileImage(), (mouseX/cs)*cs, (mouseY/cs)*cs, this);
		}
		
		if ((selectionOn) && (smthSelected)){
			g2d.setColor(Color.black);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
			int n = controller.getCurrentZoom();
			g2d.fillRect(selX1*n, selY1*n, selX2*n-selX1*n, selY2*n-selY1*n);
		}
	}
		
 
	public void setPanelDimensions(){
		//Hier wird nun eine Feste größe des JPanel gesetzt.
		setPreferredSize(new Dimension(karte.getWidth()*controller.getCurrentZoom(), karte.getHeight()*controller.getCurrentZoom()));
		scroll.setViewportView(this);
	}
	
	
	public void zeichneTile(int x, int y){
		int cs = controller.getCurrentZoom();
		x = x/cs;
		y = y/cs;
		
		if ((x >= 0) && (y >= 0)){
			karte.setTileType(x,y,controller.getCurrentTileType());
						
			Rectangle rec = scroll.getViewport().getViewRect();
		
			int dx = this.scroll.getLocation().x + editor.getInsets().left - rec.x;
			int dy = this.scroll.getLocation().y + editor.getInsets().top - rec.y + editor.menubar.getHeight();
			//zeichnet jframe inhalte neu dx und dy sind die offsets innerhalb des jframes

			m.addDirtyRegion(editor , dx+x*cs, dy+y*cs, cs+1, cs+1);
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

	public void setSelectionState(boolean b) {
		selectionOn = b;
		repaint();
	}
	
	public void calculateBorders() {
		if ((selectionOn) && (smthSelected)){
			karte.calculateBorders(selX1, selY1, selX2, selY2, typesToChange);
			smthSelected = false;
			repaint();
		}
	}

	public void setChangeable(int i) {
		typesToChange.add(i);
	}

	public void setNotChangeable(int n) {
		typesToChange.remove(n);
	}
}
