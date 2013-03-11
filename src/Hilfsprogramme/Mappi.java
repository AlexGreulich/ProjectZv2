package Hilfsprogramme;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class Mappi extends JFrame {

	private JTextArea textarea;
	
	public Mappi (){
		super("Copy me:");
		
		textarea = new JTextArea(20, 50);
		JScrollPane scrollPane = new JScrollPane(textarea); 
		textarea.setFont(new Font("Serif", Font.PLAIN, 16));
		textarea.setLineWrap(false);
		add(scrollPane);
		
		for (int i = 0; i < 20 ; i++ ){
			String str = "{" + i  ;
			for (int j = 1; j < 15 ; j++){
				str += " ," + (i+j*32);
			}
			str += " }, ";
			textarea.append("\r\n" + str);
		}
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Mappi();
	}

}
