package ca.ubc.magic.profiler.partitioning.control.alg.metis;


public class GraphClusterSet extends AbstractClusterSet<Integer> {

	@Override
	public Cluster createCluster(int id) {
		return new GraphCluster(id);
	}

}
