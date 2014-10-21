package core;

import java.awt.Point;
import java.util.Stack;

/**提供牌的两个牌堆*/
public class CardStack {
	/**初始满牌堆A的位置*/
	private final Point FULLSTACKLOCATION=new Point(530, 50);
	/**初始空牌堆B的位置*/
	private final Point EMPTYSTACKLOCATION=new Point(630,50);
	/**牌堆*/
	Stack<Card>cards;
	/**牌堆AB的标志<br>
	 * True:是初始化为满的牌堆
	 * False:是初始化为空的牌堆
	 * 
	 * */
	private boolean isA;
	/**空牌堆初始化*/
	public CardStack() {
		cards=new Stack<Card>();
		isA=false;
	}
	/**初始有牌的牌堆的初始化
	 *程序中的牌值color*13+Num-1，即为最开始初始化的值，即约定0-51为牌值
	 * */
	public CardStack(int a[]){
		cards=new Stack<Card>();
		isA=true;
		for(int i=0;i<a.length;i++){
			Card tempCard=new Card(a[i]%13+1,a[i]/13,false,FULLSTACKLOCATION,8);
			cards.add(tempCard);
		}
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
}
