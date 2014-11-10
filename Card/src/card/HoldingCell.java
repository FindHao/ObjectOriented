package card;

import dataStructures.Stack;

/**准备要移动的牌 */
public class HoldingCell extends StackOfCards {
	public HoldingCell(){}
	public HoldingCell(int x, int y, int cardWidth){
		super(x, y, cardWidth, 0, 0);
	}
	public void push(Card card){
		clear();
		super.push(card);
	}
	public void appendStack(Stack<Card> stack) {
		if(stack.size() < 2){
			super.appendStack(stack);
		} else {
			throw new IllegalArgumentException();
		}
	}
}