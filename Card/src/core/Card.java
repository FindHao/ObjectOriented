package core;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

/**纸牌类 * */
@SuppressWarnings("serial")
public class Card extends JLabel implements MouseListener{
	/**纸牌上的数值*/
	private int Num;
	/**花色	 0:红桃<br>	 1：方片 <br> 2：梅花 <br>  3：黑桃  <br>	 * */
	private int Color;
	/**牌是否正面朝上*/
	private boolean isOpen;
	/**牌的位置*/
	private Point pointer;
	/**牌所属的组*/
	private int Group;
	
	
	/**获取数值*/
	public  int getNum(){
		return Num;
	};
	/**获取花色*/
	public  int getColor(){
		return Color;
	};
	/**设置牌的翻开状态*/
	public void setOpen(boolean isOpen){
		this.isOpen=isOpen;
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
