package solitaire;

import java.awt.Component;

import card.StackOfCards;

public class StackOfCardsAnimator implements Runnable {
	private StackOfCards cards;
	private StackOfCards destination;
	private double x, y;
	private double dx, dy;
	private double accelerationX, accelerationY;
	private Component component;

	public StackOfCardsAnimator(StackOfCards cards, StackOfCards destination,
			Component component){
		this.cards = cards;
		this.destination = destination;
		//目标位置
		int destinationX = (int)destination.shapeOfNextCard().getCenterX();
		int destinationY = (int)destination.shapeOfNextCard().getCenterY();
		this.component = component;
		//算出最近的移动距离
		int deltaX = destinationX - cards.getX();
		int deltaY = destinationY - cards.getY();
		double hyp = Math.sqrt(deltaX*deltaX + deltaY*deltaY);

		dx = deltaX / hyp; 
		dy = deltaY / hyp; 
		accelerationX = dx; 
		accelerationY = dy;

		x = cards.getX();
		y = cards.getY();

		Thread thread = new Thread(this);
		thread.start(); 
	}
	@Override
	public void run(){
		while(!hasArrived()){ 
			x += dx; 
			y += dy;

			dx += accelerationX;
			dy += accelerationY;

			cards.setLocation((int)x, (int)y);

			try {
				Thread.sleep(10); 
			} catch (InterruptedException e){}

			if(component != null){	 
				component.repaint(); 
			}
		}

		try{
			destination.appendStack(cards);
		} catch(IllegalArgumentException e){}
		
		cards.clear();
		component.repaint();
	}

	private boolean hasArrived(){
		boolean arrivedX, arrivedY;
		if(dx < 0){
			arrivedX = cards.getX() <= destination.shapeOfNextCard().getCenterX();
		} else {
			arrivedX = cards.getX() >= destination.shapeOfNextCard().getCenterX();
		}
		if(dy < 0){
			arrivedY = cards.getY() <= destination.shapeOfNextCard().getCenterY();
		} else {
			arrivedY = cards.getY() >= destination.shapeOfNextCard().getCenterY();
		}
		return arrivedX && arrivedY; 
	}
}