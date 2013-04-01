package Hilfsprogramme;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Editor extends JFrame{
	
	PalettenPanel palettenPanel;	
	KartenPanel kartenPanel;
	JMenuBar menubar;
	EditorController controller;
	JButton zoomInButton, zoomOutButton;
	JCheckBox selectionBox;
	JCheckBoxMenuItem water, sand, stone, concrete, bush, house;
	JMenu menu3;
	JMenuItem calculate;
	JLabel zoomLabel;
	
	// Konstruktor
	public Editor(){
		super("Project Z V#2 | Editor");
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		createMenu();
		
		controller = new EditorController(this);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new GridLayout(5,1));
		
		zoomLabel = new JLabel(""+controller.getCurrentZoom()+" px");
		zoomInButton = new JButton("+");
		zoomOutButton = new JButton("-");
		JLabel calcLabel = new JLabel("calcB.");
		selectionBox = new JCheckBox();
		
		selectionBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				onChecked(e);
				
			}
		});
		toolPanel.add(zoomLabel);
		toolPanel.add(zoomInButton);
		toolPanel.add(zoomOutButton);
		toolPanel.add(calcLabel);
		toolPanel.add(selectionBox);
		
		ZoomButtonHandler buttonHandler = new ZoomButtonHandler();
		zoomInButton.addActionListener(buttonHandler);
		zoomOutButton.addActionListener(buttonHandler);
				
		palettenPanel = new PalettenPanel(controller);
		kartenPanel = new KartenPanel(controller);
		controller.initParts(palettenPanel, kartenPanel);
		
		topPanel.add(toolPanel);
		topPanel.add(palettenPanel.scroll);
		add(topPanel, BorderLayout.NORTH);		
		add(kartenPanel.scroll, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}
	
	
	private void createMenu(){
		menubar = new JMenuBar();
		setJMenuBar(menubar);
		
		// File menu
		JMenu menu1 = new JMenu("File");
		menubar.add(menu1);
		JMenuItem newmap = new JMenuItem("new");
		JMenuItem load = new JMenuItem("load");
		JMenuItem save = new JMenuItem("save");
		menu1.add(save);
		menu1.add(load);
		menu1.add(newmap);
		
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
		
		// Tile menu
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
		
		// CalcBorders menu
		menu3 = new JMenu("CalcBorders");
		menubar.add(menu3);
		menu3.setEnabled(false);
		water = new JCheckBoxMenuItem("Water");
		sand = new JCheckBoxMenuItem("Sand");
		stone = new JCheckBoxMenuItem("Stone");
		bush = new JCheckBoxMenuItem("Bush");
		concrete = new JCheckBoxMenuItem("Concrete");
		house = new JCheckBoxMenuItem("House");
		calculate = new JMenuItem("calculate");
		calculate.setEnabled(false);
		menu3.add(water);
		menu3.add(sand);
		menu3.add(stone);
		menu3.add(bush);
		menu3.add(concrete);
		menu3.add(house);
		menu3.addSeparator();
		menu3.add(calculate);
		
		water.addItemListener(new TileItemListener());
		sand.addItemListener(new TileItemListener());
		stone.addItemListener(new TileItemListener());
		bush.addItemListener(new TileItemListener());
		concrete.addItemListener(new TileItemListener());
		house.addItemListener(new TileItemListener());
		
		calculate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.calculateBorders();
			}
		});
	}
	
	
	public void setCalculateable(boolean b){
		calculate.setEnabled(b);
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
	
	private void onChecked(ItemEvent e) {
		 if (e.getStateChange() == ItemEvent.DESELECTED) {
			 controller.setSelectionState(false);
			 menu3.setEnabled(false);
	     }
		 if (e.getStateChange() == ItemEvent.SELECTED) {
			 controller.setSelectionState(true);
			 menu3.setEnabled(true);
	     }
	}
	
	
	private class ZoomButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == zoomInButton){
				controller.zoomIn();
				zoomLabel.setText(""+controller.getCurrentZoom()+" px");
			}
			if (e.getSource() == zoomOutButton){
				controller.zoomOut();
				zoomLabel.setText(""+controller.getCurrentZoom()+" px");
			}
		}
	}
	
	
	private class TileItemListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			int n = 0;
			if (e.getSource() == water){
				n = 2;
			} else if (e.getSource() == sand){
				n = 0;
			} else if (e.getSource() == bush){
				n = 3;
			} else if (e.getSource() == concrete){
				n = 1;
			} else if (e.getSource() == stone){
				n = 4;
			} else if (e.getSource() == house){
				n = 5;
			}
			
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				 controller.setNotChangeable(n);
		    }
			if (e.getStateChange() == ItemEvent.SELECTED) {
				 controller.setChangeable(n);
		    }
		}
		
	}

	
	// groesse des JFrames
	public Dimension getPreferredSize(){
		return new Dimension(1280,700);
	}
	
	
	// MAIN
	public static void main(String[] args){
		new Editor();
	}
}
