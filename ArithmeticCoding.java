import java.util.List;


public class ArithmeticCoding {
	
	private double lowBound;
	private double upBound;
	private int byteCounter;

	private double l2;
	private double u2;
	
	private double[][] rangeTab = new double[256][2];
	
	private List<HuffmanCharacter> characterList;
	
	ArithmeticCoding(List<HuffmanCharacter> characterList){
		this.characterList=characterList;
		makeRangeTab();
	}

	
	public String encode(byte[] byteArray){
		
		lowBound=0.0; //ograniczenie dolne na ktorym dzialja metody algorytmu
		upBound=1.0;
		
		byteCounter=0;
		String code="";
		byte x;

		double range =0.0;
		
		l2=0.0; //ograniczenie dole sprawdzajace poprawnosc 
		u2=1.0;
		
		double range2=0.0;
		
		for(int i=0;i<byteArray.length;i++){
			x=byteArray[i];
			
//			System.out.println("L: = "+lowBound+" \nR = "+upBound);
			
			range = upBound - lowBound;
			range2 = u2- l2;
			
//			System.out.println("Range: "+range);
//			System.out.println("Rt(l) = "+ rangeTab[x][0]+" RT(r)= "+rangeTab[x][1]);
//			System.out.println("Range*x[0] "+ (range * rangeTab[x][0]));
//			System.out.println("Range*x[1] "+ (range * rangeTab[x][1]));
			upBound = lowBound + (range * rangeTab[x][1]);
			lowBound = lowBound + (range * rangeTab[x][0]);
			
			u2 = l2 + (range2 * rangeTab[x][1]);
			l2 = l2 + (range2 * rangeTab[x][0]);
			
//			System.out.println("L: = "+lowBound+" \nR = "+upBound);
//			System.out.println("--- 2["+l2+" "+u2+" )");

			code+=getBytes();			
			
//			System.out.println();
		}
		
		code+=endArithmeticCoding();
		
//		System.out.println("["+lowBound+" "+upBound+" )");	
//		System.out.println("--- 2["+l2+" "+u2+" )");	
//		System.out.println("Code: "+code);
		
		return code;
	}
	
	private String endArithmeticCoding(){
		String code="";
		if (byteCounter > 0) {
			code += "1";
			for (int a = 0; a < byteCounter; a++)
				code += "0";
		}
		return code;
	}
	
	private String getBytes(){
		String code="";
		while(true){
			if(lowBound >= 0.0 && upBound < 0.5){
				code += "0";
				for (int i = 0; i < byteCounter; i++)
					code += "1";
				byteCounter = 0;
				lowBound = 2 * lowBound;
				upBound = 2 * upBound;

			}
			else if(lowBound >= 0.5 && upBound<1.0 ){
				code += "1";
				for (int i = 0; i < byteCounter; i++)
					code += "0";
				byteCounter = 0;
				lowBound = 2 * lowBound - 1;
				upBound = 2 * upBound - 1;

			}
			else if(lowBound>=0.25 && upBound<0.75){
				byteCounter++;
				lowBound = 2 * lowBound - 0.5;
				upBound = 2 * upBound - 0.5;

			}
			else{
				break;
			}	
		}

		return code;
	}
	
	public double[] getBounds(){
		double[] bounds = new double[2];
		bounds[0] = l2;
		bounds[1] = u2;
		return bounds;
	}
	
	private void makeRangeTab(){
		double parab=0.0;
		int i=0;
		for(HuffmanCharacter ch: characterList){
			if(rangeTab[ch.getValue()][0]==0.0){
				rangeTab[ch.getValue()][0]=parab;
				rangeTab[ch.getValue()][1]=ch.getParabolity()+parab;
//				System.out.println(i+" "+ (char) ch.getValue() + " "+rangeTab[ch.getValue()][0]+" "+rangeTab[ch.getValue()][1] );
				i++;
				parab+=ch.getParabolity();
			}
			
		}
	}
}

// BEZ SKALOWANIA V

//private String endArithmeticCoding(){
//	String code="";
//	byteCounter++;
//	if(l<0.25){
//		code += "0";
//		for (int i = 0; i < byteCounter; i++)
//			code += "1";
//	}
//	else{
//		code += "1";
//		for (int i = 0; i < byteCounter; i++)
//			code += "0";
//	}
//	return code;
//}
//
//private String getBytes(){
//	String code="";
//	while(true){
//		if(l >= 0.0 && r < 0.5){
////			System.out.println("1 "+l+" "+r);
//			code += "0";
//			for (int i = 0; i < byteCounter; i++)
//				code += "1";
//			byteCounter = 0;
//
//		}
//		else if(l >= 0.5 && r<1.0 ){
////			System.out.println("2");
//			code += "1";
//			for (int i = 0; i < byteCounter; i++)
//				code += "0";
//			byteCounter = 0;
//			l-=0.5;
//			r-=0.5;
//
//		}
//		else if(l>=0.25 && r<0.75){
////			System.out.println("3");
//			byteCounter++;
//			l-=0.25;
//			r-=0.25;
//
//		}
//		else{
////			System.out.println("break");
//			break;
//		}	
////		l*=2;c
//		l=2*l;
//		r=2*r;
////		r*=2;
////		r = 2 * r+1;
//
//	}
//
//
//	return code;
//}

