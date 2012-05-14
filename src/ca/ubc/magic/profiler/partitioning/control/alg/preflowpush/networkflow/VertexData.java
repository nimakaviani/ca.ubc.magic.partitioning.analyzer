package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow;

public class VertexData {
	private boolean visited;
	
	private int excess_flow;
	
	private int preflow_height;
	
	public VertexData(){
		this.visited = false;
		this.preflow_height = 0;
		this.excess_flow = 0;
	}
	
	public VertexData(boolean visited){
		this.visited = visited;
	}
	
	public void setVisited(boolean visited){
		this.visited = visited;
	}
	
	public void setExcessFlow(int excess) {
		this.excess_flow = excess;
	}
	
	public void setPreflowHeight(int height) {
		this.preflow_height = height;
	}
	
	public boolean isVisited(){
		return this.visited;
	}
	
	public int getExcessFlow() {
		return this.excess_flow;
	}
	
	public int getPreflowHeight() {
		return this.preflow_height;
	}
}
