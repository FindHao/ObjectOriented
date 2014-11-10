package card;

import dataStructures.Stack;
/**七个牌堆的类*/
public class Tableau extends StackOfCards {
	public Tableau(){}
	public Tableau(int x, int y, int cardWidth, int offsetY){
		super(x, y, cardWidth, 0, offsetY);
	}
	public Stack<Card> popCardsBelow(int y){
		if(!contains(this.x, y)) 
			return null;		
		Stack<Card> temp = new Stack<Card>();

		int numOfCards = 1;  
		while(!peek().contains(x, y + (numOfCards-1)*offsetY) &&
				numOfCards <= size){ 
			numOfCards++;    		
			System.out.println(peek().contains(x, y));
			System.out.println(y + " " + peek().getY() + " " + this.shapeOfNextCard().getCenterY());
			System.out.println(numOfCards + " will be popped");
		}

		for(int i = 0; i < numOfCards; i++){
			temp.push(pop());				
		}

		temp.reverse(); 
		return temp;
	}

	public Stack<Card> popSuitableCardsBelow(int y){
		Stack<Card> temp = popCardsBelow(y);
		if(temp == null || !isSuitable(temp)){
			appendStack(temp); 
			return null;       
		} else {
			return temp;	   
		}
	}

	public void appendSuitableCards(Stack<Card> stack){
		Card bottom = stack.reverseCopy().pop();
		if(!isEmpty() && (this.peek().compareTo(bottom) != 1
				|| this.peek().colorEquals(bottom))){

			String message = "The given stack is not suitable.";
			throw new IllegalArgumentException(message);
		}

		appendStack(stack); 
	}
	
	public static boolean isSuitable(Stack<Card> stack){
		return alternatesInColor(stack) && inSequence(stack) && isVisible(stack);
	}

	public static boolean alternatesInColor(Stack<Card> stack){
		if(stack.size() < 2){
			return true;
		}

		Stack<Card> copy = stack.copy();
		Card toCompare = copy.pop(); 

		while(!copy.isEmpty()){
			Card current = copy.pop(); 
			if(current.colorEquals(toCompare)){
				return false; 
			}
			toCompare = current;
		}
		return true; 
	}
	
	public static boolean isVisible(Stack<Card> stack){
		Stack<Card> copy = stack.copy();
		while(!copy.isEmpty()){
			if(copy.pop().isHidden()){
				return false;
			}
		}
		return true; 
	}

	public static boolean inSequence(Stack<Card> stack){
		if(stack.size() < 2){
			return true;
		}

		Stack<Card> copy = stack.copy();
		Card toCompare = copy.pop(); 

		while(!copy.isEmpty()){
			Card current = copy.pop();
			if(current.compareTo(toCompare) != 1){
				return false; 
			}
			toCompare = current; 
		}
		return true; 
	}
}