package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A set of clusters that results from a clustering analysis. 
 * This base class is specialized for both graph clusters and n-dimensional (Euclidean) metric spaces
 * 
 * @author Eric Wohlstadter
 * 
 * @param <T> the type of the items being clustered
 */
public abstract class AbstractClusterSet<T> {
	protected Map<Integer, Cluster<T>> clusters = new HashMap<Integer, Cluster<T>>();
	protected Map<Integer, Cluster<T>> instanceIndex = new HashMap<Integer, Cluster<T>>();
	
	public Cluster<T> getCluster(Integer id) {
		return clusters.get(id);
	}
	
	public Map<Integer, Cluster<T>> getInstances() {
		return instanceIndex;
	}
	
	public Set<Integer> getClusterIDs() {
		return clusters.keySet();
	}
	
	public T getData(int id) {
		return instanceIndex.get(id).getData(id);
	}
	
	public Cluster<T> getClusterForItem(int itemId) {
		return instanceIndex.get(itemId);
	}
	
	public abstract Cluster<T> createCluster(int id);
	
	public void addToCluster(int clusterId, int itemId, T inst) {
		Cluster<T> cluster = clusters.get(clusterId);
		if(cluster == null) {
			cluster = createCluster(clusterId);
			clusters.put(clusterId, cluster);
		}
		cluster.add(itemId, inst);
		instanceIndex.put(itemId, cluster);
	}
	
}
