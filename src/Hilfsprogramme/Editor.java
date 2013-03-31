package Hilfsprogramme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Editor extends JFrame{
	
	PalettenPanel palettenPanel;	
	KartenPanel kartenPanel;
	TileMonitorPanel monitor;
	JMenuBar menubar;
	Tileset tileset = new Tileset();
	
	
	// Konstruktor
	public Editor(){
		super("Project Z V#2 | Editor");
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		JMenu menu = new JMenu("Datei");
		menubar.add(menu);
		JMenuItem neu = new JMenuItem("neu");
		JMenuItem laden = new JMenuItem("laden");
		JMenuItem speichern = new JMenuItem("speichern");
		menu.add(speichern);
		menu.add(laden);
		menu.add(neu);
		
		speichern.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onSpeichern();
			}
		});
		laden.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onLaden();
			}
		});
		neu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do
			}
		});
		
		monitor = new TileMonitorPanel(tileset, this);
		add(monitor, null);
		palettenPanel = new PalettenPanel(this, tileset, monitor);
		add(palettenPanel.scroll, BorderLayout.NORTH);
		kartenPanel = new KartenPanel(this, tileset);
		add(kartenPanel.scroll, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	
	// Menubutton laden
	public void onLaden(){
		JFileChooser chooser = new JFileChooser(new File(new File("").getAbsolutePath()).toString()+"/src/karten");
		int result = chooser.showOpenDialog(this);
		if (result == chooser.APPROVE_OPTION){
			File f = chooser.getSelectedFile();
			BufferedImage kartenImage;
			try {
				kartenImage = ImageIO.read(f);
				kartenPanel.setMap(kartenImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	
	// Menubutton speichern
	public void onSpeichern(){
		JFileChooser chooser = new JFileChooser(new File(new File("").getAbsolutePath()).toString()+"/src/karten");
		int result = chooser.showSaveDialog(this);
		if (result == chooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			try {
				ImageIO.write(kartenPanel.getSaveableImage(), "png", file);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	
	// groesse des JFrames
	public Dimension getPreferredSize(){
		return new Dimension(1280,1000);
	}
	
	
	// MAIN
	public static void main(String[] args){
		new Editor();
	}
}
