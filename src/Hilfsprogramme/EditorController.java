package Hilfsprogramme;

import java.awt.image.BufferedImage;

import javax.swing.JMenuBar;

public class EditorController {
	
	Editor editor;
	PalettenPanel palettenPanel;	
	KartenPanel kartenPanel;
	JMenuBar menubar;
	Tileset tileset;
	int currentTile;
	int currentZoom;
	
	public EditorController (Editor editor){
		this.editor = editor;
		tileset = new Tileset();
	}
	
	public void initParts(PalettenPanel palettenPanel2, KartenPanel kartenPanel2) {
		this.palettenPanel = palettenPanel2;
		this.kartenPanel = kartenPanel2;
	}
	
	public Editor getEditor(){
		return editor;
	}
	
	public int getCurrentTileType() {
		return currentTile;
	}
	public void setCurrentTileType(int currentTile) {
		this.currentTile = currentTile;
		kartenPanel.repaint();
	}
	
	public void setMap(BufferedImage kartenImage){
		kartenPanel.setMap(kartenImage);
	}
	public BufferedImage getSaveableImage(){
		return kartenPanel.getSaveableImage();
	}

	public BufferedImage getTileImage(int tileType) {
		return tileset.getTileImage(tileType);
	}

	public BufferedImage getCurrentTileImage() {
		return tileset.getTileImage(currentTile);
	}

	public int getTileAmount() {
		return tileset.getTileAmount();
	}

	public void createNewMap() {
		kartenPanel.initCleanMap();
		kartenPanel.repaint();
	}

	public void zoomIn() {
		// TODO Auto-generated method stub
		
	}
	public void zoomOut() {
		// TODO Auto-generated method stub
		
	}

}
