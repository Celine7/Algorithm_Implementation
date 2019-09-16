//Celine Cui
//3.2.2019
import java.text.StringCharacterIterator;

class PQNode{
	private PQNode child;
	private PQNode neighbor;
	private char value;
	private CarPQ pq;

	public PQNode(char val){
		value = val;
	}

	public char getVal(){
		return value;
	}
	public void setVal(char val){
		value = val;
	}

	public PQNode getChild(){ return child; }
	public void setChild(PQNode child){ this.child = child; }

	public PQNode getNeighbor(){
		return neighbor;
	}
	public void setNeighbor(PQNode neighbor){
		this.neighbor = neighbor;
	}

	public CarPQ getPQ(){
		return pq;
	}
	public void setPQ(CarPQ pq){
		this.pq = pq;
	}
}

public class PQDLB{
	private PQNode root;

	public boolean insert(String word, CarPQ carPQ){
		if(word == null) return false;

		StringCharacterIterator it = new StringCharacterIterator(word);

		if(root == null){
			root = new PQNode(it.current());

			PQNode curr = root;
			it.next();

			while(it.getIndex() < it.getEndIndex()){
				PQNode newPQNode = new PQNode(it.current());
				curr.setChild(newPQNode);
				curr = curr.getChild();

				it.next();
			}

			curr.setChild(new PQNode('$'));
			if(carPQ != null){
				curr = curr.getChild();
				curr.setPQ(carPQ);
			}
		} else{
			PQNode curr = root;

			while(it.getIndex() < it.getEndIndex()){
				while(it.current() != curr.getVal()){
					if(curr.getNeighbor() == null){
						curr.setNeighbor(new PQNode(it.current()));
						curr = curr.getNeighbor();
						break;
					} else{
						curr = curr.getNeighbor();
					}
				}

				if(curr.getChild() == null){
					curr.setChild(new PQNode(it.current()));
				}

				it.next();
				curr = curr.getChild();
			}

			curr.setVal('$');
			if(carPQ != null){
				curr.setPQ(carPQ);
			}
		}

		return true;
	}


	public CarPQ getPQ(String word){
		if(word == null || root == null) return null;

		StringCharacterIterator it = new StringCharacterIterator(word);
		PQNode curr = root;

		while(it.getIndex() < it.getEndIndex()){
			if(curr == null){
				return null;
			}

			while(it.current() != curr.getVal()){
				if(curr.getNeighbor() == null){
					return null;
				} else{
					curr = curr.getNeighbor();
				}
			}
			curr = curr.getChild();
			it.next();
		}

		if(curr == null){
			return null;
		} else if(curr.getVal() == '$'){
			return curr.getPQ();
		}
		return curr.getPQ();
	}

}
