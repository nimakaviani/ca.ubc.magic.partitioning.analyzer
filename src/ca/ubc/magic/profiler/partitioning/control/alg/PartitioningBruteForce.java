/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg;

import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;
import ca.ubc.magic.profiler.partitioning.control.alg.PartitionerFactory.PartitionerType;
import ca.ubc.magic.profiler.simulator.framework.SimulationFrameworkHelper;
import ca.ubc.magic.profiler.simulator.framework.SimulationUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nima
 */
public class PartitioningBruteForce {
    
    private ModuleModel mModuleModel;
    private HostModel   mHostModel;
    
    private SimulationUnit  mTemplate;
    private String          mTemplateSig;
    
    public PartitioningBruteForce(ModuleModel moduleModel, HostModel hostModel){
        mModuleModel = moduleModel;
        mHostModel = hostModel;
        
        mTemplate = new SimulationUnit(mModuleModel.getName(), null, 
                        new DistributionModel(mModuleModel, mHostModel));
        
        mTemplateSig = SimulationFrameworkHelper.getSignature(mTemplate, mTemplate);
    }
    
    public void run(PartitionerType partitionerType, final List predefinedModuleHostPlacement){
        
        String sig = SimulationFrameworkHelper.getNextSignature(mTemplateSig, mHostModel.getNumberOfHosts());
        
        while (!sig.equals(mTemplateSig)){
            
            SimulationUnit unit = SimulationFrameworkHelper.getUnitFromSig(sig, mTemplate, Boolean.TRUE);
            AbstractPartitioner partitioner = (AbstractPartitioner) PartitionerFactory.getPartitioner(partitionerType);
            
            if (!mModuleModel.isSimulation())
                partitioner.init(new ModuleModel(mModuleModel), mHostModel);
            else
                partitioner.init(new ModuleModel(mModuleModel), mHostModel, predefinedModuleHostPlacement);
            
            partitioner.setInteractionCostMap(getInteractionCostMap(unit));
            partitioner.partition();
            
            sig = SimulationFrameworkHelper.getNextSignature(sig, mHostModel.getNumberOfHosts());
        }
    }
    
    private Map<ModulePairHostPair, InteractionCost> getInteractionCostMap(SimulationUnit unit){
        
        Map<ModulePairHostPair, InteractionCost> interactionCostMap = 
                new HashMap<ModulePairHostPair, InteractionCost>();
        
        ////////////////////////////////////////////////
        // initializing data exchange cost for the model
        for(ModulePair mp : mModuleModel.getModuleExchangeMap().keySet()){                                   
            InteractionData iData = mModuleModel.getModuleExchangeMap().get(mp);
            
            HostPair hp = new HostPair(mHostModel.getHostMap().get(
                    unit.getDistModel().getModuleMap().get(mp.getModules()[0].getName()).getPartitionId()), 
                    mHostModel.getHostMap().get(
                    unit.getDistModel().getModuleMap().get(mp.getModules()[1].getName()).getPartitionId()));
            
            ModulePairHostPair s2tmp = new ModulePairHostPair(mp, hp);                    
            s2tmp.setCost(hp.getInteractionCost(mHostModel, iData));
            interactionCostMap.put(s2tmp, s2tmp.getCost());                                                              
        }
        return interactionCostMap;
    }
    
}
