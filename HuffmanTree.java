import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class HuffmanTree implements Comparable<HuffmanTree>{

	private HuffmanTree root = this;
	private HuffmanTree right = null;
	private HuffmanTree left = null;
	private HuffmanCharacter hc = null;
	
	HuffmanTree(HuffmanCharacter hc){
		this.hc=hc;
	}
	
	public void addLeft(HuffmanTree hf){
		this.left = hf;
	}
	
	public void addRight(HuffmanTree hf){
		this.right = hf;
	}
	
	
	public void setCode(String c){
		getHc().appendCode(c);
		addCodeToSons(this,c);

	}
	
	private void addCodeToSons(HuffmanTree x,String c){
		if(x!=null){
			addCodeToSons(x.getLeft(),c);
			x.getHc().appendCode(c);
			addCodeToSons(x.getRight(),c);
		}
	}
	
	public void inOrderWalk(HuffmanTree x){
		if(x!=null){
			inOrderWalk(x.getLeft());
			System.out.println(x.getHc().getParabolity()+" "+x.getHc().getValue()+"\n");
			inOrderWalk(x.getRight());
		}
	}
	
	public void printLevelOrder(HuffmanTree root) {
		if (root == null)
			return;

		Queue<HuffmanTree> nodesQueue = new LinkedList<HuffmanTree>();
		int nodesInCurrentLevel = 1;
		int nodesInNextLevel = 0;

		nodesQueue.offer((root));
		HuffmanTree currNode;
		while (!nodesQueue.isEmpty()) {
			currNode = nodesQueue.poll();
			nodesInCurrentLevel--;
			if (currNode != null) {
				char ch = (char) currNode.getHc().getValue();			
				System.out.print(currNode.getHc().getParabolity()+ " " + ch + " " + "code: "+currNode.getHc().getCodedValue()+" ||");
				nodesQueue.offer(currNode.getLeft());
				nodesQueue.offer(currNode.getRight());
				nodesInNextLevel += 2;
			}
			else{
				System.out.print(" null ");
			}
			if (nodesInCurrentLevel == 0) {
				System.out.println();
				nodesInCurrentLevel = nodesInNextLevel;
				nodesInNextLevel = 0;
			}
		}
	}
	
	public ArrayList<HuffmanCharacter> getCharactersList(HuffmanTree root) {
		
		ArrayList<HuffmanCharacter> list = new ArrayList<HuffmanCharacter>();
		
		if (root == null)
			return null;

		Queue<HuffmanTree> nodesQueue = new LinkedList<HuffmanTree>();
		int nodesInCurrentLevel = 1;
		int nodesInNextLevel = 0;

		nodesQueue.offer((root));
		HuffmanTree currNode;
		while (!nodesQueue.isEmpty()) {
			currNode = nodesQueue.poll();
			nodesInCurrentLevel--;
			if (currNode != null) {
				
				if(currNode.getHc().getValue()!=0){
					list.add(currNode.getHc());
				}

				nodesQueue.offer(currNode.getLeft());
				nodesQueue.offer(currNode.getRight());
				nodesInNextLevel += 2;
			}
			if (nodesInCurrentLevel == 0) {
				nodesInCurrentLevel = nodesInNextLevel;
				nodesInNextLevel = 0;
			}
		}
		
		return list;
	}



	@Override
	public int compareTo(HuffmanTree o) {
		int i1 = (int)(o.getHc().getParabolity()*1000);
		int i2 = (int)(this.getHc().getParabolity()*1000);
		return i2-i1;
	}

	public HuffmanTree getRoot() {
		return root;
	}

	public void setRoot(HuffmanTree root) {
		this.root = root;
	}

	public HuffmanTree getRight() {
		return right;
	}

	public void setRight(HuffmanTree right) {
		this.right = right;
	}

	public HuffmanTree getLeft() {
		return left;
	}

	public void setLeft(HuffmanTree left) {
		this.left = left;
	}

	public HuffmanCharacter getHc() {
		return hc;
	}

	public void setHc(HuffmanCharacter hc) {
		this.hc = hc;
	}
	
	public String toString(){
		return (char)hc.getValue()+" "+hc.getParabolity();
		
	}
}
