package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;
import java.util.*;

public class PathFinder {
	
	static int maxFlow; 
	static boolean path = false;
	/**
	 * 
	 */
	public PathFinder() {
		super();
	}

	/**
	 * Given a graph and two vertices,
	 * if there is a path through the residual graph from the first vertex (s)
	 * to the second (t) with residual flow larger than the given scale, a simpleGraph
	 * of vertices and edges in a path is returned.
	 * !!Works recursively so all vertexData must set visited = no before calling!!
	 * @param G  is the simple graph to find a path in
	 * @param s  is the starting vertex
	 * @param t  is the ending vertex
	 * @param p  is an empty SimpleGraph on which the solution is built
	 * @param d  is the scale factor used by scaling FF, can be set to 0 to ignore
	 * @return   SimpleGraph representing the path from s to t
	 */
	public static SimpleGraph depthFirstSearch (SimpleGraph G, Vertex s, Vertex t, SimpleGraph p, int d) {
		Vertex v;
		Edge e;
		VertexData vData;
		Iterator i;
		SimpleGraph newPath;
		path = false;

		( (VertexData) s.getData()).setVisited(true);
		p.vertexList.add(s);

		for (i= G.incidentEdges(s); i.hasNext(); ) {
			e = (Edge) i.next();
			v = G.opposite(s, e);
			
			/*if (!((VertexData) v.getData()).isVisited()){
				System.out.println("Checking Edge " + e.getName() + " with scale " + d);
			}*/
			
			//checks if there is residual flow to an unvisited vertex greater than the scale
			if (!( (VertexData) v.getData()).isVisited() &&
					(v == e.getSecondEndpoint() && ((EdgeData) e.getData()).getAvailable() > d ||
					v == e.getFirstEndpoint() && ((EdgeData) e.getData()).getFlow() > d)) {
				
				//System.out.println("accepted");
				
				if (v == t){
					//made it to the end
					p.edgeList.add(e);
					p.vertexList.add(v);
					return p;
				}

				p.edgeList.add(e);
				newPath = depthFirstSearch(G, v, t, p, d);
				if (newPath.vertexList.getLast() == t) {
					maxFlow((EdgeData) ((Edge) newPath.edgeList.getFirst()).getData());
					isPath();
					return newPath;
				}
				p.edgeList.removeLast();
			}
			( (VertexData) s.getData()).setVisited(true);
		}

		p.vertexList.removeLast();
		return p;
	}

	private static Boolean isPath() {
		return path = true;
		
	}

	private static int maxFlow(EdgeData data) {
		maxFlow =+ data.getFlow();
		//System.out.print("MaxFlow = ");
		//System.out.println(maxFlow);
		return maxFlow;
		
	}

	public static void main(String[] args) throws Exception {
		SimpleGraph G = new SimpleGraph();
		Vertex v, a, b, c, d, f;
		Edge e, x, y, z, k, l, m;
		SimpleGraph path;
		Iterator i;
		EdgeData edata;
		VertexData vdata;

		a = G.insertVertex(new VertexData(false), "a");
		b = G.insertVertex(new VertexData(false), "b");
		x = G.insertEdge(a, b, new EdgeData(32,0), "X");
		c = G.insertVertex(new VertexData(false), "c");
		y = G.insertEdge(b, c, new EdgeData(16,0), "Y");
		d = G.insertVertex(new VertexData(false), "d");
		z = G.insertEdge(b, d, new EdgeData(16,0), "Z");
		f = G.insertVertex(new VertexData(false), "f");
		k = G.insertEdge(b, f, new EdgeData(16,0), "K");
		//l = G.insertEdge(c, f, new EdgeData(), "L");
		//m = G.insertEdge(f, d, new EdgeData(2,0), "M");
		
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

		path = depthFirstSearch(G, a, d, new SimpleGraph(), 31);
		
		for (i = G.vertices(); i.hasNext(); ){
			v = (Vertex) i.next();
			vdata = (VertexData) v.getData();
			vdata.setVisited(false);			
		}
		
		path = depthFirstSearch(G, a, d, new SimpleGraph(), 15);
		
		
		System.out.println("Path found:");
		for (i = path.edges(); i.hasNext(); ){
			e = (Edge) i.next();
			edata = (EdgeData) e.getData();
			System.out.println("Edge: " + e.getName() + " " 
					+ e.getFirstEndpoint().getName() + "-" 
					+ e.getSecondEndpoint().getName() + " "
					+ edata.getFlow() + "/" + edata.getCapacity());
		}
		System.out.println();
		
		
		
		
		
		/*
		if (i.hasNext())
			System.out.println("path found:");
		else
			System.out.println(maxFlow);

		for (i = path.vertices(); i.hasNext() ; ) {
			v = (Vertex) i.next();
			System.out.println(v.getName());
		}
		for (i = path.edges(); i.hasNext(); ) {
			e = (Edge) i.next();
			System.out.println(e.getName());
		}
		*/
	}
}
