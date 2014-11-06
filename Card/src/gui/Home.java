package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import core.CardDump;
import core.CardStack;
import core.Lib;
/**主界面*/
public class Home extends JFrame{
	private static final long serialVersionUID = 1L;
	/**下面七个牌堆*/
	CardDump []dumps;
	CardStack stack8;
	CardStack stack9;
	/**上面四个回收的牌堆，此时不能再取回牌,只记录最后一张牌大小即可*/
	int fourrec[];
	
	Lib lib;
	
	public Home() {
		super();
		setSize(690, 700);
		moveToScreenCenter();
		
		//initialize
		lib=new Lib();
		lib.initializeNums();
		initDumpsAndStacks();
		
		
		
		
		
		
		
		
		
		
		
		
		setVisible(true);
	}
	public void initDumpsAndStacks(){
		dumps=new CardDump[8];
		for(int i=1;i<=7;i++){
			dumps[i]=new CardDump(i);
			dumps[i].Initialize(lib.getInitCardsForDump(i, i));
		}
		stack8=new CardStack(8,this);
		stack8.initialize(lib.getInitCardsForDump(24, 8));
		stack9=new CardStack(9,this);
		fourrec=new int[5];
		for(int i=0;i<=4;i++)fourrec[i]=0;
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
