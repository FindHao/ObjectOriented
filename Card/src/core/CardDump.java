package core;
/**7个牌堆*/
public class CardDump {
	private Card cards[];
	/**牌堆中牌数*/
	private int len;
	/**牌堆中第一张翻开的牌的index<br>
	 * 每次加入牌或者移出牌都要更新
	 * */
	private int openIndex;
	/**组类*/
	private int group;
	public CardDump(int group) {
		//下面每堆牌最多有13+7-1个
		cards=new Card[20];
		len=0;
		openIndex=0;
		this.group=group;
	}
	/**添加一张牌*/
	public void push(Card card){
		cards[len++]=card;
	}
	/**添加多张牌
	 * @param card 长度不是预先固定的，里面肯定是满的*/
	public void push(Card card[]){
		for(Card tempcard:card){
			cards[len++]=tempcard;
		}
	}
	/**弹出牌<br>
	 * @param chooseIndex 从chooseindex到len-1的牌都弹出来*/
	public Card[] pop(int chooseIndex){
		int templen=len-chooseIndex;
		/**存储选中的牌*/
		Card anscards[]=new Card[templen];
		for(int i=0;i<templen;i++){
			anscards[i]=cards[i];
		}
		return anscards;
	}
	/**初始化牌堆
	 * @param initCards传入的初始化的牌堆
	 * 记得要翻开一张牌
	 * */
	public void Initialize(Card initCards[]){
		int templen=initCards.length;
		len=templen;
		for(int i=0;i<templen;i++){
			cards[i]=initCards[i];
		}
		cards[len-1].setOpen(true);
		openIndex=len-1;
	}
	public int getGroup(){
		return group;
	}
}
