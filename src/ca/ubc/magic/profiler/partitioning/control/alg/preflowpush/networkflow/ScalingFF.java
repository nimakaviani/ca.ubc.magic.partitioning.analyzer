package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;



import java.util.Iterator;



/**
 * Finds the max flow
 * 
 * @author Aaron Munger
 * @version 2010-11-30
 */

public class ScalingFF {

	public ScalingFF(){
		
	}
	
	
	/**
	 * Given a graph and two vertices, the method will find the max
	 * flow using the scaling ford fulkerson algorithm
	 * @param G  is the simple graph to find a max flow for
	 * @param s  is the starting vertex
	 * @param t  is the ending vertex
	 * @return   int, the max flow through G
	 */
	public static int maxFlow(SimpleGraph G, Vertex s, Vertex t) throws Exception{
		SimpleGraph path;
		int flow, choke, scale, maxcap;
		Iterator i;
		Edge e, lastedge;
		Vertex v, previous;
		VertexData vdata;
		EdgeData edata;
		boolean hasPath;
		
		flow = 0;
		maxcap = 0;
		lastedge = null;
		
		//find the maximum capacity of all edges
		for (i = G.edges();i.hasNext(); ){
			e = (Edge) i.next();
			edata = (EdgeData) e.getData();
			maxcap = max(maxcap, edata.getCapacity());
		}
		
		scale = highestPowerof2(maxcap);
		
		while(scale >= 1){
			
			//set all vertexData visited to false for pathfinder
			for (i = G.vertices(); i.hasNext(); ){
				v = (Vertex) i.next();
				vdata = (VertexData) v.getData();
				vdata.setVisited(false);			
			}
		
			path = PathFinder.depthFirstSearch(G, s, t, new SimpleGraph(), scale-1);		
			
			while (path.numEdges() > 0){
			
				choke = maxcap;
				previous = s;
				//find the value of the chokepoint in the path
				for (i = path.edges(); i.hasNext(); ){
					e = (Edge) i.next();
					edata = (EdgeData) e.getData();
					if (e.getFirstEndpoint() == previous){
						choke = min(choke, edata.getAvailable());
						previous = e.getSecondEndpoint();
					} else {
						choke = min(choke, edata.getFlow());
						previous = e.getFirstEndpoint();
					}
					
				}
				

				//debugging code
				/*System.out.println("path found:");
				for (i = path.edges(); i.hasNext(); ){
					e = (Edge) i.next();
					edata = (EdgeData) e.getData();
					System.out.println("Edge: " + e.getName() + " " 
							+ e.getFirstEndpoint().getName() + "-" 
							+ e.getSecondEndpoint().getName() + " "
							+ edata.getFlow() + "/" + edata.getCapacity());
				}
				System.out.println("choke: " + choke);
				System.out.println();*/
				
				//add or subtract the choke value to the flow of each edge in the path
				previous = s;
				for (i = path.edges(); i.hasNext(); ){
					e = (Edge) i.next();
					edata = (EdgeData) e.getData();
					if (e.getFirstEndpoint() == previous){
						edata.setFlow(edata.getFlow()+ choke);
						previous = e.getSecondEndpoint();
					} else {
						edata.setFlow(edata.getFlow() - choke);
						previous = e.getFirstEndpoint();
					}
					
				}
				flow += choke;
				
				//set all vertexData visited to false for pathfinder
				for (i = G.vertices(); i.hasNext(); ){
					v = (Vertex) i.next();
					vdata = (VertexData) v.getData();
					vdata.setVisited(false);			
				}
				path = PathFinder.depthFirstSearch(G, s, t, new SimpleGraph(), scale-1);
			}
			scale = scale / 2;
		}
		return flow;
	}
	

	private static int max(int x, int y) {
		if(x > y)
			return x;
		return y;
	}

	private static int min(int x, int y) {
		if(x < y)
			return x;
		return y;
	}
	
	/**
	 * Finds the greatest power of 2 that is less than the number given
	 * @param 	num	the cap on the return value
	 * @return	int
	 */
	private static int highestPowerof2(int num){
		int ans;
		ans = 1;
		
		while (ans * 2 <= num){
			ans = ans * 2;
		}
		return ans;
	}
	
	
	public static void main(String[] args){
		SimpleGraph G = new SimpleGraph();
		Vertex s, a, b, c, t;
		Edge e, k, l, m, n;
		int flow;
		Iterator i;
		EdgeData edata;
		VertexData vdata;
		
		try {
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		G.insertEdge(s, a, new EdgeData(2, 0), "E");
		b = G.insertVertex(new VertexData(false), "b");
		G.insertEdge(b, a, new EdgeData(1, 1), "K");
		//c = G.insertVertex(new VertexData(false), "c");
		//G.insertEdge(a, c, new EdgeData(8, 0), "L");
		//G.insertEdge(b, c, new EdgeData(16,0), "N");
		t = G.insertVertex(new VertexData(false), "t");
		//G.insertEdge(a, t, new EdgeData(32, 0), "M");
		G.insertEdge(b, t, new EdgeData(2, 0), "M");
		//G.insertEdge(c, t, new EdgeData(8, 0), "M");
		
		} catch (Exception e1) {
			System.out.println("graph creation screwed up");
			return;
		}
		
		System.out.println("Graph edges");
		for (i = G.edges(); i.hasNext(); ){
			e = (Edge) i.next();
			edata = (EdgeData) e.getData();
			System.out.println("Edge: " + e.getName() + " " 
					+ e.getFirstEndpoint().getName() + "-" 
					+ e.getSecondEndpoint().getName() + " "
					+ edata.getFlow() + "/" + edata.getCapacity());
		}
		System.out.println();
		
		try {
			
			flow = maxFlow(G, s, t);
		
		} catch (Exception e1) {
			System.out.println("scalingFF maxFlow screwed up");
			e1.printStackTrace();
			return;
		}
		
		for (i = G.edges(); i.hasNext(); ){
			e = (Edge) i.next();
			edata = (EdgeData) e.getData();
			System.out.println("Edge: " + e.getName() + " " 
					+ e.getFirstEndpoint().getName() + "-" 
					+ e.getSecondEndpoint().getName() + " "
					+ edata.getFlow() + "/" + edata.getCapacity());
		}
		
		System.out.println("max flow: " + flow);
		

	}
}
