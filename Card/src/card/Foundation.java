package card;
/**上面四个牌堆，用来存放相同花色 */
public class Foundation extends StackOfCards {
	public Foundation(){}
	public Foundation(int x, int y, int cardWidth){
		super(x, y, cardWidth, 0, 0);
	}
	@Override
	public void push(Card card){
		if(isEmpty()){                
			if(card.getValue() == 1){ 
				super.push(card);
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			if(card.getValue() == peek().getValue() + 1 
					&& card.getSuit() == peek().getSuit()){
				super.push(card);
			} else {
				throw new IllegalArgumentException();
			}
		}
	}	
}