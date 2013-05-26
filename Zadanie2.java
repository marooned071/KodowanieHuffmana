import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Zadanie2 {

	public static void main(String[] args) {
		String fileName;
		try{
			fileName= args[0];
		}catch(Exception e){
			System.out.println("Brak nazwy pliku!\nZadanie2 nazwaPliku");
			return;
		}
		
		new Zadanie2(fileName);
	}
	
	private ArrayList<HuffmanCharacter> characterList; //lista ze znakami
	private int charactersInFile; //liczba znakow w pliku
	
	Zadanie2(String fileName){
		byte[] fileByte = fileToByteArray(fileName);
		charactersInFile = fileByte.length;
		Map<Byte, Integer> map = getHashMap(fileByte);

		this.characterList= mapToSortedArrayList(map);
		setParabolity();
		
		ArithmeticCoding ac = new ArithmeticCoding(characterList);
		String code =ac.encode(fileByte);
		ArrayList<HuffmanCharacter> huffCharacterList = huffmanAlgorithm();
		System.out.println("Zakodowany ciag:\n"+code+"\n");
		double[] bounds = ac.getBounds();
		System.out.println("'0.zakodowany ciag' zawiera sie w przedziale : \n[ "+bounds[0]+"  ;  "+bounds[1]+" )");
		double entropy = getEntropy(huffCharacterList);
		System.out.printf("%s %,(.3f %s" , "Entropia : ", entropy,  "\n");
		double averageHuff = getAverageHuffmanLength(huffCharacterList);
		System.out.printf("%s %,(.3f %s" , "Srednia dlugosc Kodu Huffmana : ", averageHuff,  "\n");
		System.out.printf("%s %3d %s" , "Dlugosc pliku wejsciowego (bajty) : ", fileByte.length,  "\n");
		double averageArith = ((double)code.length())/8.0;
		System.out.printf("%s %,(.3f %s" , "Dlugosc obliczonego kodu (bajty) : ", averageArith,  "\n");
		double compressionRate =  ((double)code.length())/((double)fileByte.length*8.0);
		System.out.printf("%s %,(.3f %s" , "Wspolczynnik kompresji : ", compressionRate*100,  "%\n");
	}
	
	
	public void printByteArray(byte[] byteArray){
		for(int i=0;i<byteArray.length;i++){
			System.out.println(i+" "+byteArray[i]+" "+ (char) byteArray[i]);
			if(byteArray[i]<0){
				break;
			}
		}
	}
	
	private ArrayList<HuffmanCharacter> huffmanAlgorithm(){
		List<HuffmanTree> treeList = new ArrayList<HuffmanTree>();
		
		Iterator<HuffmanCharacter> it= characterList.iterator();
		while(it.hasNext()){
			treeList.add(new HuffmanTree(it.next()));
		}
		
		HuffmanTree rootTree;
		HuffmanTree t1;
		HuffmanTree t2;
		
		while(treeList.size()>1){
			Collections.sort(treeList);
			t1 = treeList.remove(0);
			t2 = treeList.remove(0);
			rootTree= new HuffmanTree(new HuffmanCharacter(t1.getHc().getParabolity()+t2.getHc().getParabolity()));
			rootTree.addLeft(t1);
			rootTree.addRight(t2);
			t1.setCode("0");
			t2.setCode("1");
			treeList.add(rootTree);			
		}
		return treeList.get(0).getCharactersList(treeList.get(0));
	}
	
	private double getAverageHuffmanLength(ArrayList<HuffmanCharacter> huffList){
		double average=0.0;
		
		for(HuffmanCharacter hc : huffList){
			average+= hc.getParabolity()*hc.getCodedValue().length();
		}
		
		return average;
	}
	
	
	private double getEntropy(ArrayList<HuffmanCharacter> huffList){
		double entropy = 0.0;
		double parab;
		
		for(HuffmanCharacter ch: huffList){
			parab= ch.getParabolity();
			entropy+=(-1)*parab*(Math.log(parab)/Math.log(2));
		}
		return entropy;
	}
	
	/**
	 * Zwraca posortowana Array Liste z Mapy<bajt, liczba wystapien>
	 */
	private ArrayList<HuffmanCharacter> mapToSortedArrayList(Map<Byte, Integer> map){
		List<HuffmanCharacter> arrayList = new ArrayList<HuffmanCharacter>();
		for(Map.Entry<Byte, Integer> entry : map.entrySet()){
			arrayList.add(new HuffmanCharacter(entry.getKey(), entry.getValue()));
		}
		Collections.sort(arrayList, new Comparator<HuffmanCharacter>(){

			@Override
			public int compare(HuffmanCharacter o1, HuffmanCharacter o2) {
				return o2.getNumber()-o1.getNumber();
			}
			
		});
		
		return (ArrayList<HuffmanCharacter>) arrayList;
	}
	
	private void setParabolity(){
		for(HuffmanCharacter ch: characterList){
			ch.setParabolity(charactersInFile);
		}
	}
	
	/**
	 * Zwraca hashMape z bajtow oraz ilosci ich wystapien 
	 * @param byteArray
	 * @return   
	 */
	private Map<Byte, Integer> getHashMap(byte[] byteArray){
		Map<Byte, Integer> map= new HashMap<Byte, Integer>();	
		for(int i=0;i<byteArray.length;i++){
			if(map.containsKey(byteArray[i])){
				map.put(byteArray[i], map.get(byteArray[i])+1);
			}
			else{
				map.put(byteArray[i], 1);
			}
		}
		return map;	
	}
	
	private byte[] fileToByteArray(String fileName){	
		FileInputStream fileInputStream=null;
		File file = new File(fileName);
		byte[] data=new byte[(int) file.length()];
		try{
		    fileInputStream = new FileInputStream(file);
		    fileInputStream.read(data);
		    fileInputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		} 
	    return data;
	}
	
}

