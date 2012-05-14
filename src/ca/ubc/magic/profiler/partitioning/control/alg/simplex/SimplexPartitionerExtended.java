/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.simplex;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.model.ModulePair;
import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;
import ca.ubc.magic.profiler.dist.model.TwoHostHelper;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionCostHelper;

/**
 *
 * @author nima
 */
public class SimplexPartitionerExtended extends SimplexPartitioner {
    
    @Override
    public void init (ModuleModel mModel, HostModel hModel){
        super.init(mModel, hModel);      
        if (mHostModel.getNumberOfHosts() != 2)
            throw new RuntimeException("Simplex partitioner can be applied to 2 hosts only");
        mSimplexModel = new SimplexModelExtended(mSize);
    }
    
    @Override
     protected void initEdges(){                
        for (ModulePair mPair : mModuleModel.getModuleExchangeMap().keySet()){                                   
            ModulePairHostPair baseMHP = new ModulePairHostPair(
                    mPair, new HostPair(getDefaultHost(), TwoHostHelper.getTargetHost(mHostModel)));
            ModulePairHostPair targetMHP = new ModulePairHostPair(
                    mPair, new HostPair(TwoHostHelper.getTargetHost(mHostModel), getDefaultHost()));
            InteractionCost cost = InteractionCostHelper.mergeCostsIgnoreHost(
                    mInteractionCostMap.get(baseMHP),
                    mInteractionCostMap.get(targetMHP));
            Module[] mArray = mPair.getModules();
            mSimplexModel.addEdge(mArray[0].getName(),
                    mArray[1].getName(), cost.getTotalCost()); 
            mSimplexModel.addEdge(mArray[1].getName(),
                    mArray[0].getName(), cost.getTotalCost()); 
        }
    }
}
