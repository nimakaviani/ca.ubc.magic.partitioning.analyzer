package ca.ubc.magic.profiler.partitioning.control.alg.metis;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public abstract class Graph <V, E> {
	protected SortedMap<Integer, Set<E>> graph = new TreeMap<Integer, Set<E>>();
	
	protected SortedMap<Integer, Integer> partitions = new TreeMap<Integer, Integer>();
	protected Map<Integer, Integer> idMapping = new HashMap<Integer, Integer>(); //Primary key -> Metis file line number
	protected Map<Integer, Integer> idMappingReverse = new HashMap<Integer, Integer>(); //Metis file line number -> Primary key
	
	protected Map<Integer, V> nodes = new HashMap<Integer, V>();

	public abstract int getNumNodes();
	public abstract int getNumEdges();
	protected abstract AbstractClusterSet<Integer> getClusters();
	
	public V getNode(Integer nodeID) {
		return nodes.get(nodeID);
	}
	
	public void setNode(Integer nodeID, V data) {
		nodes.put(nodeID, data);
	}
	
	public Set<Integer> getNodeIdSet(){
		return nodes.keySet();
	}
	
	public Set<E> getNodeEdgeSet(Integer nodeId){
		return graph.get(nodeId);
	}
	
	public int indexID(int index) {
		return idMappingReverse.get(index);
	}
	
	public abstract AbstractClusterSet<Integer> cluster() throws Exception;
	
	public void readPartitions(BufferedReader r) throws IOException {
		String next = r.readLine();
		int i = 1;
		while(next != null) {
			Integer partition = Integer.parseInt(next);
			Integer id = idMappingReverse.get(i);
			partitions.put(id, partition);
			next = r.readLine();
			i++;
		}
	}
	
	protected void mapIDs() {
		int i = 0;
		for(Integer id : graph.keySet()) {
			i++;
			idMapping.put(id, i);
			idMappingReverse.put(i, id);
		}
	}
	
	protected Integer getID(Integer i) {
		Integer value = idMapping.get(i);
		return value;
	}
	
	public abstract void toXGMML(PrintWriter pw);
	
//	public void toMetis(PrintWriter pw) {
//		mapIDs();
//		pw.println(getNumNodes() + " " + getNumEdges());
//		for(Set<E> entry : graph.values()) {
//			for(E i : entry) {
//				pw.print(getID(i.getEnd()) + " ");
//			}
//			pw.println();
//		}
//		pw.flush();
//	}
//	
//	public void printGraph(){
//		for (Integer nodeId : nodes.keySet()) {
//			System.out.println(nodeId + " " + nodes.get(nodeId).getName());
//			for (E edge : graph.get(nodeId))
//				System.out.println("\t " + edge.getStart() + " -> " + edge.getEnd() + " : " + edge.getWeight());
//		}
//	}
}
