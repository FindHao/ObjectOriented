package card;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Holds suits of stand French playing cards. Methods are included to get
 * the suit's color and draw the suit's symbol.<p>
 * 
 *	spades:黑桃   hearts：红桃 diamonds：方块 clubs：梅花
 */
public enum Suit {
	SPADES, HEARTS, DIAMONDS, CLUBS;

	/** Holds the symbol for this suit as an image.							*/
	private Image symbol;

	/**
	 * Four static {@link Image}s for each suit. They are static so that each
	 * <code>Suit</code> instance doesn't have its own image in memory. These
	 * variables are private because they may be <code>null</code> at some
	 * point.
	 */
	private static Image SPADES_ICON, HEARTS_ICON, DIAMONDS_ICON, CLUBS_ICON;

	/**
	 * Constructor which sets the symbol for the suit.
	 */
	private Suit(){
		setImages();
	}

	private void setImages(){
		if(SPADES_ICON != null) //Then there is no need to read all images again
			return;             //as they have already been read.
		
		try {
			SPADES_ICON = ImageIO.read(getClass().getResource("spade.gif"));
			HEARTS_ICON = ImageIO.read(getClass().getResource("heart.gif"));
			DIAMONDS_ICON = ImageIO.read(getClass().getResource("diamond.gif"));
			CLUBS_ICON = ImageIO.read(getClass().getResource("club.gif"));
		} catch(IOException e){}
	}

	/**根据花色来设置图片
	 */
	private void setSymbol(){
		switch(this){
		case SPADES:
			symbol = SPADES_ICON;
			break;
		case HEARTS:
			symbol = HEARTS_ICON;
			break;
		case CLUBS:
			symbol = CLUBS_ICON;
			break;
		case DIAMONDS:
			symbol = DIAMONDS_ICON;
			break;
		}
	}

	/**返回牌的颜色，
	 * 红桃，方片：红色
	 * 黑桃，梅花：黑色
	 */
	public Color getColor(){
		switch(this){
		case HEARTS: case DIAMONDS:
			return Color.RED;
		case SPADES: case CLUBS: default:
			return Color.BLACK;
		}
	}

	/**在指定位置画上花色*/
	public void draw(Graphics pane, int x, int y, int width){
		if(symbol == null){ 
			setSymbol();
		}
		double scale = width * 1.0 / symbol.getWidth(null); 
		int height = (int)(symbol.getHeight(null) * scale); 
		pane.drawImage(symbol, x - width/2, y - height/2, width, height, null);
	}
}