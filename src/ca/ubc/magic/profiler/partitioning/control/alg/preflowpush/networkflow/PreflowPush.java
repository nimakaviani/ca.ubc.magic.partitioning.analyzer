package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The PreflowPush class simulates the Preflow-Push algorithm, which
 * is used to obtain the maximum flow of a directed, flow network (graph).
 * 
 * @author Brent Lessley
 * @version 20 November 2010
 */
public class PreflowPush {
	
	private Map<String, Integer> vertices;
	private ArrayList<Vertex>[] excess_heights;
	private List<ArrayList<Edge>> residual_edges;
	private List<Integer> current_pointers;
	private SimpleGraph network;
	private int max_height;
	private Vertex sink_node;
	private Vertex source_node;
	private boolean gap_relabel;
	
	/**
	 * Default PreflowPush constructor.
	 * 
	 * @param G An input network graph.
	 * @param start The source node of the flow network.
	 * @param end The sink node of the flow network.
	 */
	@SuppressWarnings("unchecked")
	public PreflowPush(SimpleGraph G, Vertex start, Vertex end) {
		vertices = new HashMap<String, Integer>();
		excess_heights = new ArrayList[((G.numVertices() + 2))];
		residual_edges = new ArrayList<ArrayList<Edge>>(G.numVertices());
		current_pointers = new ArrayList<Integer>(G.numVertices());
		network = G;
		max_height = 0;
		sink_node = end;
		source_node = start;
	}
	
	/**
	 * Conducts the Preflow-Push algorithm and computes the maximum flow
	 * of a network graph.
	 * 
	 * @return The maximum flow of the input network graph.
	 */
	public int computeMaxFlow() {
        Vertex excess_vertex;
        //Vertices still exist with excess flow.
        while(excessVertices()) {
        	//Process the first vertex with excess flow at the current maximum height.
        	excess_vertex = findNextVertex();
        	boolean canPush = push(excess_vertex);
        	if(!canPush) {
        		relabel(excess_vertex);
        	}
        }
		return ((VertexData) sink_node.getData()).getExcessFlow(); //The maximum flow.
	}
	
	/**
	 * Initializes the various data structures and variables that are used
	 * in the operation of this algorithm.
	 */
	public void initialize() {
		Vertex v;
		Edge source_edge;
		String name;
		VertexData data;
    	EdgeData info;
		Iterator itr;
		Iterator s_v;
		String source_name = (String) source_node.getName();
		excess_heights[0] = new ArrayList<Vertex>();
		int index = 0;
        for (itr = network.vertices(); itr.hasNext();){
        	v = (Vertex) itr.next();
        	name = (String) v.getName();
        	//Give each vertex a list to hold its residual edges.
        	residual_edges.add(new ArrayList<Edge>());
        	//Assign an integer id to each vertex.
        	vertices.put(name, index);
        	//Set the vertex to initially point to its first residual edge.
        	current_pointers.add(0);
        	//Assign a height of n to the source vertex.
        	if(name.equals(source_name)) {
        		data = (VertexData) v.getData();
        		data.setPreflowHeight(network.numVertices());
        		v.setData(data);
        		//For each edge leaving the source, set its flow value to the edge capacity.
        		for(s_v = network.incidentEdges(v); s_v.hasNext();) {
        			source_edge = (Edge) s_v.next();
                	info = (EdgeData) source_edge.getData();
                	try {
						info.setFlow(info.getCapacity());
					} catch (Exception e) {
						e.printStackTrace();
					}
                	source_edge.setData(info);
        		} 	
        	}
        	index++;
        }
        ArrayList<Edge> residual_edge_list;
        ArrayList<Vertex> vertex_by_height;
        int count = 0;
        for (itr = network.vertices(); itr.hasNext();) {
        	int edge_flow;
        	int flow_in = 0;
        	int flow_out = 0;
        	v = (Vertex) itr.next();
        	name = (String) v.getName();
        	residual_edge_list = residual_edges.get(vertices.get(name));
        	//Obtains the values needed to calculate the excess flow at a vertex.
        	//Also records each outgoing vertex edge in the residual edge list.
        	for(s_v = network.incidentEdges(v); s_v.hasNext();) {
        		source_edge = (Edge) s_v.next();
        		edge_flow = ((EdgeData) source_edge.getData()).getFlow();
        		if(name.equals(source_edge.getSecondEndpoint().getName().toString())) {
            		flow_in += edge_flow;
            		//Residual graph backward edge (v, w) exists.
            		if(edge_flow > 0) {
            			residual_edge_list.add(source_edge);
            		}
            	} else {
            		flow_out += edge_flow;
            		//Residual graph forward edge (v, w) exists.
            		if(edge_flow < ((EdgeData) source_edge.getData()).getCapacity()) {
            			residual_edge_list.add(source_edge);
            		}
            	}
        	}
        	residual_edges.set(vertices.get(name), residual_edge_list);
        	data = (VertexData) v.getData();
        	data.setExcessFlow(flow_in - flow_out);
        	v.setData(data);
        	//Add vertices with excess flow into the list that contains
        	//vertices by height.  Only non-sink or non-source vertices with 
        	//excess flow are in this list.
        	if ((flow_in - flow_out) > 0 && !name.equals(sink_node.getName().toString()) && 
        		!name.equals(source_node.getName().toString())) {
        		int height = data.getPreflowHeight();
        		vertex_by_height = excess_heights[height];
        		vertex_by_height.add(v);
        		excess_heights[height] = vertex_by_height;
        	} 
        }
	}
	
	/**
	 * Verifies that vertices with excess flow still exist.
	 */
	private boolean excessVertices() {
		boolean moreVertices = false;
		for(int i = 0; i < excess_heights.length; i++) {
			if(excess_heights[i] != null) {
				moreVertices = true;
				break;
			}
		}
		return moreVertices; 
	}
	
	/**
	 * Pushes some of the excess flow at vertex v forwards or backwards to another
	 * vertex.  Flow is only pushed to a vertex that is incident to an outgoing
	 * residual graph edge of v.  
	 */
	private boolean push(Vertex v) {
		boolean canPush = false;
		int v_id = vertices.get(v.getName().toString());
		VertexData v_data = (VertexData) v.getData();
		List<Edge> v_edges = residual_edges.get(v_id);
		List<Edge> w_edges;
		Vertex w = null;
		VertexData w_data;
		int w_id;
		for(int i = current_pointers.get(v_id); i < v_edges.size(); i++) {
			boolean forward_edge = false;
			//Get an unprocessed residual edge incident to vertex v.
			Edge e = v_edges.get(i);
			EdgeData e_data = (EdgeData) e.getData();
			Vertex temp1 = e.getFirstEndpoint();
			Vertex temp2 = e.getSecondEndpoint();
			if(vertices.get(temp1.getName().toString()) == v_id) {
				w = temp2; //Part of forward edge.
				forward_edge = true;
			} else {
				w = temp1; //Part of backward edge.
			}
			w_data = (VertexData) w.getData();
			//Requirement of Steepness condition.
			if(v_data.getPreflowHeight() > w_data.getPreflowHeight()) {
				canPush = true;
				w_id = vertices.get(w.getName().toString());
				w_edges = residual_edges.get(w_id);
				int min;
				if(forward_edge) {
					//The amount of flow that will be pushed "forward" in the network.
					min = Math.min(v_data.getExcessFlow(), e_data.getAvailable());
					if(e_data.getFlow() == 0) {
						w_edges.add(e);
					}
				} else {
					//The amount of (negated) flow that will be pushed "backward" in the network.
					min = ((Math.min(v_data.getExcessFlow(), e_data.getFlow()))) * (-1);
					if(e_data.getAvailable() == 0) {
						w_edges.add(e);
					}
				}
				try {
					e_data.setFlow(e_data.getFlow() + min);
					if((forward_edge && (e_data.getAvailable() == 0)) ||
					   (!forward_edge && (e_data.getFlow() == 0))) {
						v_edges.remove(i);
						residual_edges.set(v_id, (ArrayList<Edge>) v_edges);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				residual_edges.set(w_id, (ArrayList<Edge>) w_edges);
				//Decrement excess flow at vertex v and increment excess at vertex w. 
				v_data.setExcessFlow(v_data.getExcessFlow() - Math.abs(min));
				w_data.setExcessFlow(w_data.getExcessFlow() + Math.abs(min));
				//Remove vertex v from the list of vertices with excess flow if the
				//push operation depleted its excess flow.
				if(v_data.getExcessFlow() <= 0) {
					ArrayList<Vertex> nodes = excess_heights[max_height];
					nodes.remove(0);
					excess_heights[max_height] =  nodes;
					if(excess_heights[max_height].size() == 0 && max_height != 0) {
						max_height = max_height - 1;
						excess_heights[max_height + 1] = null;
					}
				}
				int w_flow = w_data.getExcessFlow();
				String w_name = w.getName().toString();
				//Vertex w has not acquired excess flow and should be inserted into the height list if
				//it is not the sink vertex.
				if(w_flow > 0 && ((w_flow - Math.abs(min)) <= 0) && (!w_name.equals(sink_node.getName().toString())) &&
				   (!w_name.equals(source_node.getName().toString()))) {
					ArrayList<Vertex> height = excess_heights[w_data.getPreflowHeight()];
					if(height == null) {
						height = new ArrayList<Vertex>();
						height.add(w);
						excess_heights[w_data.getPreflowHeight()] = height;
					} else {
						excess_heights[w_data.getPreflowHeight()].add(w);
					}
				}
				//Current edge was saturated, so advance pointer to next edge incident to v.
				v.setData(v_data);
				e.setData(e_data);
				break;
			}
			current_pointers.set(v_id, current_pointers.get(v_id) + 1);
		}
		return canPush;
	}
	
	/**
	 * Increases the height of vertex v.  This height change should only occur when
	 * all of v's incident residual edges have been saturated or height(w) >= height(v) for all
	 * vertices w that are incident to a residual graph edge of v.   
	 */
	private void relabel(Vertex v) {
		int id = vertices.get(v.getName().toString());
		VertexData data = (VertexData) v.getData();
		int original_height = data.getPreflowHeight();
		int min_neighbor_height = network.numVertices();
		int neighbor_height;
		String name = v.getName().toString();
		//Finds the neighboring vertex with the smallest height among the set of neighbors. 
		for(Edge neighbor : residual_edges.get(id)) {
			if(neighbor.getFirstEndpoint().getName().toString().equals(name)) {
				neighbor_height = ((VertexData) neighbor.getSecondEndpoint().getData()).getPreflowHeight();
			} else {
				neighbor_height = ((VertexData) neighbor.getFirstEndpoint().getData()).getPreflowHeight();
			}
			if((neighbor_height >= original_height) && neighbor_height < min_neighbor_height) {
				min_neighbor_height = neighbor_height;
			}
		}
		//Set the height of v to one level greater than its closest neighbor (by height).
		data.setPreflowHeight(min_neighbor_height + 1);
		max_height = min_neighbor_height + 1;
		v.setData(data);
		current_pointers.set(id, 0);
		ArrayList<Vertex> new_excess_vertices = new ArrayList<Vertex>();
		new_excess_vertices.add(v);
		if(excess_heights[max_height] == null) {
			excess_heights[max_height] = new_excess_vertices;
		}
		//Removes v from its old height level list.
		ArrayList<Vertex> old_height = excess_heights[original_height];
		for(int i = 0; i < old_height.size(); i++) {
			if(old_height.get(i).getName().toString().equals(v.getName().toString())) {
				old_height.remove(i);
				if(old_height.size() == 0) {
					excess_heights[original_height] = null;
				} else {
					excess_heights[original_height] = old_height;
				}
			}
		}
	}
	
	/**
	 * Finds the next excess-flow vertex to process.  
	 */
	private Vertex findNextVertex() {
		if(excess_heights[max_height] == null) {
			for(int i = max_height - 1; i >= 0; i--) {
				if(excess_heights[i] != null) {
					max_height = i;
					break;
				}
			}
		}
		return (Vertex) excess_heights[max_height].get(0); 
	}
	
	public static void main(String[] args) {
		SimpleGraph G = new SimpleGraph();
		Vertex s, a, b, c, d, t;
		Edge e, k, l, m, n;
		int flow;
		Iterator i;
		EdgeData edata;
		try {
		
		/*//Correct Max Flow: 6
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		G.insertEdge(s, a, new EdgeData(4, 0), "E");
		b = G.insertVertex(new VertexData(false), "b");
		G.insertEdge(s, b, new EdgeData(2, 0), "K");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(a, t, new EdgeData(5, 0), "I");
		G.insertEdge(b, t, new EdgeData(1, 0), "M");
		G.insertEdge(b, a, new EdgeData(3, 0), "L");*/
		
	    /*//Correct Max Flow: 120
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		G.insertEdge(s, a, new EdgeData(90, 0), "s-a");
		b = G.insertVertex(new VertexData(false), "b");
		G.insertEdge(a, b, new EdgeData(60, 0), "a-b");
		c = G.insertVertex(new VertexData(false), "c");
		G.insertEdge(s, c, new EdgeData(110, 0), "K");
		d = G.insertVertex(new VertexData(false), "d");
		G.insertEdge(c, d, new EdgeData(30, 0), "c-d");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(b, t, new EdgeData(50, 0), "b-t");
		G.insertEdge(c, a, new EdgeData(10, 0), "c-a");
		G.insertEdge(d, t, new EdgeData(100, 0), "d-t");
		G.insertEdge(a, d, new EdgeData(30, 0), "a-d");
		G.insertEdge(b, d, new EdgeData(40, 0), "b-d");*/
			
			/*//Correct Max Flow: 24
			s = G.insertVertex(new VertexData(false), "s");
			a = G.insertVertex(new VertexData(false), "a");
			G.insertEdge(s, a, new EdgeData(32, 0), "E");
			b = G.insertVertex(new VertexData(false), "b");
			G.insertEdge(a, b, new EdgeData(16, 0), "K");
			c = G.insertVertex(new VertexData(false), "c");
			G.insertEdge(a, c, new EdgeData(8, 0), "L");
			// G.insertEdge(b, c, new EdgeData(16,0), "N");
			t = G.insertVertex(new VertexData(false), "t");
			// G.insertEdge(a, t, new EdgeData(32, 0), "M");
			G.insertEdge(b, t, new EdgeData(16, 0), "M");
			G.insertEdge(c, t, new EdgeData(8, 0), "M");*/
			
			s = G.insertVertex(new VertexData(false), "s");
			a = G.insertVertex(new VertexData(false), "11");
			G.insertEdge(s, a, new EdgeData(1, 0), "E");
			b = G.insertVertex(new VertexData(false), "12");
			G.insertEdge(s, b, new EdgeData(1, 0), "K");
			c = G.insertVertex(new VertexData(false), "r1");
			d = G.insertVertex(new VertexData(false), "r2");
			G.insertEdge(a, c, new EdgeData(1, 0), "N");
			G.insertEdge(a, d, new EdgeData(1, 0), "L");
			G.insertEdge(b, c, new EdgeData(1, 0), "Z");
			G.insertEdge(b, d, new EdgeData(1, 0), "D");
			// G.insertEdge(b, c, new EdgeData(16,0), "N");
			t = G.insertVertex(new VertexData(false), "t");
			// G.insertEdge(a, t, new EdgeData(32, 0), "M");
			G.insertEdge(c, t, new EdgeData(1, 0), "M");
			G.insertEdge(d, t, new EdgeData(1, 0), "S");
		
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
			PreflowPush model = new PreflowPush(G, s, t);
			model.initialize();
			flow = model.computeMaxFlow();
		
		} catch (Exception e1) {
			System.out.println("Preflow-Push screwed up");
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
