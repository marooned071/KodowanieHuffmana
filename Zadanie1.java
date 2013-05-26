import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Zadanie1 {

	public static void main(String[] args) {
		String fileName;
		boolean printChars= false;
		try{
			fileName=args[0];
		}catch(Exception e){
			System.out.println("Blad!\nZadanie1 plik -l\nl - czy wypisywac litery");
			return;
		}
		try{
			if(args[1].equals("-l"))
				printChars=true;
		}catch(Exception e){
		}
		
		new Zadanie1(fileName,printChars);

	}
	
	private List<HuffmanCharacter> characterList; //lista ze znakami
	private int charactersInFile; //liczba znakow w pliku
	
	
	Zadanie1(String fileName, boolean printChars){
		Map<Byte, Integer> map = getHashMapFormFile(fileName);

		this.characterList= mapToSortedArrayList(map);
		setParabolity();
		
		ArrayList<HuffmanCharacter> characterList = huffmanAlgorithm();
		Collections.sort(characterList, new Comparator<HuffmanCharacter>(){

			@Override
			public int compare(HuffmanCharacter o1, HuffmanCharacter o2) {
				return o2.getNumber()-o1.getNumber();
			}
			
		});
		
		double entropy = getEntropy(characterList);
		double average = getAverageHuffmanLength(characterList);
		double compressionRate =  getCompressionRate(characterList);
		
		if(printChars){
			for(HuffmanCharacter hc : characterList){
				System.out.printf("%s%s%s %6d %,3f %10s %s" ,"'",(char)hc.getValue(),"'", hc.getNumber(), hc.getParabolity(), hc.getCodedValue() , "\n");
			}
		}
		
		System.out.println("Plik : "+fileName);
		System.out.println("Wielkosc: : "+charactersInFile);
		System.out.printf("%s %,(.3f %s" , "Entropia : ", entropy,  "\n");
		System.out.printf("%s %,(.3f %s" , "Srednia dlugosc Kodu Huffmana : ", average,  "\n");
		System.out.printf("%s %,(.3f %s" , "Wspolczynnik sredniej dlugosci kodu Huffmana do entropii: ", average/entropy,  "\n");
		System.out.printf("%s %,(.3f %s" , "Efektywnosc kodu Huffmana: ", entropy/average*100,  "%\n");
		System.out.printf("%s %,(.3f %s" , "Wspolczynnik kompresji: ", compressionRate*100,  "%\n");

	}
	
	private double getCompressionRate(ArrayList<HuffmanCharacter> huffList){
		BigDecimal cr= new BigDecimal("0");
		
		BigDecimal before = new BigDecimal(charactersInFile);
		before=before.multiply(new BigDecimal(8));
		BigDecimal after= new BigDecimal(0);
		for(HuffmanCharacter hc : huffList){
			after=after.add(new BigDecimal(hc.getCodedValue().length()*hc.getNumber()+""));
		}
		cr=after.divide(before,10,BigDecimal.ROUND_HALF_EVEN);

		return cr.doubleValue();
	}
	
	/**
	 * Srednia dlugosc kodu Huffmana
	 * @param huffList
	 * @return
	 */
	private double getAverageHuffmanLength(ArrayList<HuffmanCharacter> huffList){
		double average=0.0;
		
		for(HuffmanCharacter hc : huffList){
			average+= hc.getParabolity()*hc.getCodedValue().length();
		}
		
		return average;
	}
	
	private double getEntropy(){
		double entropy = 0.0;
		double parab;
		for(HuffmanCharacter ch: characterList){
			parab= ch.getParabolity();
			entropy+=(-1)*parab*(Math.log(parab)/Math.log(2));
		}
		return entropy;
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
	
	
	
	/**
	 * W kazdym znaku characterList ustawia prawdopodobienstwo
	 */
	private void setParabolity(){
		for(HuffmanCharacter ch: characterList){
			ch.setParabolity(charactersInFile);
		}
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
	
	private Map<Byte, Integer> getHashMapFormFile(String fileName){
		File file = new File(fileName);
		Map<Byte, Integer> map= new HashMap<Byte, Integer>();
		
		int counter = 0;
		
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ios = new FileInputStream(file);
	        int read = 0;
	      
	        while ( (read = ios.read(buffer)) != -1 ) {
	            counter+=read;
	            for(int i=0;i<read;i++){
	    			if(map.containsKey(buffer[i])){
	    				map.put(buffer[i], map.get(buffer[i])+1);
	    			}
	    			else{
	    				map.put(buffer[i], 1);
	    			}
	            }
	        }
	        
	    } catch (Exception e) {
			e.printStackTrace();
		} finally { 
	        try {
	             if ( ios != null ) 
	                  ios.close();
	        } catch ( IOException e) {
	        	e.printStackTrace();
	        }
	    }
	    
	    this.charactersInFile=counter;
	    
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
