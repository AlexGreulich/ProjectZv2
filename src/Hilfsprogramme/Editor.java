package Hilfsprogramme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Editor extends JFrame{
	
	PalettenPanel palettenPanel;	
	KartenPanel kartenPanel;
	JMenuBar menubar;
	EditorController controller;
	JButton zoomInButton, zoomOutButton, personButton, selectionButton;
	
	// Konstruktor
	public Editor(){
		super("Project Z V#2 | Editor");
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		JMenu menu1 = new JMenu("File");
		menubar.add(menu1);
		JMenuItem newmap = new JMenuItem("new");
		JMenuItem load = new JMenuItem("load");
		JMenuItem save = new JMenuItem("save");
		menu1.add(save);
		menu1.add(load);
		menu1.add(newmap);
		
		JMenu menu2 = new JMenu("Tiles");
		menubar.add(menu2);
		JCheckBoxMenuItem all = new JCheckBoxMenuItem("Show all");
		JCheckBoxMenuItem country = new JCheckBoxMenuItem("Countrsside");
		JCheckBoxMenuItem houses = new JCheckBoxMenuItem("Houses");
		JCheckBoxMenuItem items = new JCheckBoxMenuItem("Items");
		menu2.add(all);
		menu2.add(country);
		menu2.add(houses);
		menu2.add(items);
		all.setEnabled(false);
		country.setEnabled(false);
		houses.setEnabled(false);
		items.setEnabled(false);
		
		JMenu menu3 = new JMenu("Umwandeln");
		menubar.add(menu3);
		JMenuItem water = new JMenuItem("Water");
		JMenuItem sand = new JMenuItem("Sand");
		JMenuItem stone = new JMenuItem("Stone");
		JMenuItem bush = new JMenuItem("Bush");
		JMenuItem concrete = new JMenuItem("Concrete");
		JMenuItem house = new JMenuItem("House");
		menu3.add(water);
		menu3.add(sand);
		menu3.add(stone);
		menu3.add(bush);
		menu3.add(concrete);
		menu3.add(house);
		water.setEnabled(false);
		sand.setEnabled(false);
		stone.setEnabled(false);
		bush.setEnabled(false);
		concrete.setEnabled(false);
		house.setEnabled(false);
		
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onSpeichern();
			}
		});
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onLaden();
			}
		});
		newmap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				onNew();
			}
		});
		
		controller = new EditorController(this);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel toolPanel = new JPanel();
//		toolPanel.setPreferredSize(new Dimension(40,40));
		toolPanel.setLayout(new GridLayout(4,1));
		
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		personButton = new JButton("P");
		selectionButton = new JButton("S");
		toolPanel.add(zoomInButton);
		toolPanel.add(zoomOutButton);
		toolPanel.add(personButton);
		toolPanel.add(selectionButton);
		
		
		palettenPanel = new PalettenPanel(controller);
		kartenPanel = new KartenPanel(controller);
		//controller.initParts(palettenPanel, kartenPanel);
		
		JButton testB = new JButton("test");
		testB.setPreferredSize(new Dimension(100,100));
		topPanel.add(toolPanel);
		topPanel.add(palettenPanel.scroll);
		//topPanel.add(testB);
		add(topPanel, BorderLayout.NORTH);		
		add(kartenPanel.scroll, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	
	public void onLaden(){
		JFileChooser chooser = new JFileChooser(new File(new File("").getAbsolutePath()).toString()+"/src/karten");
		int result = chooser.showOpenDialog(this);
		if (result == chooser.APPROVE_OPTION){
			File f = chooser.getSelectedFile();
			BufferedImage kartenImage;
			try {
				kartenImage = ImageIO.read(f);
				controller.setMap(kartenImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
	
	
	public void onSpeichern(){
		JFileChooser chooser = new JFileChooser(new File(new File("").getAbsolutePath()).toString()+"/src/karten");
		int result = chooser.showSaveDialog(this);
		if (result == chooser.APPROVE_OPTION){
			File file = chooser.getSelectedFile();
			try {
				ImageIO.write(controller.getSaveableImage(), "png", file);
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
	public void onNew(){
		controller.createNewMap();
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
