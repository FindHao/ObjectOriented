package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
/**主界面*/
public class Home extends JFrame{
	private static final long serialVersionUID = 1L;
	
	
	public Home() {
		super();
		setSize(690, 700);
		moveToScreenCenter();
		setVisible(true);
	}
	public static void main(String[] args) {
		new Home();
	}
	
	
	/**移动窗口到屏幕中央*/
	void moveToScreenCenter(){
		Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();   
		Dimension   framesize; 
		framesize=getSize();
		  int   x   =   (int)screensize.getWidth()/2   -   (int)framesize.getWidth()/2;   
        int   y   =   (int)screensize.getHeight()/2   -   (int)framesize.getHeight()/2;   
        setLocation(x,y);   
	}
}
