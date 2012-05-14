package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import ca.ubc.magic.profiler.dist.control.Constants;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UndirectedGraph extends Graph<String, EdgeData> {
	
	private int numOfEdges = 0;
	
	public AbstractClusterSet<Integer> getClusters() {
		GraphClusterSet cs = new GraphClusterSet();
		for(Map.Entry<Integer, Integer> entry : partitions.entrySet()) {
			cs.addToCluster(entry.getValue(), entry.getKey(), entry.getKey());
		}
		return cs;
	}
	
	public  int getNumNodes() {
		return graph.size();
	}
	
	public  int getNumEdges() {
		return numOfEdges;
	}
	
	
	
	public void toXGMML(PrintWriter pw) {
		pw.println("<?xml version='1.0'?><graph id='graph' label='graph' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:ns1='http://www.w3.org/1999/xlink' xmlns:dc='http://purl.org/dc/elements/1.1/' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#' xmlns='http://www.cs.rpi.edu/XGMML'>");
		for(Map.Entry<Integer, Set<EdgeData>> entry : graph.entrySet()) {
			
			Integer partition = partitions.get(entry.getKey());
			pw.println("<node id='n" + entry.getKey() + "' label='" + entry.getKey() + "'>");
			pw.println("<att type='string' name='node.id' value='n" + entry.getKey() + "'/>");
			
			String name = nodes.get(entry.getKey());
			if(name != null) {
				pw.println("<att type='string' name='node.label' value='" + name  + "'/>");
			}
			
			pw.println("<att type='string' name='node.partition' value='" + partition + "'/>");
			pw.println("</node>");
			for(EdgeData connected : entry.getValue()) {
				String edgeName = "e" + connected.getStart() + "_" + connected.getEnd();
				pw.println("<edge id='" + edgeName + "' label='" + edgeName + "' target='n" + connected.getEnd() + "' source='n" + connected.getStart() + "'>");
				pw.println("<att type='string' name='edge.weight' value='" + connected.getWeight() + "'/>");
				pw.println("<att type='string' name='edge.cut' value='" + 
						((partitions.get(connected.getStart()) != partitions.get(connected.getEnd())) ? "true" : "false") + "'/>");
				pw.println("</edge>");
			}
		}
		
		pw.println("</graph>");
	}
	
	public void addEdge(Integer fst, Integer snd, Double weight) {
		Set<EdgeData> fstSet = graph.get(fst);
		if(fstSet == null) {
			fstSet = new HashSet<EdgeData>();
			graph.put(fst, fstSet);
		}
		
		Set<EdgeData> sndSet = graph.get(snd);
		if(sndSet == null) {
			sndSet = new HashSet<EdgeData>();
			graph.put(snd, sndSet);
		}
		
		if(!fstSet.contains(snd)) {
			if(sndSet.contains(fst)) {
				throw new IllegalStateException("Database integrity violation: found a non-mutual social link");
			}
			fstSet.add(new EdgeData(fst, snd, weight));
			sndSet.add(new EdgeData(snd, fst, weight));
			numOfEdges++;
		}
	}
	
	public EdgeData getEdge(Integer fst, Integer snd){
		Set<EdgeData> edgeSet = graph.get(fst);
		if (edgeSet == null)
			return null;
		for (EdgeData edge : edgeSet)
			if (edge.getEnd().equals(snd))
				return edge;
		return null;
	}
	
//	public void toMetis(PrintWriter pw) {
//		mapIDs();
//		pw.println(getNumNodes() + " " + getNumEdges() + " " + Constants.METIS_PARTITIONING_CODE);
//		for(Integer key : graph.keySet()) {
//			if (Constants.METIS_PARTITIONING_CODE.equals("011")){
//				Module node = getNode(key);
//				pw.print(node.getExecutionCost().longValue() / 
//                                        node.getExecutionCount() + " ");
//			}
//			for(EdgeData i : graph.get(key)) {
//				pw.print(getID(i.getEnd()) + " " + i.getWeight().intValue() + " ");
//			}
//			pw.println();
//		}
//		pw.flush();
//	}
        
        public void tohMetis(PrintWriter pw){
            mapIDs();
            List<EdgeData> edgeList = new ArrayList<EdgeData>();
            for (Integer i : graph.keySet()){
                for (EdgeData e : graph.get(i)){
                    EdgeData reverseE = new EdgeData(e.getEnd(), e.getStart(), e.getWeight());
                    if (!edgeList.contains(e) && !edgeList.contains(reverseE))
                        edgeList.add(e);
                }
            }
            pw.println(getNumEdges() + " " + getNumNodes() + " " + Constants.HMETIS_PARTITIONING_CODE);
            for (EdgeData e : edgeList)
                pw.println(e.getWeight().intValue() + " " + e.getStart() + " " + e.getEnd());
            pw.flush();
        }

	@Override
	public AbstractClusterSet<Integer> cluster() throws Exception {
//		int nodes = this.getNumNodes();
//		int preferredNumClusters = Integer.parseInt(Config.getProperty("preferredClusterSize"));
//		int preferredNumClusters = (int) Math.floor(nodes/preferredFriendClusterSize);
////		MetisUtil.metis(Config.getProperty("metisCommand"), Config.getProperty("workingDir"), this, preferredNumClusters);
//		
//		return this.getClusters();
            return null;
	}
	
	public int[][] floydWarshall() {
		int n = this.getNumNodes();
		int[][] path = new int[n][n];
		for(int i=0; i < n; i++) {
			for(int j=0; j < n; j++) {
				int id = this.idMappingReverse.get(i + 1);
				Set<EdgeData> neighbors = this.graph.get(id);
				int otherID = this.idMappingReverse.get(j + 1);
				for (EdgeData edge : neighbors)
					if(edge.getEnd().equals(otherID)) {
						path[i][j] = 1;
						break;
					} 
				if (path[i][j] != 1) {
						path[i][j] = 99999999;
				}
			}
		}
		for(int k=0; k < n; k++) {
			for(int i =0; i < n; i++) {
				for(int j = 0; j < n; j++) {
					if(i != j) {
						path[i][j] = Math.min(path[i][j], path[i][k] + path[k][j]);
					}
				}
			}
		}
		return path;
	}
}
