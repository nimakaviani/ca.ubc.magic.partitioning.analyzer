/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.execution;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.cost.conversion.CostConversionSingleton;
import ca.ubc.magic.profiler.dist.model.platform.RateHelper;

/**
 *
 * @author nima
 */
public class ExecutionRate {
    
    private static double getExecutionTimePrivate(Host targetHost, Host baseHost, 
            double usage, double normalizationFactor) throws RuntimeException{   
        
        if (usage == Constants.INFINITE_WEIGHT)
            return Constants.INFINITE_WEIGHT;
        
        usage = RateHelper.normalizeFromNanoSec(usage, normalizationFactor);
        usage = CostConversionSingleton.getInstance().executionConvert(usage, 1.0);
        double ratio = baseHost.getCpu().getCapability().getValue() / targetHost.getCpu().getCapability().getValue();        
        return usage * ratio;
    }
    
    public static double getExecutionTime(Host targetHost, Host baseHost, 
            double usage) throws RuntimeException{    
        
        return getExecutionTimePrivate(targetHost, baseHost, usage, Constants.MILLISEC_NORMALIZATION_FACTOR);
        
    }  
    
    public static double getExecutionCost(Host targetHost, Host baseHost, double usage) throws RuntimeException{    
       
        double usageRatio = getExecutionTimePrivate(targetHost, baseHost, usage, targetHost.getCpu().getCost().getScale());
        return usageRatio * targetHost.getCpu().getCost().getValue();
    }   
}
