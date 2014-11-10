package solitaire;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**主Panel来加载游戏 */
public class Solitaire extends JPanel  {
	private Klondike game;
	public Solitaire(){
		game = new Klondike(this);
	}

	protected void paintComponent(Graphics pane) {
		super.paintComponent(pane);
		game.paint(pane);
	}

	public static void main(String[] args){
		Solitaire gamePanel = new Solitaire();		
		JFrame window = new JFrame();				
		window.setTitle("Solitaire");				
		window.setLocation(10, 10); 				
		window.getContentPane().setBackground(gamePanel.getBackground()); 
		window.add(gamePanel);						
		window.setSize(gamePanel.getPreferredSize());
		window.setVisible(true);	
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}