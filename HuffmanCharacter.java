
public class HuffmanCharacter{
	
	private byte value; //wartosc znaku w byte
	private int number=0; //ilosc wystapien danego znaku
	private double parabolity=0.0;
	private String codedValue="";
	
	
	HuffmanCharacter(double parabolity){
		this.parabolity = parabolity;
		value=0;
	}
	

	HuffmanCharacter(byte value, int number){
		this.value=value;
		this.number=number;
	}
	
	public void appendCode(String c) {
		this.codedValue=c+codedValue;
	}
	
	public void setParabolity(int size){
		double sizeD=size;
		double numberD=this.number;
		parabolity=numberD/sizeD;
	}
	
	public String getCodedValue() {
		String cv="blad";
		if(codedValue.length()>1){
			try{
				cv = codedValue.substring(0, codedValue.length()-1);
			}catch(Exception e){}
		}else{
			cv=codedValue;
		}
		return cv;
	}
	public byte getValue() {
		return value;
	}

	public void setValue(byte value) {
		this.value = value;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	@Override
	public String toString(){
		return (char)value+" "+parabolity+"\n";
	}
	
	public double getParabolity(){
		return parabolity;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HuffmanCharacter other = (HuffmanCharacter) obj;
		if (value != other.value)
			return false;
		return true;
	}
}
