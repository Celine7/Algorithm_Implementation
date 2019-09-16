//Celine Cui
//1.30.2019
import java.util.*;
import java.io.File;

class Node{
    private char value;
    private Node neighbor;
    private Node child;
    private int frequency = 0;
    public Node(){
    }
    public int getFrequency(){
    	return frequency;
    }
    public void setValue(char val){
    	value = val;
    }
    public char getValue(){
    	return value;
    }
    public void setNeighbor(Node neighbor){
    	this.neighbor = neighbor;
    }
    public Node getNeighbor(){
    	return neighbor;
    }
    public void setChild(Node child){
    	this.child = child;
    }
    public Node getChild(){
    	return child;
    }
    public void addFrequency(){
    	this.frequency++;
    }
    public boolean checkEmpty(){
    	if(value == 0) return true;
    	return false;
    }
}
public class DLB {
	public boolean no_user_history = false;
	public Node root;
	public Node current;
	private Node root_child;
	public String prefix = "";
	public boolean no_prediction = false;
	private ArrayList<String> predictions;
	private HashMap<String, Integer> map = new HashMap<>();
	private String real_prefix;

	public DLB() {
		root = new Node();
		root.setChild(new Node());
		root_child = root.getChild();
		current = root;
	}

	public Node user_search_for_word(char value) {
		current = current.getChild();
		real_prefix = prefix + value;
		no_user_history = false;
		while (current.getValue() != value) {
			if (current.getNeighbor().checkEmpty()) {
				no_user_history = true;
				break;
			} else {
				current = current.getNeighbor();
			}
		}
		return current;
	}

	public Node search_for_word(char value) {
		if(current.checkEmpty() && current.getChild() == null){
			no_prediction = true;
			return current;
		}
		current = current.getChild();
		real_prefix = prefix + value;
		while (current.getValue() != value) {
			current = current.getNeighbor();
			if (current.checkEmpty()) {
				no_prediction = true;
				break;
			}
		}
		return current;
	}

	public ArrayList<String> usr_prediction_array(Node node) {
		map = new HashMap<>();
		usr_recursive_prediction(node, prefix);

		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(map.keySet());
		Collections.sort(keys, new Comparator<String>() {
			@Override
			public int compare(String left, String right) {
				Integer val1 = map.get(left);
				Integer val2 = map.get(right);
				if (val1 == null) {
					return (val2 != null) ? 0 : 1;
				} else if ((val1 != null) && (val2 != null)) {
					return val2.compareTo(val1);
				}
                else {
					return 0;
				}
			}
		});
		return keys;
	}


	public void usr_recursive_prediction(Node node, String input){
		if(node.checkEmpty() ){
			return;
		}
		if(node.getValue() == '$'){
			if(!input.startsWith(real_prefix)){
				return;
			}
			map.put(input, node.getFrequency());
		}
		usr_recursive_prediction(node.getChild(), input + node.getValue());
		usr_recursive_prediction(node.getNeighbor(), input);
	}


	public ArrayList<String> prediction_array(Node node){
		predictions = new ArrayList<>(5); 
		for(int i = 0; i < 5; i++){
			recursive_prediction(node, prefix);
		}
		return predictions;
	}

	public void recursive_prediction(Node node, String input){
		if(node.checkEmpty() || predictions.size() == 5){
			return;
		}
		if(node.getValue() == '$'){
			if(predictions.contains(input)){
				return;
			}
			if(!input.startsWith(real_prefix)){
				return;
			}
			predictions.add(input);
		}
		recursive_prediction(node.getChild(), input + node.getValue());
		recursive_prediction(node.getNeighbor(), input);
	}



	public void addDictionary(String filename){
		File f = new File(filename);
		try{
			Scanner scanner = new Scanner(f);
			while (scanner.hasNext()) {
    	        this.addWord(scanner.nextLine());
			}
			scanner.close();
		}
		catch(Exception e){
			System.out.println("This file does not exist.");
			e.printStackTrace();
		}
	}
	public void addWord(String word){
		Node currentNode = root_child;
		if (currentNode.checkEmpty()){
			for(int i = 0; i < word.length(); i++){
				currentNode.setValue(word.charAt(i));
				currentNode.addFrequency();
				currentNode.setNeighbor(new Node());
				currentNode.setChild(new Node());
				currentNode = currentNode.getChild();
			}
		}
		else{
			for(int i = 0; i < word.length(); i++){
				currentNode = addNeighber(currentNode, word.charAt(i));
				currentNode = currentNode.getChild();
			}
		}

			if(!currentNode.checkEmpty()){
				currentNode = addNeighber(currentNode, '$');
				if(currentNode.getNeighbor().checkEmpty()){
					currentNode.setNeighbor(new Node());
					currentNode.setChild(new Node());
				}
			}else{
				currentNode.setValue('$');
				currentNode.addFrequency();
				currentNode.setNeighbor(new Node());
				currentNode.setChild(new Node());
			}

	}
	public Node searchforNeighbers(char value, Node node){
		Node currentNode = node;
		while(!currentNode.checkEmpty()){
			if (currentNode.getValue() == value){
				currentNode.addFrequency();
				return currentNode;
			}
			else{
			currentNode = currentNode.getNeighbor();
			}
		}
		return currentNode;
	}

	public Node addNeighber(Node node, char value){
		Node currentNode = searchforNeighbers(value, node);
		if (currentNode.checkEmpty()){
			currentNode.setValue(value);
			currentNode.addFrequency();
			currentNode.setNeighbor(new Node());
			currentNode.setChild(new Node());
		}
		return currentNode;
	}
}