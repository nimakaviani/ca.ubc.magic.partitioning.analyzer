/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg;

import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.transform.IFilter;
import ca.ubc.magic.profiler.dist.transform.IInteractionFilter;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import ca.ubc.magic.profiler.simulator.control.CostAnalyzerHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nima
 */
public abstract class AbstractPartitioner implements IPartitioner {
    
    protected ModuleModel mModuleModel = null;
    protected HostModel mHostModel = null;
    
    protected Map<ModuleHost, Double> mExecutionCostMap = null;
    protected Map<ModulePairHostPair, InteractionCost> mInteractionCostMap = null;
    
    protected List<IModuleFilter> mFilterList = null;
    protected List<IInteractionFilter> iFilterList = null;
    
    protected int mSize;
    
    public void init (ModuleModel mModel, final HostModel hModel){
        mModuleModel = mModel;
        mHostModel = hModel;
        
        mSize = mModuleModel.getModuleMap().keySet().size();
                       
        if (mModuleModel == null || mHostModel == null)
            throw new RuntimeException("The partitioner is not properly initialized.");                        
        
        if (!mModuleModel.isSimulation())                       
            initCostMap();        
    }               
    
    public void init(ModuleModel mModel, final HostModel hModel, final List predefinedModuleHostPlacement){
        this.init(mModel, hModel);
        if (mModuleModel.isSimulation())
            initSimulationCostMap(predefinedModuleHostPlacement);
        else
            throw new RuntimeException("Improper partitioner initialization.");
    }
    
    public void partition(){
        applyFilters();
        initNodes();
        initEdges();
//        printInteractions();
        long start = System.currentTimeMillis();
        doPartition();
        System.out.println(this.getClass().getCanonicalName() + "ExecTime: " + (System.currentTimeMillis() - start));
        postPartition();
//        printCutEdges();
    }

    protected void initCostMap(){
        
        // initializing data execution cost for the model
        mExecutionCostMap = new HashMap<ModuleHost, Double>();
        for (Module m : mModuleModel.getModuleMap().values())
            for (Host h : mHostModel.getHostMap().values()){
                ModuleHost mh = new ModuleHost(m, h);
                mExecutionCostMap.put(mh, mh.setExecutionCost(mHostModel, 
                        mHostModel.getDefaultHost()));
                
                System.out.println(mh.toString() + " ExecCost: " + mh.getCost());
            }
        
        ////////////////////////////////////////////////
        // initializing data exchange cost for the model
        mInteractionCostMap = new HashMap<ModulePairHostPair, InteractionCost>();
        for(ModulePair mp : mModuleModel.getModuleExchangeMap().keySet()){                                   
            InteractionData iData = mModuleModel.getModuleExchangeMap().get(mp);
            
            Iterator<Host> sitr = mHostModel.getHostMap().values().iterator();
            while(sitr.hasNext()){
                Host sourceHost = sitr.next();
                Iterator<Host> titr = mHostModel.getHostMap().values().iterator();
                while(titr.hasNext()){
                    Host targetHost = titr.next();                                        
                    if (sourceHost.equals(targetHost))
                        continue;
                   
                    HostPair s2tp = new HostPair(sourceHost, targetHost);                                        
                    ModulePairHostPair s2tmp = new ModulePairHostPair(mp, s2tp);                    
                    s2tmp.setCost(s2tp.getInteractionCost(mHostModel, iData));
                    mInteractionCostMap.put(s2tmp, s2tmp.getCost());      
                    
                    System.out.println(s2tmp.toString() + "InterCost: " + s2tmp.getCost());
                }
            }
        }
    }      
    
    protected void initSimulationCostMap(List predefinedModuleHostPlacement){
        
        // initializing data execution cost for the model
        mExecutionCostMap = new HashMap<ModuleHost, Double>();
        for (Module m : (List<Module>) predefinedModuleHostPlacement)
            for (Host h : mHostModel.getHostMap().values()){
                if (m.getPartitionId() == h.getId()){
                    ModuleHost mh = new ModuleHost(m, h);
                    mExecutionCostMap.put(mh, mh.setExecutionCost(mHostModel, mHostModel.getDefaultHost()));
                }                                
            }
        
        ////////////////////////////////////////////////
        // initializing data exchange cost for the model
        mInteractionCostMap = new HashMap<ModulePairHostPair, InteractionCost>();
        for(ModulePair mp : mModuleModel.getModuleExchangeMap().keySet()){                                   
            InteractionData iData = mModuleModel.getModuleExchangeMap().get(mp);
            
            Iterator<Host> sitr = mHostModel.getHostMap().values().iterator();
            while(sitr.hasNext()){
                Host sourceHost = sitr.next();
                Iterator<Host> titr = mHostModel.getHostMap().values().iterator();
                while(titr.hasNext()){
                    Host targetHost = titr.next();         
                    if (sourceHost.equals(targetHost))
                        continue;                  

                        HostPair s2tp = new HostPair(sourceHost, targetHost);                                        
                        ModulePairHostPair s2tmp = new ModulePairHostPair(mp, s2tp);                    
                        s2tmp.setCost(s2tp.getInteractionCost(mHostModel, iData));
                        mInteractionCostMap.put(s2tmp, s2tmp.getCost());                                                              
                }
            }
        }
    }
    
    /**
     * Applies filters to the cost of interaction or execution for modules in 
     * the model. The filter returns "true" if it can be applied to the ModuleHost
     * or the ModulePairHostPair elements, in that case, the corresponding values
     * in the execution map are updated.
     */
    public void applyFilters(){
        for (ModuleHost mh : mExecutionCostMap.keySet()){
            if (mFilterList == null)
                break;
            for (IModuleFilter f : mFilterList){
                if (f.isFilterable(mh))
                    filterHostExecution(f, mh);
            }
        }
        
        for (ModulePairHostPair mhp : mInteractionCostMap.keySet()){
            if (iFilterList == null)
                break;
            for (IInteractionFilter f : iFilterList){
                if (f.isFilterable(mhp))
                    filterHostInteraction(f, mhp);
            }
        }
    }
    
    public void addFilter(IFilter filter){
        if (filter instanceof IModuleFilter){
            if (mFilterList == null) 
                mFilterList = new ArrayList<IModuleFilter>();
            if (!mFilterList.contains((IModuleFilter) filter))
                mFilterList.add((IModuleFilter) filter);
        }else if (filter instanceof IInteractionFilter){
            if (iFilterList == null)
                iFilterList = new ArrayList<IInteractionFilter>();
            if (!iFilterList.contains((IInteractionFilter) filter))
                iFilterList.add((IInteractionFilter) filter);
        }
    }
    
    public void removeFilter(IFilter filter){
        if (filter instanceof IModuleFilter){
            if (mFilterList.contains((IModuleFilter) filter))
                mFilterList.remove((IModuleFilter) filter);
        }else if (filter instanceof IInteractionFilter){
            if (iFilterList.contains((IInteractionFilter) filter))
                iFilterList.remove((IInteractionFilter) filter);
        }
    }
    
    public abstract String getSolution();
    
    protected void postPartition(){
        mModuleModel.setParitioned(true);

        CostAnalyzerHelper.analyzeCosts(this.getClass().getCanonicalName(), mModuleModel, mHostModel);
    }
    
    void setInteractionCostMap(Map<ModulePairHostPair, InteractionCost> interactionCostMap){
        mInteractionCostMap = interactionCostMap;
    }
    
    Map<ModulePairHostPair, InteractionCost> getInteractionCostMap(){
        return mInteractionCostMap;
    }
    
    void setExecutionCostmap(Map<ModuleHost, Double> executionCostMap){
        mExecutionCostMap = executionCostMap;
    }
    
    Map<ModuleHost, Double> getExecutionCostMap(){
        return mExecutionCostMap;
    }
    
    private void printInteractions(){
        for (ModuleHost mh : mExecutionCostMap.keySet())
            System.out.println(mh.toString() + " " + mExecutionCostMap.get(mh));
        System.out.println("---------------------- \\"
                + "------------------------");
        
        for (ModulePairHostPair mhp : mInteractionCostMap.keySet())
            System.out.println(mhp.toString());
        
        System.out.println("************************ \n"
                + "************************ \n"
                + "************************ \n");
    }
    
    private void printCutEdges(){
        for (Module m: mModuleModel.getModuleMap().values()){
            ModuleHost mh = new ModuleHost(m, mHostModel.getHostMap().get(m.getPartitionId()));
            System.out.println(m.getName() + " on: " + m.getPartitionId() + " : " + mExecutionCostMap.get(mh));
        }
        
        for (ModulePairHostPair mhp : mInteractionCostMap.keySet()){
            Module m1 = new Module(mModuleModel.getModuleMap().get(mhp.getModulePair().getModules()[0].getName()));
            Module m2 = new Module(mModuleModel.getModuleMap().get(mhp.getModulePair().getModules()[1].getName()));
            if (!m1.getPartitionId().equals(m2.getPartitionId()))
                System.out.println(mhp.toString() + mhp.getCost().toString());
        }
    }
    
      
    
    
    
    protected abstract void initNodes();
    protected abstract void initEdges();   
    protected abstract void filterHostExecution(IModuleFilter filter, ModuleHost mh);
    protected abstract void filterHostInteraction(IInteractionFilter filter, ModulePairHostPair mhp);
    protected abstract void doPartition();    
}
