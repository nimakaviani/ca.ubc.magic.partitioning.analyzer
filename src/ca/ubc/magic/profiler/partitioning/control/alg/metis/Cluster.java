package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Encapsulates a group of elements that have been clustered. Essentially this is just a Set with a unique ID, 
 * and additionally, every member of the set has a unique ID.
 * 
 * @author Eric Wohlstadter
 *
 * @param <T>
 * The type of the elements that are clustered (i.e. members of the cluster)
 */
public abstract class Cluster<T> {
	private SortedMap<Integer, T> instances = new TreeMap<Integer, T>();
	private int id;
	
	public Cluster(int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	public Map<Integer, T> getInstances() {
		return instances;
	}
	
	public void add(int id, T item) {
		instances.put(id, item);
	}
	
	public T getData(int id) {
		return instances.get(id);
	}
	
	public List<Integer> getMembers() {
		List<Integer> list = new LinkedList<Integer>();
		list.addAll(instances.keySet());
		return list;
	}
}
