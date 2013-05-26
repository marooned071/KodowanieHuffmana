import java.util.Random;


public class Zadanie3 {


	public static void main(String[] args) {
		new Zadanie3(100);

	}
	
	
	private double[] randomTab;
	
	Zadanie3(int length){
		randomTab = new double[length];
		fillRandomTab();
		printRandomTab();
	}
	
	private void fillRandomTab(){
		double en;
		Random random = new Random();
		randomTab[0]=random.nextGaussian()+random.nextGaussian();
		for(int i=1;i<randomTab.length;i++){
			en= random.nextGaussian();
			randomTab[i]=(0.9*randomTab[i-1])+en;
		}
	}
	private void printRandomTab(){
		for(int i=0;i<randomTab.length;i++){
			System.out.println(i+" "+randomTab[i]);
		}
	}

}
