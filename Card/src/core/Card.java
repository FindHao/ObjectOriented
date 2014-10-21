package core;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**纸牌类 * */
@SuppressWarnings("serial")
public class Card extends JLabel implements MouseListener{
	
	static final int CARDWIDTH=50;
	static final int CARDHEIGHT=80;
	
	
	
	/**纸牌上的数值*/
	private int Num;
	/**花色	 0:红桃<br>	 1：方片 <br> 2：梅花 <br>  3：黑桃  <br>	 * */
	private int Color;
	/**牌是否正面朝上*/
	private boolean isOpen;
	/**牌的位置*/
	private Point pointer;
	/**牌所属的组,用来区分不同的牌堆*/
	private int Group;
	
	
	
	
	/**获取数值*/
	public  int getNum(){
		return Num;
	};
	/**获取花色*/
	public  int getColor(){
		return Color;
	};
	/**设置牌的翻开状态<br>
	 * 还要更新图片和位置
	 * */
	public void setOpen(boolean isOpen){
		this.isOpen=isOpen;
		setLocation(1,1);
		
	};
	/**获取翻开的状态*/
	public boolean getOpenState(){
		return isOpen;
	}
	/**设置牌的数值*/
	public void setNum(int aNum){
		Num=aNum;
	};
	/**设置牌的花色*/
	public void setColor(int color){
		Color=color;
	}
	/**获取位置描述点*/
	public Point getPointer(){
		return pointer;
	}
	/**设置位置描述点*/
	public void setPointer(Point pp){
		pointer=pp;
	}
	/**设置所属的组*/
	public void setGroup(int group){
		Group=group;
	}
	/**获得所属的组*/
	public int getGroup(){
		return Group;
	}
	/**设置牌的图片背景<br>*/
	public void setBackPicture(){
		String name=""+getColor()+"-"+getNum();
		if(!isOpen)name="0";
		ImageIcon temp=new ImageIcon("images/" + name + ".jpg");
		  temp.setImage(temp.getImage().getScaledInstance(CARDWIDTH, CARDHEIGHT, Image.SCALE_DEFAULT));
	    this.setIcon(temp);
	}
	
	/**初始化牌*/
	public Card(int num,int color,boolean isopen,Point pointer,int group ){
		super();
		setNum(num);
		setOpen(isopen);
		setPointer(pointer);
		setGroup(group);
		setColor(color);
		setBackPicture();
	}
	
	
	/**1，选取牌<br>
	 * 2，放置牌到下面的牌堆<br>
	 * 3，放置牌到顶牌堆<br>
	 * 4，翻顶部牌堆的牌<br>*/
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
