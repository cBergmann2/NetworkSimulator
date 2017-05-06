
public class Main {

	
	public static void main(String args[]){
		OLSR_Graph graph = new OLSR_Graph(5);
		
		graph.HelloNachrichtenSenden();
		
		graph.mprSetsBestimmen();
		
		System.out.println(graph.toString());
	}
}
