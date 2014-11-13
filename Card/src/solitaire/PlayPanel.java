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
	/** 下面七个牌堆 */
	protected Tableau[] tableaux;
	/** 上面四个排好的 */
	protected Foundation[] foundations;
	/** 上面那个还没有翻开的牌堆 */
	protected StackOfCards stock;
	/** 已经翻开的牌 */
	protected StackOfCards waste;
	/** 准备要移动的牌 */
	protected StackOfCards inUse;
	/** 选中的要移动的牌的起始堆 */
	protected StackOfCards lastStack;
	protected boolean initiallyHidden = true;
	/** 校验位检验是否初始化完毕 */
	protected boolean initialized;
	/** 牌的宽度 */
	protected int cardWidth;
	// 以下是界面的调整
	protected int offset;
	protected int yCoord;
	/** 选中的要移动的牌数 */
	protected int moves;
	protected int deltaX, deltaY;
	protected Container container;

	protected Queue<StackOfCards> animationQueue;

	public Klondike() {
	}

	public Klondike(Container container) {
		this.container = container;
		container.addMouseListener(this);
		container.addMouseMotionListener(this);
		container.setBackground(new Color(0, 180, 0)); // 绿色背景
		container.setSize(600, 650);
		container.setPreferredSize(container.getSize());
		yCoord = container.getHeight() / 12;
		cardWidth = 60;
		offset = cardWidth / 2;
		inUse = new StackOfCards(0, 0, cardWidth, 0, offset * 3 / 2);
		animationQueue = new Queue<StackOfCards>();
		init();
	}

	protected void init() {
		// 初始化一个用来分配的牌堆
		StackOfCards deck = StackOfCards.randomDeck();
		// 初始化那七堆牌
		initTableaux(deck, new int[] { 1, 2, 3, 4, 5, 6, 7 });
		// 初始化提供牌堆的牌堆
		initStockAndWaste(deck);
		// 初始化四个用来接收的牌堆
		initFoundations(4);
		// 确定初始化成功
		initialized = true;
		container.repaint();
	}

	/** 初始化七个牌堆 */
	protected void initTableaux(StackOfCards source, int[] initialTableauxSizes) {
		tableaux = new Tableau[initialTableauxSizes.length];
		for (int i = 0; i < tableaux.length; i++) {
			tableaux[i] = new Tableau((cardWidth + 10) * (i + 1), yCoord
					+ cardWidth * 2, cardWidth, offset);
			for (int j = 0; j < initialTableauxSizes[i]; j++) {
				tableaux[i].push(source.pop());
				tableaux[i].peek().setHidden(initiallyHidden);
			}
		}
		// 牌堆顶部的牌反转过来
		for (StackOfCards stack : tableaux) {
			stack.peek().setHidden(false);
		}
	}

	protected void initStockAndWaste(StackOfCards deck) {
		stock = new StackOfCards(cardWidth + 10, yCoord, cardWidth, 0, 0);
		stock.appendStack(deck);
		stock.peek().setHidden(true);
		waste = new StackOfCards(2 * (stock.getX()), yCoord, cardWidth, 0, 0);
	}

	protected void initFoundations(int numOfFoundations) {
		foundations = new Foundation[numOfFoundations];
		for (int i = 0; i < foundations.length; i++) {
			foundations[i] = new Foundation(
					tableaux[tableaux.length - i - 1].getX(), yCoord, cardWidth);
		}
	}

	protected boolean stockPressedAction(int x, int y) {
		// 先判断是否在范围内
		if (stock.contains(x, y)) {
			waste.push(stock.pop()); // 移牌
			waste.peek().setHidden(false);// 展示

			if (!stock.isEmpty())
				stock.peek().setHidden(true);
			moves++; // 移动步数
			container.repaint();
			return true;
			//这里是因为有空的情况
		} else if (stock.shapeOfNextCard().contains(x, y)) {
			// 点击空的发牌堆将会把waste牌堆重新反过来一次
			stock.appendStack(waste.reverseCopy());
			waste.clear();

			if (!stock.isEmpty()) {
				stock.peek().setHidden(true); 
				moves++; 
			}
			container.repaint();
			return true; 
		}
		return false; 
	}

	protected boolean wastePressedAction(int x, int y) {
		if (waste.contains(x, y)) {
			inUse.push(waste.pop());
			lastStack = waste; 
			moves++;
			return true;
		}
		return false; 
	}

	protected boolean tableauxPressedAction(int x, int y) {
		for (Tableau tableau : tableaux) { 
			if (tableau.contains(x, y)) { 
				Stack<Card> cards = tableau.popCardsBelow(y);
				if (!removableFromTableaux(cards)) {
					//不符合条件
					tableau.appendStack(cards);
					return false;
				}
				int cardsY = cards.reverseCopy().peek().getY();
				deltaX = x - tableau.getX(); 
				deltaY = y - cardsY; 
				inUse.appendStack(cards);
				lastStack = tableau;
			}
		}
		return false; 
	}
	//判断是否可以移动
	protected boolean removableFromTableaux(Stack<Card> cards) {
		return cards != null && Tableau.isVisible(cards)
				&& Tableau.inSequence(cards)
				&& Tableau.alternatesInColor(cards);
	}
	public void mousePressed(MouseEvent e) {
		if (hasWon()) { 
			container.repaint();
			onWin(); 
			return;
		}

		int x = e.getX(), y = e.getY();
		if (inUse.isEmpty() && !stockPressedAction(x, y)
				&& !wastePressedAction(x, y)) {
			tableauxPressedAction(x, y);
		}

	}
	/**右键在7个牌堆上释放时的事件*/
	protected boolean tableauxReleasedAction(int x, int y) {
		for (Tableau tableau : tableaux) { 
			if (tableau.contains(x, y)
					|| tableau.shapeOfNextCard().contains(x, y)) {
				try {
					tableau.appendSuitableCards(inUse);
					inUse.clear();
					flipLastStack();
					return true;
				} catch (IllegalArgumentException ex) {
				}
			}
		}
		return false;
	}

	/**当鼠标在收牌的堆上放置的事件*/
	protected boolean foundationsReleasedAction(int x, int y) {
		if (inUse.isEmpty() || inUse.size() != 1) { 
			return false; 
		}
		for (Foundation foundation : foundations) {
			if (foundation.contains(x, y)
					|| (foundation.isEmpty() && foundation.shapeOfNextCard()
							.contains(x, y))) {
				try {
					foundation.push(inUse.peek());
					inUse.pop(); 
					flipLastStack();
					return true; 
				} catch (IllegalArgumentException ex) { 
					return false; 
				}
			}
		}
		return false;
	}

	/**鼠标事件，如果没有事件相应，那么牌将被退回到原来的牌堆*/
	public void mouseReleased(MouseEvent e) {
		if (inUse.isEmpty()) {
			return; 
		}
		int x = e.getX(), y = e.getY(); 
		if (!tableauxReleasedAction(x, y) && !foundationsReleasedAction(x, y)) {
			returnToLastStack(); 
		} else {
			moves++; 
		}
	}

	/**回退	 */
	protected void returnToLastStack() {
		new StackOfCardsAnimator(inUse, lastStack, container);
	}

	protected void flipLastStack() {
		if (!lastStack.isEmpty()) { 
			lastStack.peek().setHidden(false); 
		}
		container.repaint();
	}
	//拖动事件
	public void mouseDragged(MouseEvent e) {
		if (inUse != null) {
			inUse.setLocation(e.getX() - deltaX, e.getY() - deltaY);
			container.repaint(); 
		}
	}

	protected void updateAnimationQueue() {
		while (!animationQueue.isEmpty()) { 
			if (animationQueue.peek().isEmpty()) {
				animationQueue.dequeue(); 
			} else { 
				return; 
			}
		}
	}


	public void paint(Graphics pane) {
		if (initialized) {
			for (StackOfCards tableau : tableaux) {
				tableau.draw(pane);
			}
			for (StackOfCards foundation : foundations) {
				foundation.draw(pane);
			}
			if (stock != null && !stock.isEmpty())
				stock.peek().draw(pane);
			if (waste != null && !waste.isEmpty())
				waste.peek().draw(pane);
			if (inUse != null && !inUse.isEmpty())
				inUse.draw(pane);
			updateAnimationQueue();
			for (StackOfCards stack : animationQueue) {
				if (!stack.isEmpty()) {
					stack.draw(pane);
				}
			}
		}
	}


	protected boolean hasWon() {
		for (Foundation f : foundations) {
			if (f.isEmpty()) {
				return false; 
			}
		}
		int numOfNonEmptyTableaux = 0; 
		for (Tableau tableau : tableaux) {
			if (!Tableau.isSuitable(tableau)) { 
				return false; 
			} else if (tableau.size() != 0) {
				numOfNonEmptyTableaux++;
			}
		}
		return numOfNonEmptyTableaux <= 4 && stock.isEmpty() && waste.isEmpty();
	}
	protected void onWin() {
		new Thread(new Runnable() {
			public void run() {
				winningAnimation();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(container,
						"Congratulations, you won in " + moves + " moves!.");
			}
		}).start();
	}


	protected void winningAnimation() {
		int sizeOfFoundations = 0;
		for (Foundation f : foundations) {
			sizeOfFoundations += f.size();
		}

		while (sizeOfFoundations < 52) { 
			if (animationQueue.size() > 6) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				continue; 
			}
			for (Foundation foundation : foundations) { 
				Card temp = foundation.peek(); 

				for (Tableau tableau : tableaux) {
					if (!tableau.isEmpty()
							&& temp.compareTo(tableau.peek()) == -1
							&& temp.getSuit() == tableau.peek().getSuit()) {
						animateTopCardOf(tableau, foundation);
						sizeOfFoundations++;
						break; 
					}
				}
			}
		}
	}

	protected void animateTopCardOf(StackOfCards source,
			StackOfCards destination) {
		StackOfCards temp = new StackOfCards(source.getX(), source.peek()
				.getY(), cardWidth, 0, 0);
		temp.push(source.pop()); 
		animationQueue.enqueue(temp); 
		new StackOfCardsAnimator(temp, destination, container);
	}
	public void mouseEntered(MouseEvent e) {	}
	public void mouseExited(MouseEvent e) {	}
	public void mouseClicked(MouseEvent e) {	}
	public void mouseMoved(MouseEvent e) {	}
}