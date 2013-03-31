package Hilfsprogramme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RepaintManager;


public class PalettenPanel extends JPanel{
	
	EditorController controller;
	public JScrollPane scroll = new JScrollPane();
	RepaintManager m;
	
	public PalettenPanel(EditorController controller){
		this.controller = controller;
		new MouseExplorer(this);
		setPreferredSize(new Dimension(2048,2048));
		scroll.setViewportView(this);
		scroll.setPreferredSize(new Dimension(50, 200));
		setDoubleBuffered(true);
		m = RepaintManager.currentManager(controller.getEditor());
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				setCurrentTileID(e.getX(), e.getY());
			}
		});
	}
	
	
	public void setCurrentTileID(int x, int y){
		controller.setCurrentTileType(x/32+(y/32)*64);
	}
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		int x = 0;
		int y = 0;
		for(int index = 0; index < controller.getTileAmount(); index++){
			g2d.drawImage(controller.getTileImage(index), x*32, y*32, this);
			if(index%64==63){
				y++;
				x=0;
			}
			else{
				x++;
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
}