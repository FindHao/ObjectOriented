package core;

import java.awt.Point;
import java.util.Random;

/**库函数*/
public class Lib {
	/**初始化int保存*/
	int CardNums[];
	/**初始化分派的牌需要从这个开始分派，其中，getFromIndex是没有分配的。0除外*/
	static int GetFromIndex;
	/**全局的参数*/
	public final static int Space=30;
	public final static int CardHeight=80;
	public final static int CardWidth=50;
	
	
	/**初始化*/
	public Lib() {
		super();
		CardNums=new int[52];
		GetFromIndex=0;
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
	/**初始化dump牌堆*/
	public Card[] getInitCardsForDump(int len,int group){
		Card tempcards[]=new Card[len];
		for(int i=GetFromIndex;i<GetFromIndex+len;i++){
			tempcards[i-GetFromIndex]=new Card(CardNums[i]%13+1, CardNums[i]/13, false, Lib.getPointForGroup(group), group);
		}
		GetFromIndex+=len;
		return tempcards;
	} 
	
	
	/**返回每个组的位置*/
	public static Point getPointForGroup(int group){
		/**第一行的高度y，第二行的高度y2*/
		int height1=Space,height2=Space*2+CardHeight;
		switch(group){
		case 1:return new Point(Space,height1);
		case 2:return new Point(Space*2+CardWidth,height2);
		case 3:return new Point(Space*3+CardWidth*2,height2);
		case 4:return new Point(Space*4+CardWidth*3,height2);
		case 5:return new Point(Space*5+CardWidth*4,height2);
		case 6:return new Point(Space*6+CardWidth*5,height2);
		case 7:return new Point(Space*7+CardWidth*6,height2);
		case 8:return new Point(Space*7+CardWidth*6,height1);
		case 9:return new Point(Space*6+CardWidth*5,height1);
		case 10:return new Point(Space*4+CardWidth*3,height1);
		case 11:return new Point(Space*3+CardWidth*2,height1);
		case 12:return new Point(Space*2+CardWidth*1,height1);
		case 13:return new Point(Space*1+CardWidth*0,height1);
		}
		return null;
	}
	
}
