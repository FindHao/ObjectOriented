package card;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class Card implements Comparable<Card> {
	/**牌的花色*/
	private final Suit SUIT;
	/** 牌值*/
	private final int VALUE;
	/**	牌是否是翻开的*/
	private boolean hidden;
	/** 牌中心的坐标*/
	private int x, y;
	/** 牌的长度和宽度*/
	private int width, height;

	/**先初始化*/
	public Card(){
		this(Suit.SPADES, 1, 0, 0, 0, false);
	}

	/**
	 * @param suit	牌的花色
	 * @param value 牌值
	 * @param x 	牌中心的x坐标
	 * @param y 	牌中心的y坐标
	 * @param width	The width of the card in px. The card's height will be
	 * 				calculated from the value of width and be based roughly on 
	 * 				standard card dimensions. So the card's height will be 1.5 
	 * 				times the given width.
	 * @param hidden 牌是否是翻开的
	 */
	public Card(Suit suit, int value, int x, int y, 
			int width, boolean hidden){

		if(value < 1 || value > 13){
			throw new IllegalArgumentException("Value out of range.");
		}

		setSize(width);
		this.x = x;
		this.y = y;
		this.SUIT = suit;
		this.VALUE = value;
		this.hidden = hidden;
	}

	/**获取牌的花色	 */
	public Suit getSuit(){return SUIT;	}
	/**获取牌值*/
	public int getValue(){return VALUE;	}
	/**翻转牌 */
	public void flip(){hidden = !hidden;}
	/**获取牌的翻开状态	 */
	public boolean isHidden(){return hidden;}
	/**设置牌为未翻开的	 */
	public void setHidden(boolean hidden){this.hidden = hidden;	}
	/**获取牌中心的x坐标*/
	public int getX(){	return x;}
	public int getY(){return y;	}
	/**设置牌的中心坐标	 */
	public void setLocation(int x, int y){this.x = x;this.y = y;}
	/**只需要指定宽度，长度是宽度的1.5倍	 */
	public void setSize(int width){	this.width = width;	height = width * 3/2;}
	/**画牌的背面	 */
	private void drawBack(Graphics pane){
		//蓝色
		pane.setColor(new Color(0, 0, 150));
		pane.fillRoundRect(x - width/2 + width/20, y - height/2 + height/20, 
				width*9/10, height*9/10, width/10, height/10);
	}
	/**画牌的正面	 */
	private void drawFront(Graphics pane){
		//设置颜色，字体
		pane.setColor(SUIT.getColor());
		pane.setFont(new Font("Monospaced", Font.BOLD, width/4));
		//写上数值左上角
		pane.drawString(valueToString(), (int)(x - width*2/5), (int)(y - height/3));
		//右下角，同时数值翻转
		pane.setFont(new Font("Monospaced", Font.BOLD, -width/4));
		pane.drawString(valueToString(), (int)(x + width*2/5), (int)(y + height/3));
		//画上三个花色
		SUIT.draw(pane, x, y, width/3); 
		SUIT.draw(pane, x - width/3 + 2, y - height/5, width/5);
		SUIT.draw(pane, x + width/3 - 2, y + height/5, -width/5);
	}


	public void draw(Graphics pane){
		//白色背景
		pane.setColor(Color.WHITE);
		pane.fillRoundRect(x - width/2, y - height/2, 
				width, height, width/10, height/10);
		//黑色边框
		pane.setColor(Color.BLACK);
		pane.drawRoundRect(x - width/2, y - height/2, 
				width, height, width/10, height/10);
		if(hidden){
			drawBack(pane);
		} else { 
			drawFront(pane);
		}
	}

	/**返回牌值String类型*/
	private String valueToString(){
		switch(VALUE){
		case 1: return "A";	 //Ace
		case 11: return "J"; //Jack
		case 12: return "Q"; //Queen
		case 13: return "K"; //King
		default: return Integer.toString(VALUE);
		}
	}

	/**判定给的坐标是否在该张牌的范围内	 */
	public boolean contains(int x, int y){	return getShape().contains(x, y);}


	public Shape getShape(){
		return new RoundRectangle2D.Double(x - width/2, y - height/2, 
				width, height, width/10, width/10);
	}

	/**比较牌的大小	 */
	@Override
	public int compareTo(Card card) {return VALUE - card.getValue();}
	/**判断牌的颜色是否一样	 */
	public boolean colorEquals(Card card){	return SUIT.getColor() == card.getSuit().getColor();}
}