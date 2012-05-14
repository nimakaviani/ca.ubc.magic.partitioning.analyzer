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

/**
 *
 * @author nima
 */
public class SimplexPartitionerExtended4Cost extends SimplexPartitionerExtended {
    
    private static final int CLOUD_MAX_ITERATIONS = 3000;
    
     @Override
    public void init (ModuleModel mModel, HostModel hModel){
        super.init(mModel, hModel);      
        if (mHostModel.getNumberOfHosts() != 2)
            throw new RuntimeException("Simplex partitioner can be applied to 2 hosts only");
        mSimplexModel = new SimplexModelExtended4Cost(mSize);
    }
    
    @Override
    protected void initEdges(){                
        for (ModulePair mPair : mModuleModel.getModuleExchangeMap().keySet()){                                   
            ModulePairHostPair baseMHP = new ModulePairHostPair(
                    mPair, new HostPair(getDefaultHost(), TwoHostHelper.getTargetHost(mHostModel)));
            ModulePairHostPair targetMHP = new ModulePairHostPair(
                    mPair, new HostPair(TwoHostHelper.getTargetHost(mHostModel), getDefaultHost()));
            Module[] mArray = mPair.getModules();
            
            // This is to add cost of having m1 on premise and m2 in the cloud
            // put into the AdjancencyMatrix[i][j]. Only h2toh1cost is taken into 
            // consideration as the other cost is multiplied by its associated 
            // value and is then normalized to 0. In this case, h2 to h1 cost would
            // be equal to the cost of transfering data from cloud(h2) to premise(h1)
            mSimplexModel.addEdge(mArray[0].getName(),
                    mArray[1].getName(), mInteractionCostMap.get(baseMHP).getTotalCost());
            
            // This is the cost of having m2 on premise and m1 in the cloud
            // put into adjacencymatrix[j][i]. Only h2toh1cost is taken into 
            // consideration as the other cost is multiplied by its associated 
            // value and is then normalized to 0. In this case, h1 to h2 cost would
            // be equal to the cost of transfering data from cloud(h1) to premise(h2)
             mSimplexModel.addEdge(mArray[1].getName(),
                    mArray[0].getName(), mInteractionCostMap.get(targetMHP).getTotalCost());
        }
    }
    
    @Override
    protected int getMaxIterations(){
        return SimplexPartitionerExtended4Cost.CLOUD_MAX_ITERATIONS;
    }
}
