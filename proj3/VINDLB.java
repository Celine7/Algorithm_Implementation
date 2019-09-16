//Celine Cui
//3.2.2019
import java.text.StringCharacterIterator;
class CarNode{
	private CarNode child;
	private CarNode neighbor;
	private char value;
	private Car car;

	public CarNode(char val){
		value = val;
	}

	public char getVal(){
		return value;
	}
	public void setVal(char val){
		value = val;
	}

	public CarNode getChild(){
		return child;
	}
	public void setChild(CarNode child){
		this.child = child;
	}

	public CarNode getNeighbor(){
		return neighbor;
	}
	public void setNeighbor(CarNode neighbor){
		this.neighbor = neighbor;
	}

	public Car getCar(){
		return car;
	}
	public void setCar(Car newCar){
		car = newCar;
	}
}
public class VINDLB{
	private CarNode root;
	private StringCharacterIterator it;
	public Car theCar = null;

	public boolean insert(Car car){
		String word = car.getVIN();
		if(word == null) return false;

		it = new StringCharacterIterator(word);
		CarNode curr;
		if(root == null){
			root = new CarNode(it.current());

			curr = root;
			it.next();

			while(it.getIndex() < it.getEndIndex()){
				curr.setChild(new CarNode(it.current()));
				curr = curr.getChild();
				it.next();
			}
			curr.setChild(new CarNode('$'));
			curr = curr.getChild();
		} else{
			curr = root;

			while(it.getIndex() < it.getEndIndex()){
				while(it.current() != curr.getVal()){
					if(curr.getNeighbor() == null){
						curr.setNeighbor(new CarNode(it.current()));
						curr = curr.getNeighbor();
						break;
					} else{
						curr = curr.getNeighbor();
					}
				}
				it.next();
				if(curr.getChild() == null){
					curr.setChild(new CarNode(it.current()));
				}
				curr = curr.getChild();
			}

			curr.setVal('$');
			}
		if(car != null){
			curr.setCar(car);
		}
		return true;
	}

	public boolean exists(String word){
		if(word == null || root == null) return false;
		it = new StringCharacterIterator(word);
		CarNode curr = root;

		while(it.getIndex() < it.getEndIndex()){
			if(curr == null){
				return false;
			}
			while(it.current() != curr.getVal()){
				if(curr.getNeighbor() == null){
					return false;
				} else{
					curr = curr.getNeighbor();
				}
			}
			curr = curr.getChild();
			it.next();
		}

		if(curr == null){
			return false;
		} else if(curr.getVal() == '$'){
			if(curr.getCar() != null){
				theCar = curr.getCar();
				return true;
			}
		}

		return false;
	}

	public boolean remove(String word){
		if(word == null || root == null) return false;

		it = new StringCharacterIterator(word);
		CarNode curr = root;

		while(it.getIndex() < it.getEndIndex()){
			if(curr == null){
				return false;
			}

			while(it.current() != curr.getVal()){
				if(curr.getNeighbor() == null){
					return false;
				} else{
					curr = curr.getNeighbor();
				}
			}

			curr = curr.getChild();
			it.next();
		}

		if(curr == null){
			return false;
		} else if(curr.getVal() == '$'){
			if(curr.getCar() != null){
				curr.setCar(null);
				return true;
			}
		}
		return false;
	}
}
