//Celine Cui
//3.26.2019
import java.util.*;
public class NetworkAnalysis{
    public static void main(String[] args){
        if(args.length == 1){
            Graph graph = new Graph(args[0]);
//         	Graph graph = new Graph("network_data3.txt");
            Scanner sc = new Scanner(System.in);
            String input;
            do{
                System.out.println("\n\t1.Find the lowest latency path between any two points.");
                System.out.println("\t2.Determine whether or not the graph is copper-only connected.");
                System.out.println("\t3.Find the minimum average latency spanning tree. ");
                System.out.println("\t4.Determine whether the graph would remain connected. ");
                System.out.println("\t5.Quit.");
                System.out.print("\tChoose an option: ");
                input = sc.nextLine();
                if(input.equals("1")){
                    String distance = String.format("%.2f", graph.find_lowest_latency_path());
                    if(distance.equals("-1.00")) System.out.println("\nThere is no path between these two points. ");
                }else if(input.equals("2")){
                    if(graph.check_copper_only()) System.out.println("\nThis graph is copper-only connected");
                    else System.out.println("\nThis graph is not copper-only connected");
                }else if(input.equals("3")){
                    graph.find_minimum_average();
                }else if(input.equals("4")){
                    graph.check_remain_connected();
                }else if(input.equals("5")){
                    System.exit(0);
                }else{
                    System.out.println("\nInvalid option! ");
                }
            }while(!input.equals("5"));
        }else{
            System.out.print("Invalid arguments. \n");
        }
    }
}