//Celine Cui
//3.26.2019
import java.util.*;

public class Vertex{
    private LinkedList<Edge> edges;
    private int id;
    private ArrayList<Vertex> vertices;

    public Vertex(int id){
        this.id = id;
        edges = new LinkedList<>();
        vertices = new ArrayList<>();
    }

    public LinkedList<Edge> getEdges() { return edges; }
    public void setEdges(LinkedList<Edge> edges) { this.edges = edges;}
    public void addEdges(Edge edge){ edges.add(edge);}
    public int getId() { return id; }
}