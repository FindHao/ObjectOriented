package solitaire;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

import card.Card;
import card.Foundation;
import card.StackOfCards;
import card.Tableau;
import dataStructures.Queue;
import dataStructures.Stack;

public class Klondike implements MouseListener, MouseMotionListener {
	/**下面七个牌堆*/
	protected Tableau[] tableaux;
	/**上面四个排好的*/
	protected Foundation[] foundations;
	/**上面那个还没有翻开的牌堆*/
	protected StackOfCards stock;
	/**已经翻开的牌*/
	protected StackOfCards waste;
	/**准备要移动的牌*/
	protected StackOfCards inUse;
	/**选中的要移动的牌的起始堆*/
	protected StackOfCards lastStack;
	protected boolean initiallyHidden = true;
	/**校验位检验是否初始化完毕*/
	protected boolean initialized;
	/**牌的宽度*/
	protected int cardWidth;
	//以下是界面的调整
	protected int offset;
	protected int yCoord;
	/** 选中的要移动的牌数*/
	protected int moves;
	protected int deltaX, deltaY;
	protected Container container;

	protected Queue<StackOfCards> animationQueue;
	public Klondike(){}
	public Klondike(Container container){
		this.container = container;
		container.addMouseListener(this); 		
		container.addMouseMotionListener(this); 
		container.setBackground(new Color(0, 180, 0)); //绿色背景
		container.setSize(600, 650);
		container.setPreferredSize(container.getSize());
		yCoord = container.getHeight()/12;
		cardWidth = 60;
		offset = cardWidth/2;
		inUse = new StackOfCards(0, 0, cardWidth, 0, offset * 3/2);
		animationQueue = new Queue<StackOfCards>();
		init();
	}

	protected void init(){
		//初始化一个用来分配的牌堆
		StackOfCards deck = StackOfCards.randomDeck();
		//初始化那七堆牌
		initTableaux(deck, new int[] {1, 2, 3, 4, 5, 6, 7});
		//初始化提供牌堆的牌堆
		initStockAndWaste(deck);
		//初始化四个用来接收的牌堆
		initFoundations(4);	
		//确定初始化成功
		initialized = true;
		container.repaint();
	}
	/**初始化七个牌堆*/
	protected void initTableaux(StackOfCards source, int[] initialTableauxSizes){
		tableaux = new Tableau[initialTableauxSizes.length];
		for(int i = 0; i < tableaux.length; i++){
			tableaux[i] = new Tableau(
					(cardWidth+10)*(i+1), yCoord + cardWidth*2, cardWidth, offset);
			for(int j = 0; j < initialTableauxSizes[i]; j++){ 
				tableaux[i].push(source.pop());          
				tableaux[i].peek().setHidden(initiallyHidden);
			}
		}
		//牌堆顶部的牌反转过来
		for(StackOfCards stack : tableaux){ 
			stack.peek().setHidden(false); 
		}
	}

	protected void initStockAndWaste(StackOfCards deck){
		stock = new StackOfCards(cardWidth + 10, yCoord, cardWidth, 0, 0);
		stock.appendStack(deck); //The stock contains all of its cards.
		stock.peek().setHidden(true); //So that the stock is hidden.

		waste = new StackOfCards(2*(stock.getX()), yCoord, cardWidth, 0, 0);
	}

	/**
	 * Initializes the size and location of foundation stacks which are initially empty.
	 */
	protected void initFoundations(int numOfFoundations){
		foundations = new Foundation[numOfFoundations];
		for(int i = 0; i < foundations.length; i++){
			foundations[i] = new Foundation(tableaux[tableaux.length - i - 1].getX(),
					yCoord, cardWidth);
		}
	}

	/**
	 * Performs the action associated with stock when clicked. If the stock is not
	 * empty, a card will be flipped from the stock to the waste, otherwise, the
	 * waste will be emptied onto the stock. In this method, the number of moves 
	 * is incremented if the action is performed.
	 * <p>
	 * The action will only be performed if the given mouse click coordinates
	 * are contained in the stock.
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action was performed, 
	 * 			else <code>false</code>
	 */
	protected boolean stockPressedAction(int x, int y){
		if(stock.contains(x, y)){
			//If the stock was clicked:
			waste.push(stock.pop());	 //Move the top card from stock to waste.
			waste.peek().setHidden(false);//And show it.

			if(!stock.isEmpty())
				stock.peek().setHidden(true);//Hides the new top card of the stack.
			moves++; //This counts as a move.
			container.repaint();
			return true; //The action was performed.

		} else if(stock.shapeOfNextCard().contains(x, y)){
			//else if the mouse clicked the empty stock's area:
			//Turn over all cards from the waste to the stock,
			stock.appendStack(waste.reverseCopy());
			waste.clear(); //and clear the waste.

			if(!stock.isEmpty()){
				stock.peek().setHidden(true); //So that stock is turned form
				moves++;					  //the user.
			}
			container.repaint();
			return true; //The action was performed.
		}
		return false; //The action was not performed.
	}

	/**
	 * Performs the action associated with the waste if the waste contains
	 * the given coordinates. The action is to pop a card from the waste and put
	 * it inUse. In this method, the number of moves is incremented if the action 
	 * is performed.
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action was performed, 
	 * 			else <code>false</code>
	 */
	protected boolean wastePressedAction(int x, int y){
		//If the waste has cards and the mouse clicked the waste,
		if(waste.contains(x, y)){
			inUse.push(waste.pop());//then the top card from the waste is put inUse
			lastStack = waste;  //and the waste becomes the last stack to be used
			moves++;
			return true; //The action was performed.
		}
		return false; //The waste was not clicked.
	}

	/**
	 * Performs the action associated with the tableaux. If one tableau contains
	 * the coordinates, all cards below the mouse click will be put inUse and
	 * removed from the tableau. The action will be deemed successful if cards
	 * are put inUse 
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action was successfully performed, 
	 * 			else <code>false</code>
	 */
	protected boolean tableauxPressedAction(int x, int y){
		for(Tableau tableau : tableaux){ //Check each tableau,
			if(tableau.contains(x, y)){  //and if the mouse clicked a tableau,

				//The cards to be put inUse.
				Stack<Card> cards = tableau.popCardsBelow(y);

				if(!removableFromTableaux(cards)){
					//the cards are not removable so we put them back.
					tableau.appendStack(cards);
					return false; //The action was not performed.
				}

				//The y coordinate of the bottom card that was popped.
				int cardsY = cards.reverseCopy().peek().getY();

				deltaX = x - tableau.getX(); //How off center the click was
				deltaY = y - cardsY;		//relative to the card.

				//Then put all cards below the click in use, if they are suitable.
				inUse.appendStack(cards);

				lastStack = tableau; //And the the tableau becomes the last stack.
			}
		}
		return false; //No tableau was clicked.
	}
	
	/**
	 * 
	 */
	protected boolean removableFromTableaux(Stack<Card> cards){
		return cards != null
				&& Tableau.isVisible(cards)
				&& Tableau.inSequence(cards)
				&& Tableau.alternatesInColor(cards);
	}


	/**
	 * Performs the pressed action methods.
	 */
	@Override
	public void mousePressed(MouseEvent e){
		if(hasWon()){				//If the user has won,
			container.repaint();	//repaint and
			onWin();				//perform the on win action
			return;
		}

		int x = e.getX(), y = e.getY();

		//Short circuit evaluation is used to perform each action if the
		//previous action was not done.
		if(inUse.isEmpty() && !stockPressedAction(x,y) && !wastePressedAction(x,y)){
			tableauxPressedAction(x, y);
		}

	}

	/**
	 * If a tableau in {@link #tableaux} contains the given coordinates and the 
	 * cards in {@link #inUse} increment in value and alternated in color, then
	 * the cards inUse will be appended to the tableau and inUse will be cleared.
	 * 
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action above was performed, 
	 * 			else <code>false</code>
	 */
	protected boolean tableauxReleasedAction(int x, int y){
		for(Tableau tableau : tableaux){ //Check each of the tableaux
			if(tableau.contains(x, y) || tableau.shapeOfNextCard().contains(x, y)){
				//Then we check if the inUse stack can be appended to the
				//tableau per the rules of solitaire.

				try {
					tableau.appendSuitableCards(inUse);
					//This code is not executed if an exception was thrown.
					inUse.clear();
					flipLastStack();
					return true;
				} catch(IllegalArgumentException ex){}
			}
		}
		return false;//If we have reached this point, then no action was performed
	}

	/**
	 * If one foundation in {@link #foundations} contains the given coordinates,
	 * and only one card is in {@link #inUse}, and either:
	 * <ul>
	 * <li>that foundation is empty and that card is an ace, or
	 * <li>that foundation is not empty and that card's value is one more than the
	 * 		value of the top card of the foundation and of the same suit,
	 * </ul>
	 * then that card is added to the foundation and <code>true</code> is returned.
	 * Otherwise nothing is performed and the method returns <code>false</code>.
	 * 
	 * @param x		The x coordinate of a mouse click.
	 * @param y		The y coordinate.
	 * @return <code>true</code> if the action above was performed, 
	 * 			else <code>false</code>
	 */
	protected boolean foundationsReleasedAction(int x, int y){
		if(inUse.isEmpty() || inUse.size() != 1){ //Only 1 card can be added to
			return false;						  //a foundation at a time.
		}
		for(Foundation foundation : foundations){
			//If the foundation was clicked.
			if(foundation.contains(x, y) || (foundation.isEmpty()
					&& foundation.shapeOfNextCard().contains(x, y))){
				try {
					//Peek is used in case the card is not appended.
					foundation.push(inUse.peek());
					//if an exception was not thrown:
					inUse.pop(); //we pop.
					flipLastStack();
					return true; //The action was performed
				} catch(IllegalArgumentException ex){ //If an exception was thrown,
					return false; //we return false as nothing was done.
				}
			}
		}
		return false;
	}

	/**
	 * Calls all of the release action methods. But if no action is performed,
	 * then the cards in {@link #inUse} are returned to {@link #lastStack}
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		if(inUse.isEmpty()){//Then there is nothing to do when the mouse
			return;					 //is released.
		}
		int x = e.getX(), y = e.getY(); //The mouse's location.
		if(!tableauxReleasedAction(x, y) && !foundationsReleasedAction(x, y)){
			//Then no action was performed, so we return the cards to the
			returnToLastStack();	//last stack.
		} else {
			moves++; //A move was made
		}
	}

	/**
	 * Return the cards that are in use to the last stack that was clicked.
	 */
	protected void returnToLastStack(){
		new StackOfCardsAnimator(inUse, lastStack, container);
	}

	/**
	 * Flips the top card of {@link #lastStack} if it is not empty.
	 */
	protected void flipLastStack(){
		if(!lastStack.isEmpty()){ //We unhide the top card
			lastStack.peek().setHidden(false); //of the last stack.
		}
		container.repaint();
	}
	
	/**
	 * Sets the location of the stack {@link #inUse} to the MouseEvent's location.
	 */
	@Override
	public void mouseDragged(MouseEvent e){
		if(inUse != null){//Just move the cards inUse When the mouse is dragged
			inUse.setLocation(e.getX() - deltaX, e.getY() - deltaY);
			container.repaint();                   //and repaint.
		}
	}

	/**
	 * Removes empty elements from the animation queue.
	 */
	protected void updateAnimationQueue(){
		while(!animationQueue.isEmpty()){ //While it has elements.
			if(animationQueue.peek().isEmpty()){//If the front element is empty,
				animationQueue.dequeue();		//remove it.
			} else {							//else it is not empty,
				return;							//so we are done.
			}
		}
	}

	/**
	 * Paints all of the stacks. This should be placed in the container's paint
	 * method.
	 */
	public void paint(Graphics pane){
		if(initialized){
			for(StackOfCards tableau : tableaux){
				tableau.draw(pane);
			}
			for(StackOfCards foundation : foundations){
				foundation.draw(pane);
			}
			if(stock != null && !stock.isEmpty())
				stock.peek().draw(pane);
			if(waste != null && !waste.isEmpty())
				waste.peek().draw(pane);
			if(inUse != null && !inUse.isEmpty())
				inUse.draw(pane);
			
			updateAnimationQueue();
			for(StackOfCards stack : animationQueue){
				if(!stack.isEmpty()){
					stack.draw(pane);
				}
			}
		}
	}

	/**
	 * Determines whether the following winning condition has been met:
	 * <ul>
	 * <li>The stock and waste are both empty.
	 * <li>All cards in the tableaux are not hidden.
	 * <li>Only four or fewer of the tableaux are not empty.
	 * <li>All foundations have at least one card.
	 * </ul>
	 * When these conditions are met, the user has won because all that is done
	 * is to move cards to the foundation without any transfers among the stock,
	 * waste, and tableaux.
	 * @return <code>true</code> if the above condition has been met, else
	 * 			<code>false</code>.
	 */
	protected boolean hasWon(){
		for(Foundation f : foundations){
			if(f.isEmpty()){
				return false; //a foundation is empty so the user hasn't won.
			}
		}
		
		int numOfNonEmptyTableaux = 0; //To check how many tableaux have cards
		for(Tableau tableau : tableaux){ //Checks each tableau if it is suitable.
			
			if(!Tableau.isSuitable(tableau)){ //If any tableaux is not suitable,
				return false;				  //the user has not won.
			} else if(tableau.size() != 0){
				numOfNonEmptyTableaux++;
			}
		}
		//If there are fewer than 4 nonempty tableaux, and the stock and waste
		//are empty, then the user has effectively won.
		return numOfNonEmptyTableaux <= 4 && stock.isEmpty() && waste.isEmpty();
	}

	/**
	 * Alerts the user that s/he has won and plays the winning animation.
	 * Pre. <code>hasWon()</code> returns <code>true</code>.
	 */
	protected void onWin(){
		//We start a new anonymous thread with and anonymous runnable object
		//to play the winning animation.
		new Thread(new Runnable(){
			public void run(){
				winningAnimation();
			}
		}).start();
		
		//Then we show a dialog box to alert the user of the fact.
		//We start another anonymous thread to show the dialog box because
		//the dialog will pause all threads if it is in the main thread.
		new Thread(new Runnable(){
			public void run(){
				JOptionPane.showMessageDialog(container,
						"Congratulations, you won in " + moves + " moves!.");
			}
		}).start();
	}

	/**
	 * Plays the winning animation.
	 * Pre. <code>hasWon()</code> returns <code>true</code>.
	 */
	protected void winningAnimation(){
		//We calculate the number of cards in all of the foundations.
		int sizeOfFoundations = 0;
		for(Foundation f : foundations){
			sizeOfFoundations += f.size();
		}

		while(sizeOfFoundations < 52){ //until all cards are in the foundations.
			//If the animation queue has more than 5 cards, then we wait so as to
			//prevent the program from crashing by creating too many threads at
			if(animationQueue.size() > 6){						//the same time.
				try {
					Thread.sleep(100);
				} catch (InterruptedException e){}
				continue; //try again.
			}
			for(Foundation foundation : foundations){ //For each foundation:
				Card temp = foundation.peek(); //For comparisons.

				for(Tableau tableau : tableaux){
					//If the tableau:
					//-is not empty
					//-its top card's value is one greater than temp
					//-and it has the same suit as temp, then:
					if(!tableau.isEmpty() &&
							temp.compareTo(tableau.peek()) == -1
							&& temp.getSuit() == tableau.peek().getSuit()){

						//move the top card to the foundation and animate it.
						animateTopCardOf(tableau, foundation);
						sizeOfFoundations++;//One more card is in a foundation.
						
						break; //We don't need to look in another tableau.
					}
				}	
			}
		}
	}

	/**
	 * Moves the top card of a source stack to the destination and animates it.
	 * @param source		The stack whose top card is to be moved.
	 * @param destination	The stack to receive the card.
	 */
	protected void animateTopCardOf(StackOfCards source, StackOfCards destination){
		//Holds one of the cards in use for animation.
		StackOfCards temp = new StackOfCards(
				source.getX(), source.peek().getY(), 
				cardWidth, 0, 0);

		temp.push(source.pop()); //Moves a card to the temp.
		animationQueue.enqueue(temp); //and add temp to the queue.
		//Performs the animation.
		new StackOfCardsAnimator(temp, destination, container);
	}

	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
}