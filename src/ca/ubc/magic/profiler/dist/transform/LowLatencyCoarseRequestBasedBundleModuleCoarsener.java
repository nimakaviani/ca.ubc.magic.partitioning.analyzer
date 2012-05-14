/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;
import ca.ubc.magic.profiler.dist.transform.model.NodeObj;

/**
 *
 * @author nima
 */
public class LowLatencyCoarseRequestBasedBundleModuleCoarsener extends CoarseRequestBasedBundleModuleCoarsener {
    
    /**
     * mRequestCount is a field responsible for getting the visit counts for a given head node and then
     * considering it as the number of times a request has been occurred during the execution of a node.
     * This allows for us to measure how often a particular request has been executed in the system.
     */
    private long mRequestCount = 0L;
            
    public LowLatencyCoarseRequestBasedBundleModuleCoarsener(EntityConstraintModel constraintModel){
        super(constraintModel);
    }

    /**
        accounting for the number of times the root node, i.e., the entry point, for
        the subgraph has been visited.
     * 
     * */
    @Override
    protected void preprocessing(Object... objs){
        super.preprocessing(objs);
        mRequestCount = ((NodeObj) objs[0]).getNodeVisit();
    }
    
    @Override
    protected void addEdge(String m1Name, String m2Name, NodeObj childNode) {
        Module m1 = mModuleModel.getModuleMap().get(m1Name);
        Module m2 = mModuleModel.getModuleMap().get(m2Name);
        addDataExchange(new ModulePair(m1, m2), 
            childNode.getEdge4ParentWeight().longValue() * childNode.getEdge4ParentCount().longValue() / mRequestCount, 
            childNode.getEdge2ParentWeight().longValue() * childNode.getEdge2ParentCount().longValue() / mRequestCount, 
            mRequestCount,
            mRequestCount);
    }
}
