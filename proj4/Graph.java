//Celine Cui
//3.26.2019
import java.util.*;
import java.io.File;
public class Graph {
    private int vertices_number;
    private ArrayList<Edge> edges;
    private Vertex[] vertices;
    Scanner sc;

    public Graph(String f) {
        make_graph(f);
    }

    public void make_graph(String filename) {
        if (filename == null) return;
        sc = null;
        try {
            File f = new File(filename);
            sc = new Scanner(f);
            if (sc.hasNext()) vertices_number = Integer.parseInt(sc.nextLine());
            else return;
            vertices = new Vertex[vertices_number];
            for (int i = 0; i < vertices_number; i++) {
                vertices[i] = new Vertex(i);
            }
        } catch (Exception e) {
            System.out.println("This file does not exist");
            e.printStackTrace();
        }
        edges = new ArrayList<>();
        while (sc.hasNext()) {
            String info[] = sc.nextLine().split(" ");
            if (info.length != 5) continue;
            Vertex a = vertices[Integer.parseInt(info[0])];
            Vertex b = vertices[Integer.parseInt(info[1])];
            Edge original = new Edge(a, b, info[2], Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            Edge back = new Edge(b, a, info[2], Integer.parseInt(info[3]), Integer.parseInt(info[4]));
            edges.add(original);
            a.addEdges(original);
            b.addEdges(back);
        }
    }

    public double find_lowest_latency_path() {
        int id;
        Vertex from = null;
        Vertex to = null;
        do {
            System.out.print("Enter the beginning vertex id (0 to " + (vertices_number-1) + "): ");
            sc = new Scanner(System.in);
            try {
                id = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Invalid vertex. Enter the a number id: ");
                id = Integer.parseInt(sc.nextLine());
            }
            if (id < 0 || id >= vertices_number) System.out.print("Invalid vertex. ");
            else from = vertices[id];
        } while (from == null);
        do {
            System.out.print("Enter the end vertex id (0 to " + (vertices_number-1) + "): ");
            sc = new Scanner(System.in);
            try {
                id = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Invalid vertex. Enter the a number id: ");
                id = Integer.parseInt(sc.nextLine());
            }
            if (id < 0 || id >= vertices_number || id == from.getId()) System.out.print("Invalid vertex. ");
            else to = vertices[id];
        } while (to == null);
        if(!has_path(from.getId(), to.getId())) return -1;
        double[] distTo = new double[vertices_number];
        for (int i = 0; i < vertices_number; i++)
            distTo[i] = Double.POSITIVE_INFINITY;
        distTo[from.getId()] = 0.0;
        ArrayList<Vertex> visited = new ArrayList<>();
        ArrayList<Vertex> unvisited = new ArrayList<>();
        for(int i = 0; i < vertices_number; i++){
            unvisited.add(vertices[i]);
        }
        unvisited.remove(from);
        Vertex curr = from;
        int next = -1;
        Edge[] path = new Edge[vertices_number];
        double distance = Double.POSITIVE_INFINITY;
        while(unvisited.contains(vertices[to.getId()])){
            for(Edge edge: curr.getEdges()){
                if(unvisited.contains(vertices[edge.getTo().getId()])){
                    if(distTo[curr.getId()] + edge.getTime() < distTo[edge.getTo().getId()]){
                        distTo[edge.getTo().getId()] = distTo[curr.getId()] + edge.getTime();
                        path[edge.getTo().getId()] = edge;
                    }
                }
            }

            for(int i = 0; i < vertices_number; i++){
                if(unvisited.contains(vertices[i])&& i != curr.getId()){
                    if(distTo[i] < distance){
                        distance = distTo[i];
                        next = i;
                    }
                }
            }
            unvisited.remove(curr);
            distance = Double.POSITIVE_INFINITY;
            if(!visited.contains(curr)) visited.add(curr);
            curr = vertices[next];
        }
        ArrayList<Edge> realPath = new ArrayList<>();
        Edge cur = path[to.getId()];
        realPath.add(cur);

        while(cur.getFrom().getId() != from.getId()){
            cur = path[cur.getFrom().getId()];
            realPath.add(cur);
        }
        System.out.print("\nThe lowest latency path between " + from.getId() + " and " + to.getId() + " is: ");
        int[] ids = new int[realPath.size()+1];
        ids[0] = realPath.get(realPath.size()-1).getFrom().getId();
        for(int i = realPath.size()-1; i >= 0; i--){
            ids[realPath.size()- i] = realPath.get(i).getTo().getId();
        }

        System.out.print(ids[0]);
        for(int i = 1; i < ids.length; i++){
            System.out.print("->" + ids[i]);
        }
        double bandwidth = Double.POSITIVE_INFINITY;
        for(Edge edge: realPath){
            if(edge.getBandwidth() < bandwidth) bandwidth = edge.getBandwidth();
        }
        String latency = String.format("%.2f", distTo[to.getId()]);
        System.out.println("\nThe latency(in nanoseconds): " + latency);
        System.out.println("The bandwidth(megabits per second): " + (int)bandwidth);
        return 0;
    }


    public boolean isConnected() {
        for (int a = 0; a < vertices_number; a++) {
            for (int b = a+1; b < vertices_number; b++) {
                if (!has_path(a, b)) return false;
            }
        }
        return true;
    }

    public boolean has_path(int i, int j) {
        Vertex curr = vertices[i];
        Vertex end = vertices[j];
        if(curr == null || end == null) return false;
        Stack<Vertex> visited = new Stack<>();
        boolean[] visit = new boolean[vertices_number];
        visited.push(curr);
        while (!visited.empty()) {
            curr = visited.pop();
            visit[curr.getId()] = true;
            for (Edge edge : curr.getEdges()) {
                if(edge == null) continue;
                if(vertices[edge.getFrom().getId()] == null || vertices[edge.getTo().getId()] == null) continue;
                if (edge.getTo().getId() == j) {
                    return true;
                }
                if (!visit[edge.getTo().getId()]) {
                    visited.push(edge.getTo());
                }
            }
        }
        return false;
    }

    public boolean has_copper_path(int i, int j) {
        Vertex curr = vertices[i];
        Vertex end = vertices[j];
        if(curr == null || end == null) return false;
        Stack<Vertex> visited = new Stack<>();
        boolean[] visit = new boolean[vertices_number];
        visited.push(curr);
        while (!visited.empty()) {
            curr = visited.pop();
            visit[curr.getId()] = true;
            for (Edge edge : curr.getEdges()) {
                if (edge.getType().equals("copper")) {
                    if (edge.getTo().getId() == j) {
                        return true;
                    }
                    if (!visit[edge.getTo().getId()]) {
                        visited.push(edge.getTo());
                    }
                }
            }
        }
        return false;
    }

    public boolean check_copper_only() {
        for (int i = 0; i < vertices_number; i++) {
            for (int j = i+1; j < vertices_number; j++) {
                if (!has_copper_path(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void find_minimum_average() {
        if(!isConnected()){
            System.out.println("\nThis graph is not connected. ");
            return;
        }
        ArrayList<Edge> edges = new ArrayList<>(0);
        MinPQ edgePQ = new MinPQ();
        for(int i = 0; i < vertices_number; i++){
            for(Edge edge:vertices[i].getEdges()){
                edgePQ.insert(edge);
            }
        }
        ArrayList<Vertex> unvisited = new ArrayList<>();
        for(int i = 0; i < vertices_number; i++){
            unvisited.add(vertices[i]);
        }
        Edge curr;
        while(edges.size() != vertices_number-1){
            curr = edgePQ.delMin();
            if(edges.size() == 0){
                edges.add(curr);
                System.out.println("\n"+curr.getFrom().getId() + "->" + curr.getTo().getId());
                unvisited.remove(vertices[curr.getFrom().getId()]);
                unvisited.remove(vertices[curr.getTo().getId()]);
            }
            else if ((unvisited.contains(vertices[curr.getFrom().getId()]) && !unvisited.contains(vertices[curr.getTo().getId()]))
                    ^(unvisited.contains(vertices[curr.getTo().getId()]) && !unvisited.contains(vertices[curr.getFrom().getId()]))){
                edges.add(curr);
                if(unvisited.contains(vertices[curr.getFrom().getId()]) && !unvisited.contains(vertices[curr.getTo().getId()])){
                    System.out.println(curr.getTo().getId() + "->" + curr.getFrom().getId());
                }else System.out.println(curr.getFrom().getId() + "->" + curr.getTo().getId());
                unvisited.remove(vertices[curr.getFrom().getId()]);
                unvisited.remove(vertices[curr.getTo().getId()]);
            }
        }
    }

    public boolean isTwoPointConnected(int i, int j) {
        for (int a = 0; a < vertices_number; a++) {
            if(a == i || a == j) continue;
            for (int b = a+1; b < vertices_number; b++) {
                if(b == i || b == j) continue;
                if (!has_path(a, b)) return false;
            }
        }
        return true;
    }

    public void check_remain_connected(){
        if(!isConnected()){
            System.out.println("\nThis original graph is not connected. ");
            return;
        }
        Vertex curr;
        for (int i = 0; i < vertices_number; i++){
            curr = vertices[i];
            //two vertices fail, each vertex must have at least three edges.
            if (curr.getEdges().size() < 3){

                System.out.print("\nOne pair example: ( ");
                for(Edge edge: curr.getEdges()){
                    System.out.print(edge.getTo().getId() + " ");
                }
                System.out.print(")");
                System.out.println("\nThis graph would not remain connected if this pair of vertices fails.");
                return;
            }
        }
        for(int i = 0; i < vertices_number; i++){
            LinkedList<Edge> edgesfori = vertices[i].getEdges();
            for(int j = 0; j < vertices_number; j++){
                LinkedList<Edge> edgesforj = vertices[j].getEdges();
                if (i != j){
                    vertices[i] = null;
                    vertices[j] = null;

                }
                else continue;
                if(!isTwoPointConnected(i, j)){
                    System.out.println("\nOne pair example: ( " + i+ " " + j+" )");
                    System.out.println("This graph would not remain connected if this pair of vertices fails.");
                    vertices[i] = new Vertex(i);
                    vertices[i].setEdges(edgesfori);
                    vertices[j] = new Vertex(j);
                    vertices[j].setEdges(edgesforj);
                    return;
                }
                else {
                    vertices[i] = new Vertex(i);
                    vertices[i].setEdges(edgesfori);
                    vertices[j] = new Vertex(j);
                    vertices[j].setEdges(edgesforj);
                }
            }
        }
        System.out.println("\nThis graph would remain connected even if any two vertices fail.");
        return;
    }
}