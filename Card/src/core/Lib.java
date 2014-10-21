package core;

import java.util.Random;

/**库函数*/
public class Lib {
	
	int CardNums[];
	/**初始化*/
	public Lib() {
		super();
		CardNums=new int[52];
	}
	/**初始化数值，并打乱顺序*/
	public void initializeNums(){
		//约定从0-51fffff
		for(int i=0;i<52;i++){
			CardNums[i]=i;
		}
		for(int i=0;i<100;i++){
			Random rand = new Random(System.currentTimeMillis());
			int id1=rand.nextInt(51)+1,id2=rand.nextInt(51)+1;
			int temp=CardNums[id1];
			CardNums[id1]=CardNums[id2];
			CardNums[id2]=temp;
		}
	}
	public Card getInitCardsForDump(){
		Card tempcards[]=new Card[24];
		for(int i=0;i<24;i++){
			tempcards[i]=new Card(CardNums[0]%13+1, color, isopen, pointer, group)
		}
		
		return tempcards;
	} 
}
