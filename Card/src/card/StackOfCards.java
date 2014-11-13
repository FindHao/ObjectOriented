package card;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import dataStructures.Stack;
/**做大量初始化的工作*/
public class StackOfCards extends Stack<Card> {
	protected int x, y;
	protected int cardWidth;
	protected int offsetX, offsetY;

	public StackOfCards(){}

	/**
	 * @param x			
	 * @param y			卡片中心
	 * @param cardWidth	卡片宽度
	 * @param offsetX	上下两个卡片的x距离，如果是0，就表示上面的全部压着下面那个
	 * @param offsetY	y距离
	 */
	public StackOfCards(int x, int y, int cardWidth, int offsetX, int offsetY){
		this.x = x;
		this.y = y;
		this.cardWidth = cardWidth;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	/**一开始初始化52张牌，然后打乱他们的顺序*/
	public static StackOfCards randomDeck(){
		StackOfCards deck = new StackOfCards();
		deck.fillBySuit();
		deck.shuffle();           
		return deck;
	}
	/**添加牌*/
	public void push(Card card){
		card.setLocation(x + offsetX*size, y + offsetY*size);
		card.setSize(cardWidth); 
		super.push(card);
	}
	/**生成52张牌*/
	public void fillBySuit(){
		for(Suit suit : Suit.values()){
			for(int i = 1; i < 14; i++){
				push(new Card(suit, i, cardWidth, x, y, false));
			}
		}
	}
	public void reverse(){
		super.reverse();
		setLocation(x, y); 
	}
	/**打乱牌的顺序*/
	public void shuffle(){
		head = knuthShuffle(mergeShuffle(head));
		setLocation(x, y);
	}
	private Node<Card> knuthShuffle(Node<Card> head){
		if(size < 2) 
			return head;
		int tempSize = size;
		for(Node<Card> node = head; node.getNext() != null; node = node.getNext()){
			Node<Card> randomNode = node; 
			int numOfIterations = (int)(Math.random() * tempSize--);
			for(int i = 0; i < numOfIterations; i++){
				randomNode = randomNode.getNext();
			}	
			swap(node, randomNode); 
		}
		return head;
	}
	
	private void swap(Node<Card> node1, Node<Card> node2){
		Card tempCard = node1.getValue();	
		node1.setValue(node2.getValue());
		node2.setValue(tempCard);
	}

	private Node<Card> mergeShuffle(Node<Card> node){
		if(node== null || node.getNext() == null){ 
			return node;					 
		}
		Node<Card> headL = node;		
		Node<Card> slowCounter = node;	
		Node<Card> fastCounter = node.getNext();	
		while(fastCounter != null && fastCounter.getNext() != null){
			slowCounter = slowCounter.getNext();
			fastCounter = fastCounter.getNext().getNext(); 
		}
		Node<Card> headR = slowCounter.getNext();
		slowCounter.setNext(null); 
		headL = mergeShuffle(headL); 
		headR = mergeShuffle(headR);
		return randomizedMerge(headL, headR); 
	}

	private Node<Card> randomizedMerge(Node<Card> left, Node<Card> right){
		if(left == null) 
			return right;
		if(right == null)
			return left;
		Node<Card> randomHead = null; 
		if(Math.random() <= 0.5){ 
			randomHead = left;
			randomHead.setNext(randomizedMerge(left.getNext(), right));
		} else {
			randomHead = right;
			randomHead.setNext(randomizedMerge(left, right.getNext()));
		}
		return randomHead;
	}

	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
		int i = 0;
		for(Node<Card> node = head; node != null; node = node.getNext()){
			node.getValue().setLocation( 
					x + (size- i - 1)*offsetX, y + (size - i - 1)*offsetY);
			i++;
		}
	}
	public void setOffset(int offsetX, int offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		setLocation(x, y); 
	}
	public boolean contains(int x, int y){
		for(Node<Card> node = head; node != null; node = node.getNext()){
			if(node.getValue().contains(x, y))
				return true;
		}
		return false;
	}
	public void draw(Graphics pane){
		if(isEmpty()){
			drawOutlineOfNextCard(pane);
		} else {
			draw(pane, head);	
		}
	}
	private void draw(Graphics pane, Node<Card> node){
		if(node != null){
			draw(pane, node.getNext());
			node.getValue().draw(pane);
		}
	}
	public RoundRectangle2D.Double shapeOfNextCard(){
		return new RoundRectangle2D.Double(
				x - cardWidth/2 - offsetX*size, y - cardWidth*3/4 + offsetY*size, 
				cardWidth, cardWidth*3/2, cardWidth/10, cardWidth/10);
	}
	public void drawOutlineOfNextCard(Graphics pane){
		pane.setColor(Color.LIGHT_GRAY);
		((Graphics2D)pane).fill(shapeOfNextCard());
	}
	public int getX(){		return x;	}
	public int getY(){		return y;	}
}