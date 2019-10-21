import java.io.*;
import java.util.*;




public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		Stack.add(source);
		ArrayList<Integer> visited = new ArrayList<Integer>();
	    visited.add(source);
	    while(!Stack.isEmpty()&&!destination.equals(Stack.get(Stack.size()-1))){
	    	int current = Stack.get(Stack.size()-1) ;
	    	ArrayList<Edge> listOfEdges = graph.getEdges() ;
	    	int index = 0 ;
	    	boolean isGoodEdge = false ;
	    	while(!isGoodEdge && index < listOfEdges.size()) {
	    		Edge currentEdge = listOfEdges.get(index)  ;
	    		if(currentEdge.nodes[0]== current && !visited.contains(currentEdge.nodes[1]) && currentEdge.weight>0){
	    			isGoodEdge = true;
	    			Stack.add(currentEdge.nodes[1]);
	    			visited.add(currentEdge.nodes[1]);
	    		}
	    		index++ ;
	    	}
	    	if(!isGoodEdge){
	    		Stack.remove(Stack.size()-1);
	    	}
	    	
	    }
		return Stack;
	}
	
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "260683363"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;
		
		WGraph residual = new WGraph(graph) ;
		WGraph flowGraph = new WGraph(graph);
		for(Edge e : graph.getEdges()){
			flowGraph.setEdge(e.nodes[0] , e.nodes[1] , 0);
		}
		boolean keepGoing = true ;
		while(keepGoing){
			ArrayList<Integer> newPath = pathDFS(source , destination ,residual);
			if (newPath.isEmpty()){
				keepGoing = false ;
			}
			else{
				Integer minimumEdge = null ;
				for( int i=0 ; i<newPath.size()-1; i++ ){
					Edge currentEdge = residual.getEdge(newPath.get(i),newPath.get(i+1));
					int weight = currentEdge.weight;
					if (minimumEdge==null || weight < minimumEdge){
						minimumEdge = weight ;
					}		
				}
				for( int i=0 ; i<newPath.size()-1; i++ ){
					Edge currentEdge = residual.getEdge(newPath.get(i),newPath.get(i+1));
					currentEdge.weight = currentEdge.weight - minimumEdge ;
					Edge edgeRev = residual.getEdge(newPath.get(i+1), newPath.get(i));
					if(edgeRev!=null){
						edgeRev.weight += minimumEdge ;
					}
					else{
						Edge e = new Edge(newPath.get(i+1), newPath.get(i),minimumEdge);
						residual.addEdge(e);
					}
					Edge fe = flowGraph.getEdge(newPath.get(i), newPath.get(i+1));
					if( fe!=null ){
						fe.weight += minimumEdge ;
					}
					else{
						Edge rfe = flowGraph.getEdge(newPath.get(i+1), newPath.get(i)) ;
						rfe.weight -= minimumEdge ;
					}
				}
				maxFlow = maxFlow+minimumEdge ;
			}
		}
		graph = flowGraph ;

		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
	}
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
}
