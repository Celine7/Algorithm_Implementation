//Celine Cui
//3.26.2019
public class Edge{
	private final int copper = 230000000; //meters per second
	private final int optical = 200000000;//meters per second
	private Vertex from;
	private Vertex to;
	private String type; //either "optical" or "copper"	
	private int bandwidth; //in megabits per second
	private int length; //in meters
	private double time;
	public Edge(Vertex from, Vertex to, String type, int bandwidth, int length){
		this.from = from;
		this.to = to;
		this.type = type;
		this.bandwidth = bandwidth;
		this.length = length;
		if(type.equals("copper")){
			time = (double)length/copper *Math.pow(10,9);
		}else if(type.equals("optical")){
			time = (double)length/optical * Math.pow(10,9);
		}
	}
	public int getBandwidth() {return bandwidth;}
	public double getTime() { return time;}
	public String getType() {return type; }
	public Vertex getFrom() { return from; }
	public Vertex getTo() { return to; }
}
