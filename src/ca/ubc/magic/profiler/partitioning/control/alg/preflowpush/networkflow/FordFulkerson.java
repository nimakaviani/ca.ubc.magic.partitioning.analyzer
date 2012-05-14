package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;

import java.util.Iterator;

/**
 * Finds the max flow with FordFulkerson.
 * 
 * @author Eun Ju Lee
 * @version 2010-11-30
 */
public class FordFulkerson {
	public static int maxFlow(SimpleGraph G, Vertex s, Vertex t) throws Exception{
		SimpleGraph path;
		int flow, choke, maxcap;
		Iterator i;
		Edge e;
		Vertex v, previous;
		VertexData vdata;
		EdgeData edata;
		
		flow = 0;
		maxcap = 0;
		
		//find the maximum capacity of all edges
		for (i = G.edges();i.hasNext(); ){
			e = (Edge) i.next();
			edata = (EdgeData) e.getData();
			maxcap = Math.max(maxcap, edata.getCapacity());
		}
			
			//set all vertexData visited to false for pathfinder
			for (i = G.vertices(); i.hasNext(); ){
				v = (Vertex) i.next();
				vdata = (VertexData) v.getData();
				vdata.setVisited(false);			
			}
		
			path = PathFinder.depthFirstSearch(G, s, t, new SimpleGraph(), 0);		
			
			while (path.numEdges() > 0){
			
				choke = maxcap;
				previous = s;
				//find the value of the chokepoint in the path
				for (i = path.edges(); i.hasNext(); ){
					e = (Edge) i.next();
					edata = (EdgeData) e.getData();
					if (e.getFirstEndpoint() == previous){
						choke = Math.min(choke, edata.getAvailable());
						previous = e.getSecondEndpoint();
					} else {
						choke = Math.min(choke, edata.getFlow());
						previous = e.getFirstEndpoint();
					}
					
				}
				
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
				path = PathFinder.depthFirstSearch(G, s, t, new SimpleGraph(),0);
			}
		
		return flow;
	}
}
