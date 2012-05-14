package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;
/**
 * Provides edges with a set capacity and current flow.
 * 
 * @author Aaron Munger
 * @author Miles Raymond
 * @version 2010-11-30
 */
public class EdgeData {
	
	private int capacity;
	private int flow;
	
	/**
	 * Default EdgeData with maximum capacity of 1 and current flow of 0.
	 */
	public EdgeData(){
		this.capacity = 1;
		this.flow = 0;
	}
	
	/**
	 * Initialize EdgeData with custom values.
	 * 
	 * @param capacity The maximum flow an edge can carry.
	 * @param flow The current flow an edge is carrying.
	 * @throws Exception If provided values are out of bounds.
	 */
	public EdgeData(int capacity, int flow) throws Exception{
		if(capacity<flow || flow<0 || capacity<0)
			throw new IndexOutOfBoundsException();
		this.capacity = capacity;
		this.flow = flow;
	}
	
	/**
	 * Returns the maximum capacity minus the current flow.
	 * 
	 * @return int Available capacity
	 */
	public int getAvailable() {
		return this.capacity - flow;
	}
	
	/**
	 * Returns the maximum capacity of this edge.
	 * 
	 * @return int Maximum capacity
	 */
	public int getCapacity(){
		return this.capacity;
	}
	
	/**
	 * Returns the current flow of this edge.
	 * 
	 * @return int Current flow
	 */
	public int getFlow(){
		return this.flow;
	}
	
	/**
	 * Sets the current flow of this edge.
	 * 
	 * @param flow New flow value this edge will carry.
	 * @throws Exception If provided flow is out of bounds.
	 */
	public void setFlow(int flow) throws Exception{
		if(this.capacity<flow || flow<0)
			throw new IndexOutOfBoundsException();
		this.flow = flow;
	}
	
}
