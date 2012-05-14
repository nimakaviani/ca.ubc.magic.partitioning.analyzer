/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.preflowpush;

import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;
import ca.ubc.magic.profiler.dist.model.TwoHostHelper;
import ca.ubc.magic.profiler.dist.transform.IInteractionFilter;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import ca.ubc.magic.profiler.partitioning.control.alg.AbstractPartitioner;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.Edge;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.EdgeData;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.PreflowPushEnhanced;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.SimpleGraph;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.Vertex;
import ca.ubc.magic.profiler.partitioning.control.alg.preflowpush.networkflow.VertexData;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author nima
 */
public class PreflowPushPartitioner extends AbstractPartitioner {
    
    Module source, target;
    Map<String, Vertex> vMap;
    
    Vertex sourceVertex, targetVertex;
    
    SimpleGraph mGraph;
    
    protected long mFlow;
    
    // The PreflowPush algorithm only takes integer values and overflwos if the value
    // for the integer goes beyond Integer.MAX_VALUE. The following  reasonably large
    // number hopefully will prevent an overflow in the type of systems we are testing
    // the algorithm against. This needs to be closely monitored in case the issue 
    // appears.
    public static final int INFINITE_VALUE = Integer.MAX_VALUE / 4;

    @Override
    public void init (ModuleModel mModel, HostModel hModel){
        super.init(mModel, hModel);      
        if (mHostModel.getNumberOfHosts() != 2)
            throw new RuntimeException("Simplex partitioner can be applied to 2 hosts only");
        mGraph = new SimpleGraph();
     }
     
    @Override
    public void doPartition(){
        
        ///////// Test Printouts ////////////////////
//        for (ModuleHost mh : mExecutionCostMap.keySet())
//            System.out.println(mh.toString() + " " + mExecutionCostMap.get(mh));
//        
//        System.err.println("--------");
//        
//        for (ModulePairHostPair mhp : mInteractionCostMap.keySet())
//            System.out.println(mhp.toString() + " " + mInteractionCostMap.get(mhp));
//              
        //////////// Test Printouts Ending //////////////
        long flow;
        PreflowPushEnhanced ppe = null;
        Set<Edge> cEdges;
        try {
            
            ppe = new PreflowPushEnhanced(mGraph, 
                    sourceVertex, targetVertex);
            ppe.initialize();
            flow = ppe.computeMaxFlow();
            
//            System.out.println("Graph edges");
//            Iterator i;
//            int count = 0;
//            for (i = mGraph.edges(); i.hasNext(); ){
//                    Edge e = (Edge) i.next();
//                    EdgeData edata = (EdgeData) e.getData();
//                    System.out.println(++count + " Edge: " + e.getName() + " " 
//                                    + e.getFirstEndpoint().getName() + "-" 
//                                    + e.getSecondEndpoint().getName() + " "
//                                    + edata.getFlow() + "/" + edata.getCapacity());
//            }
//            System.out.println();
        

            for (Module m: mModuleModel.getModuleMap().values()){
                if (ppe.isSourceVertex(m.getName()))
                    m.setPartitionId(new Integer(1));
                else
                    m.setPartitionId(new Integer(2));
            }
            
            mFlow = flow;
            System.out.println("\n max flow: " + mFlow);
        } catch (Exception e1) {
                System.out.println("Preflow-Push screwed up");
                e1.printStackTrace();
                return;
        }                        
    }
    
    public String getSolution(){
        return "Solution: " + mFlow;
    }
    
    @Override
    protected void initEdges() {
        try{
            for (ModuleHost mh : mExecutionCostMap.keySet()){
                Host h = mh.getHost();
                
                // adjusts the weight to a value within the proper range of integer values
                int weight = (mExecutionCostMap.get(mh).intValue() >= PreflowPushPartitioner.INFINITE_VALUE) ?
                            PreflowPushPartitioner.INFINITE_VALUE : (int) Math.round(mExecutionCostMap.get(mh));
                if (mExecutionCostMap.get(mh).intValue() >= PreflowPushPartitioner.INFINITE_VALUE)
                    System.err.println("[PreflowPushPartitioner WARNING]: max value adjusted for " + 
                            mh.toString() + " :: " + (int) Math.round(mExecutionCostMap.get(mh)));
                
                if (weight >= 0){
                    if (h.getId() == TwoHostHelper.getTargetHost(mHostModel).getId()){
//                        if (TestPreflowPushPartitioner.EDGE_LIST.contains("s -> " + mh.getModule().getName()))
                            mGraph.insertEdge(sourceVertex, vMap.get(mh.getModule().getName()),                        
                                new EdgeData(weight, 0), "s -> " + mh.getModule().getName());                     
                    }
                    else if (h.getId() == TwoHostHelper.getSourceHost(mHostModel).getId()){
//                        if (TestPreflowPushPartitioner.EDGE_LIST.contains(mh.getModule().getName() + " -> t"))
                            mGraph.insertEdge(vMap.get(mh.getModule().getName()), targetVertex, 
                                new EdgeData(weight, 0), mh.getModule().getName() + " -> t");  
                    }
                }
            }

            for (ModulePairHostPair mhp : mInteractionCostMap.keySet()){
                ModulePair mp = mhp.getModulePair();
                Vertex v1, v2;
                if (mhp.getHostPair().getHost1().getId() == 1 &&
                        mhp.getHostPair().getHost2().getId() == 2){
                    v1 = vMap.get(mp.getModules()[0].getName());
                    v2 = vMap.get(mp.getModules()[1].getName());
                } else if (mhp.getHostPair().getHost1().getId() == 2 &&
                        mhp.getHostPair().getHost2().getId() == 1){
                    v2 = vMap.get(mp.getModules()[0].getName());
                    v1 = vMap.get(mp.getModules()[1].getName());
                }else
                    throw new RuntimeException("Illegal placement of modules "
                            + "on hosts");
                
                // adjusts the weight to a value within the proper range of integer values
                // and issues a written warning to highlight the fact that this is happening
                int weight = (mInteractionCostMap.get(mhp).getTotalCost().intValue() >= PreflowPushPartitioner.INFINITE_VALUE) ?
                        PreflowPushPartitioner.INFINITE_VALUE : mInteractionCostMap.get(mhp).getTotalCost().intValue();
                if (mInteractionCostMap.get(mhp).getTotalCost().doubleValue() >= PreflowPushPartitioner.INFINITE_VALUE)
                    System.err.println("[PreflowPushPartitioner WARNING]: max value adjusted for " + 
                            mhp.toString()  + " :: " + mInteractionCostMap.get(mhp));
                
//                if (TestPreflowPushPartitioner.EDGE_LIST.contains(v1.getName() + " -> " + v2.getName()))
                    mGraph.insertEdge(v1, v2, 
                         new EdgeData(weight, 0), v1.getName() + " -> " + v2.getName());                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initNodes() {
        vMap = new HashMap<String, Vertex>();
        sourceVertex = mGraph.insertVertex(new VertexData(false), 
                Integer.toString(TwoHostHelper.getSourceHost(mHostModel).getId()));
        targetVertex = mGraph.insertVertex(new VertexData(false), 
                Integer.toString(TwoHostHelper.getTargetHost(mHostModel).getId()));
        for (ModuleHost mh : mExecutionCostMap.keySet()){
            Vertex v = mGraph.insertVertex(new VertexData(false), mh.getModule().getName());
            if (!vMap.keySet().contains(mh.getModule().getName()))               
                vMap.put(mh.getModule().getName(), v);            
        }
    }  
    
    protected void filterHostExecution(IModuleFilter filter, ModuleHost mh){
        mh.setCost(filter.filter(mh));
        mExecutionCostMap.put(mh, mh.getCost());
    }
    protected void filterHostInteraction(IInteractionFilter filter, ModulePairHostPair mhp) {
        throw new UnsupportedOperationException();
    }
}
