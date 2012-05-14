/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.metis;

import ca.ubc.magic.profiler.dist.control.Constants;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author nima
 */
public class MetisPartitioner extends AbstractPartitioner {
    
    protected UndirectedGraph ugraph = new UndirectedGraph();
    protected Map<String, Integer> nameIdMap = new HashMap<String, Integer>();
    protected SortedMap<Integer, String> sortedIdNameMap = new TreeMap<Integer, String>();
    
    protected String mSolution;
    
    @Override
    public void init(ModuleModel mModel, HostModel hModel) {
        super.init(mModel, hModel);
    }

    @Override
    public void doPartition() {
        try {
            
            MetisUtil.fixedFile(Constants.METIS_HOME, Constants.METIS_FIXED,
                    getFixedFileContent());
            
            MetisUtil.metis(Constants.METIS_EXECUTABLE, 
                    Constants.METIS_HOME, 
                    ugraph, Constants.METIS_PARTITIONS_NUM,
                    Constants.METIS_PARTITIONS_UBFACTOR);
            
            for (Integer i : ugraph.getClusters().getClusterIDs()){
                Cluster c = ugraph.getClusters().getCluster(i);
                for (Integer id : (Collection<Integer>) c.getInstances().values()){
                    String s = sortedIdNameMap.get(id);
                    Module m = mModuleModel.getModuleMap().get(s);
                    if (m!= null){
                        m.setPartitionId(i + 1);
                    }
                }
            }
            
            mSolution = MetisUtil.getSolution();
            
        }catch (Exception e){
            throw new RuntimeException ("Metis screwed up: " + e.getMessage());
        }
    }     
    
    public String getSolution() {
        return (mSolution != null ? mSolution : "");
    }
    
    protected void initNodes(){
        int seed = 1;
        for (String m : mModuleModel.getModuleMap().keySet()){
            ugraph.setNode(seed, mModuleModel.getModuleMap().get(m).getName());
            nameIdMap.put(m, seed);
            sortedIdNameMap.put(seed, m);
            seed++;
        }
        
        for (Host h : mHostModel.getHostMap().values()){
            ugraph.setNode(seed, h.toString());
            nameIdMap.put(h.toString(), seed);
            sortedIdNameMap.put(seed, h.toString());
            seed++;
        }
    }
    
    protected void initEdges() {
        for (ModuleHost mh : mExecutionCostMap.keySet()){
            int hostNodeId = nameIdMap.get(mh.getHost().toString());
            int moduleNodeId = nameIdMap.get(mh.getModule().getName());
            if (hostNodeId == getTargetHostId())
                ugraph.addEdge(getSourceHostId(), moduleNodeId, Math.max(1.0, mExecutionCostMap.get(mh)));           
            else
                ugraph.addEdge(getTargetHostId(), moduleNodeId, Math.max(1.0, mExecutionCostMap.get(mh)));                           
        }
       
        Set<ModulePair> mpSet = new HashSet<ModulePair>();
        for (ModulePairHostPair mhp : mInteractionCostMap.keySet()){
            if (mpSet.contains(mhp.getModulePair()))
                continue;
            mpSet.add(mhp.getModulePair());
            int moduleNodId1 = nameIdMap.get(mhp.getModulePair().getModules()[0].getName());
            int moduleNodId2 = nameIdMap.get(mhp.getModulePair().getModules()[1].getName());
            ugraph.addEdge(moduleNodId1, moduleNodId2, Math.max(1.0, 
                    mInteractionCostMap.get(mhp).getTotalCost()));
        }
    }
    
    private String getFixedFileContent(){
        StringBuilder str = new StringBuilder();
        for (Integer i : sortedIdNameMap.keySet()){
            boolean found = Boolean.FALSE;
            for (Host h : mHostModel.getHostMap().values())
                if (sortedIdNameMap.get(i).equals(h.toString())){
                    str.append(h.getId() - 1);
                    found = Boolean.TRUE;
                    break;
                }
            if (!found)
                str.append(Constants.METIS_DONT_CARE_PARTITION);
            str.append(System.getProperty("line.separator"));
        }
        return str.toString().trim();
    }
    
    private Integer getTargetHostId(){
       return nameIdMap.get(TwoHostHelper.getTargetHost(mHostModel).toString());
    }
    
    private Integer getSourceHostId(){
        return  nameIdMap.get(TwoHostHelper.getSourceHost(mHostModel).toString());
    }
    
    protected void filterHostExecution(IModuleFilter filter, ModuleHost mh){
        mh.setCost(filter.filter(mh));
        mExecutionCostMap.put(mh, mh.getCost());
    }
    protected void filterHostInteraction(IInteractionFilter filter, ModulePairHostPair mhp) {
        throw new UnsupportedOperationException();
    }
}
