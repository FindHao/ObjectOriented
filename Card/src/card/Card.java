package card;
/**纸牌类 * */
public class Card {
	/**纸牌上的数值*/
	private int Num;
	/**花色	 0:红桃<br>	 1：方片 <br> 2：梅花 <br>  3：黑桃  <br>	 * */
	private int Color;
	/**牌是否正面朝上*/
	private boolean isOpen;
	
	/**获取数值*/
	public  int getNum(){
		return Num;
	};
	/**获取花色*/
	public  int getColor(){
		return Color;
	};
	/**设置牌的翻开状态*/
	public void setOpen(boolean isOpen){
		this.isOpen=isOpen;
	};
	/**设置牌的数值*/
	public void setNum(int aNum){
		Num=aNum;
	};
	/**设置牌的花色*/
	public void setColor(int color){
		Color=color;
	}
	
}
