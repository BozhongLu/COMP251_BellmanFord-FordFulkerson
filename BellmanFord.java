public class BellmanFord{

	
	/**
	 * Utility class. Don't use.
	 */
	public class BellmanFordException extends Exception{
		private static final long serialVersionUID = -4302041380938489291L;
		public BellmanFordException() {super();}
		public BellmanFordException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 * 
	 * Use this to specify a negative cycle has been found 
	 */
	public class NegativeWeightException extends BellmanFordException{
		private static final long serialVersionUID = -7144618211100573822L;
		public NegativeWeightException() {super();}
		public NegativeWeightException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 *
	 * Use this to specify that a path does not exist
	 */
	public class PathDoesNotExistException extends BellmanFordException{
		private static final long serialVersionUID = 547323414762935276L;
		public PathDoesNotExistException() { super();} 
		public PathDoesNotExistException(String message) {
			super(message);
		}
	}
	
    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws BellmanFordException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         *  
         *  When throwing an exception, choose an appropriate one from the ones given above
         */
        /* step 1 */
        this.source = source ;
        distances = new int[g.getNbNodes()] ;
        predecessors = new int[g.getNbNodes()];
        for (int i=0 ; i<g.getNbNodes() ; i++){
        	distances[i] = Integer.MAX_VALUE ;
        	predecessors[i] = -1 ;
        }
        distances[source] = 0 ;
        
        /* step 2 */
        for (int i = 0 ; i< g.getNbNodes()-1 ; i++) {
        	for (Edge e : g.getEdges()){
        		if ((distances[e.nodes[0]]!=Integer.MAX_VALUE)&&((distances[e.nodes[0]]+e.weight)<distances[e.nodes[1]])){
        			distances[e.nodes[1]] = distances[e.nodes[0]]+e.weight ;
        			predecessors[e.nodes[1]] = e.nodes[0] ;
        		}
        	}
        }

        /* step 3 */
        for (Edge e : g.getEdges()){
        	if ((distances[e.nodes[0]]!=Integer.MAX_VALUE)&&((distances[e.nodes[0]]+e.weight)<distances[e.nodes[1]])){
        		throw new NegativeWeightException("Contains negative cycles !") ;
        	}
        }
        
    }

    public int[] shortestPath(int destination) throws BellmanFordException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Exception is thrown
         * Choose appropriate Exception from the ones given 
         */

        /* YOUR CODE GOES HERE (update the return statement as well!) */
    	int current = destination ;
    	int[] accumulator = new int[distances.length] ;
    	int size = 1 ;
    	accumulator[0] = destination ;
        while( current != source ){
        	current = predecessors[current] ;
        	if (current == -1) throw new PathDoesNotExistException("There's no path from "+ source + " to this " + destination +" !");
        	accumulator[size] = current ;
        	size++ ;
        }
        int[] path = new int[size] ;
        for ( int i = 0 ; i < size ; i++ ){
        	path[i] = accumulator[size-(i+1)] ;
        } 
        return path ;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }

   } 
}
