package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The PreflowPushEnhanced class simulates the generic Preflow-Push algorithm, while
 * incorporating several height processing and re-labeling heuristics.  While managing vertex
 * heights, FIFO (First-In-First-Out) and Highest Label procedures are leveraged.  Throughout
 * the push() and relabeling operations local and gap re-labeling schemes are employed.  
 * Local re-labeling is applied in the relabel() method and gap re-labeling is conducted whenever a 
 * vertex loses its excess flow.
 * 
 * @author Brent Lessley
 * @version 03 December 2010
 */
public class PreflowPushEnhanced {
	
	private Map<String, Integer> vertices;
	private ArrayList<Vertex>[] excess_heights;
	private ArrayList<Vertex>[] inactive_excess_nodes;
	private List<ArrayList<Edge>> residual_edges;
	private List<Integer> current_pointers;
        private Set<String> source_vertices = null;        
	private SimpleGraph network;
	private int max_height;
	private Vertex sink_node;
	private Vertex source_node;
	
	/**
	 * Default PreflowPushEnhanced constructor.
	 * 
	 * @param G An input network graph.
	 */
	@SuppressWarnings("unchecked")
	public PreflowPushEnhanced(SimpleGraph G, Vertex start, Vertex end) {
		vertices = new HashMap<String, Integer>();
		excess_heights = new ArrayList[2 * (G.numVertices())];
		inactive_excess_nodes = new ArrayList[2* (G.numVertices())];
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
            
            // identify source nodes and sink nodes for the cut
            cutEdges();
            
            return ((VertexData) sink_node.getData()).getExcessFlow(); //The maximum flow.
	}
        
        private void cutEdges(){
            source_vertices = new HashSet<String>();            
            recurseResidualEdge(source_node);
        }
        
        public void recurseResidualEdge(Vertex v){
            
            if (source_vertices.contains((String) v.getName()))
                return;
            
            source_vertices.add((String)v.getName());
            
            int v_index = vertices.get((String)v.getName());
            List<Edge> rEdgeList = residual_edges.get(v_index);
            for (Edge e : rEdgeList){
                
//                System.out.println( e.getName() + " :: " + ((EdgeData) e.getData()).getAvailable() + " / " +
//                        ((EdgeData) e.getData()).getCapacity());
                               
                if (v.equals(e.getFirstEndpoint()))
                    recurseResidualEdge(e.getSecondEndpoint());
                else
                    recurseResidualEdge(e.getFirstEndpoint());
            }
        }
        
        public boolean isSourceVertex(String vertexName){
            return (source_vertices.contains(vertexName));
        }
        
	/**
	 * Initializes the various data structures and variables that are used
	 * in the operation of this algorithm.
	 * 
	 * @param start The source node of the flow network.
	 * @param end The sink node of the flow network.
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
		for(int i = 0; i < inactive_excess_nodes.length; i++) {
			inactive_excess_nodes[i] = new ArrayList<Vertex>();
		}
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
        	} else if(!name.equals(sink_node.getName().toString()) && 
        		      !name.equals(source_node.getName().toString()))  {
        		inactive_excess_nodes[0].add(v);
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
					inactive_excess_nodes[v_data.getPreflowHeight()].add(v);
					excess_heights[max_height] =  nodes;
					if(excess_heights[max_height].size() == 0 && max_height != 0) {
						max_height = max_height - 1;
						excess_heights[max_height + 1] = null;
						gapRelabel(v_data.getPreflowHeight());
					}
				}
				int w_flow = w_data.getExcessFlow();
				String w_name = w.getName().toString();
				//Vertex w has not acquired excess flow and should be inserted into the height list if
				//it is not the sink vertex.
				if(w_flow > 0 && (w_flow - Math.abs(min)) <= 0 && (!w_name.equals(sink_node.getName().toString())) &&
				  (!w_name.equals(source_node.getName().toString()))) {
					ArrayList<Vertex> height = excess_heights[w_data.getPreflowHeight()];
					if(height == null) {
						height = new ArrayList<Vertex>();
						height.add(w);
						excess_heights[w_data.getPreflowHeight()] = height;
					} else {
						excess_heights[w_data.getPreflowHeight()].add(w);
					}
					//Remove w from the inactive vertex list.
					ArrayList<Vertex> inactive_nodes = inactive_excess_nodes[w_data.getPreflowHeight()];
					for(int index = 0; index < inactive_nodes.size(); index++) {
						if(inactive_nodes.get(index).getName().toString().equals(w.getName().toString())) {
							inactive_nodes.remove(index);
						}
					}
					inactive_excess_nodes[w_data.getPreflowHeight()] = inactive_nodes;
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
//		int min_neighbor_height = network.numVertices();
                int min_neighbor_height = Integer.MAX_VALUE;
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
//		System.out.println(max_height);
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
	
	/**
	 * Once no more active vertices exist in a height level h, all vertices m,
	 * such that h < height(m) < |V|, have their height raised to level n.
	 */
	private void gapRelabel(int old_height) {
		int n = network.numVertices();
		Vertex v_inactive;
		Vertex v_active;
		VertexData data;
		VertexData info;
		List<Vertex> active = excess_heights[n];
		if(active == null) {
			active = new ArrayList<Vertex>();
		}
		List<Vertex> inactive = inactive_excess_nodes[n];
		if(inactive == null) {
			inactive = new ArrayList<Vertex>();
		}
		for(int i = old_height + 1; i < n; i++) {
			if(excess_heights[i] != null) {
				for(int j = 0; j < excess_heights[i].size(); j++) {
					v_active = excess_heights[i].get(j);
					data = ((VertexData) v_active.getData());
					data.setPreflowHeight(n);
					v_active.setData(data);
					active.add(v_active);
				}
			}
			if(inactive_excess_nodes[i] != null) {
				for(int k = 0; k < inactive_excess_nodes[i].size(); k++) {
					v_inactive = inactive_excess_nodes[i].get(k);
					info = ((VertexData) v_inactive.getData());
					info.setPreflowHeight(n);
					v_inactive.setData(info);
					inactive.add(inactive_excess_nodes[i].get(k));
				}
			}
		}
		if(active.isEmpty()) {
			active = null;
		}
		excess_heights[n] = (ArrayList<Vertex>) active;
		inactive_excess_nodes[n] = (ArrayList<Vertex>) inactive;
	}
	
	public static void main(String[] args) {
		SimpleGraph G = new SimpleGraph();
		Vertex s, a, b, c, d, t, g, f;
		Edge e;
		Set<Edge> c_edges;
		int flow;
		Iterator i;
		EdgeData edata;
		try {
		
		//Correct Max Flow: 6
		/*s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		G.insertEdge(s, a, new EdgeData(4, 0), "E");
		b = G.insertVertex(new VertexData(false), "b");
		G.insertEdge(s, b, new EdgeData(2, 0), "K");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(a, t, new EdgeData(5, 0), "I");
		G.insertEdge(b, t, new EdgeData(1, 0), "M");
		G.insertEdge(b, a, new EdgeData(3, 0), "L");*/
		
	    //Correct Max Flow: 15
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		b = G.insertVertex(new VertexData(false), "b");
		c = G.insertVertex(new VertexData(false), "c");
		d = G.insertVertex(new VertexData(false), "d");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(s, a, new EdgeData(20, 0), "s->a");
		G.insertEdge(s, b, new EdgeData(1, 0), "s->b");
		G.insertEdge(s, c, new EdgeData(20, 0), "s->c");
		G.insertEdge(s, d, new EdgeData(20, 0), "s->d");
		G.insertEdge(a, t, new EdgeData(1, 0), "a->t");
		G.insertEdge(b, t, new EdgeData(20, 0), "b->t");
		G.insertEdge(c, t, new EdgeData(1, 0), "c->t");
		G.insertEdge(d, t, new EdgeData(1, 0), "d->t");
		
		G.insertEdge(a, d, new EdgeData(1, 0), "a->d");
		G.insertEdge(a, c, new EdgeData(1, 0), "a->c");
		G.insertEdge(a, b, new EdgeData(1, 0), "a->b");
		G.insertEdge(b, c, new EdgeData(10, 0), "b->c");
		
		G.insertEdge(d, a, new EdgeData(1, 0), "d->a");
		G.insertEdge(c, a, new EdgeData(1, 0), "c->a");
		G.insertEdge(b, a, new EdgeData(1, 0), "b->a");
		G.insertEdge(c, b, new EdgeData(10, 0), "c->b");
			
		/*//Correct Max Flow: 42
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		b = G.insertVertex(new VertexData(false), "b");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(s, a, new EdgeData(20, 0), "s->a");
		G.insertEdge(s, b, new EdgeData(40, 0), "s->b");
		G.insertEdge(a, t, new EdgeData(Integer.MAX_VALUE, 0), "a->t");
		G.insertEdge(b, t, new EdgeData(12, 0), "b->t");
		G.insertEdge(a, b, new EdgeData(10, 0), "a->b");
		G.insertEdge(b, a, new EdgeData(10, 0), "b->a");*/
			
		/*//Correct Max Flow: 24
		s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		G.insertEdge(s, a, new EdgeData(8, 0), "sa");
		b = G.insertVertex(new VertexData(false), "b");
		G.insertEdge(a, b, new EdgeData(8, 0), "ab");
		c = G.insertVertex(new VertexData(false), "c");
		G.insertEdge(b, c, new EdgeData(8, 0), "bc");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(c, t, new EdgeData(8, 0), "ct");
		d = G.insertVertex(new VertexData(false), "d");
		g = G.insertVertex(new VertexData(false), "g");
		f = G.insertVertex(new VertexData(false), "f");
		G.insertEdge(s, d, new EdgeData(12, 0), "sd");
		G.insertEdge(d, g, new EdgeData(12, 0), "dg");
		G.insertEdge(g, f, new EdgeData(12, 0), "gf");		
		G.insertEdge(f, t, new EdgeData(12, 0), "ft");*/
			
		/*s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		b = G.insertVertex(new VertexData(false), "b");
		d = G.insertVertex(new VertexData(false), "d");
		g = G.insertVertex(new VertexData(false), "g");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(s, a, new EdgeData(10, 0), "sa");
		G.insertEdge(s, d, new EdgeData(20, 0), "sd");
		G.insertEdge(a, b, new EdgeData(7, 0), "ab");
		G.insertEdge(d, b, new EdgeData(3, 0), "db");
		G.insertEdge(d, g, new EdgeData(20, 0), "dg");
		G.insertEdge(b, g, new EdgeData(3, 0), "bg");
		G.insertEdge(b, t, new EdgeData(7, 0), "bt");
		G.insertEdge(g, t, new EdgeData(20, 0), "gt");*/
			
		/*s = G.insertVertex(new VertexData(false), "s");
		a = G.insertVertex(new VertexData(false), "a");
		b = G.insertVertex(new VertexData(false), "b");
		c = G.insertVertex(new VertexData(false), "c");
		t = G.insertVertex(new VertexData(false), "t");
		G.insertEdge(s, a, new EdgeData(10, 0), "sa");
		G.insertEdge(s, b, new EdgeData(100, 0), "sb");
		G.insertEdge(s, c, new EdgeData(7, 0), "sc");
		G.insertEdge(b, a, new EdgeData(20, 0), "ba");
		G.insertEdge(a, b, new EdgeData(20, 0), "ab");
		G.insertEdge(a, c, new EdgeData(3, 0), "ac");
		G.insertEdge(c, a, new EdgeData(3, 0), "ca");
		G.insertEdge(b, c, new EdgeData(11, 0), "bc");
		G.insertEdge(c, b, new EdgeData(11, 0), "cb");
		G.insertEdge(a, t, new EdgeData(12, 0), "at");
		G.insertEdge(b, t, new EdgeData(14, 0), "bt");
		G.insertEdge(c, t, new EdgeData(9, 0), "ct");*/
		
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
		G.insertEdge(c, t, new EdgeData(8, 0), "O");*/
		
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
			PreflowPushEnhanced model = new PreflowPushEnhanced(G, s, t);
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
		System.out.println("\n max flow: " + flow);
	}
}
