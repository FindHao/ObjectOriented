package core;

import java.awt.Container;
import java.util.Stack;

/**提供牌的两个牌堆*/
public class CardStack {
	/**初始满牌堆A的位置*/
//	private final Point FULLSTACKLOCATION=new Point(530, 50);
	/**初始空牌堆B的位置*/
//	private final Point EMPTYSTACKLOCATION=new Point(630,50);
	/**牌堆*/
	Stack<Card>cards;
	private int group;
	
	Container parentContainer;
	
	
	/**牌堆初始化*/
	public CardStack(int group,Container aContainer) {
		
		cards=new Stack<Card>();
		parentContainer=aContainer;
		this.group=group;
	}
	public int getGroup(){
		return group;
	}
	public boolean isEmpty(){
		return cards.empty();
	}
	/**牌堆弹出<br>判空操作在调用pop之前！执行pop的时候一定非空*/
	public Card pop(){
		return cards.pop();
	}
	/**牌堆压入<br>因为压入操作只有在B中，而且压入之后牌面都是向上的*/
	public void push(Card aCard){
		aCard.setOpen(true);
		cards.push(aCard);
	}
	/**牌堆内容的初始化*/
	public void initialize(Card []initCards){
		int templen=initCards.length;
		for(int i=0;i<templen;i++){
			cards.push(initCards[i]);
			parentContainer.add(cards.firstElement());
			cards.firstElement().setLocation(Lib.getPointForGroup(8));
			parentContainer.validate();
			parentContainer.repaint();
		}
		
	}
}
