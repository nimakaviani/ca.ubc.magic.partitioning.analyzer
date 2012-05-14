/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.cost.conversion.CostConversionSingleton;
import ca.ubc.magic.profiler.dist.model.platform.RateHelper;

/**
 *
 * @author nima
 */
public class InteractionRate {
    public static final int BYTE_2_BIT_RATE = 8;
    
    /**
     * The return value for the method will always be in Milliseconds
     * 
     * @param bytes                 amount of bytes of data transferred
     * @param count                 Number of times data exchange happened
     * @param bandwidth             The communication bandwidth
     * @param handshakeCost         The cost of handshake
     * @param liftingLoweringCost   The cost of data lifting and lowering
     * @param latency               The latency for data transfer
     * @return                      The return value for the transmission time will
     *                              always be in milliseconds
     */
    public static double getTransmissionTime(long bytes, long count, 
            double bandwidth, double handshakeCost, double liftingLoweringCost, double latency){
        if (bytes == 0)
            return 0.0;       
        
        double poissonQueueingDelay = ((bytes * BYTE_2_BIT_RATE) / bandwidth) /
                (Math.pow(bandwidth / count, 2) + ((bandwidth * bytes) / (Math.pow(count, 2))));
        
            return Math.ceil(poissonQueueingDelay +
               CostConversionSingleton.getInstance().interactionConvert((liftingLoweringCost * bytes +
               handshakeCost + ((bytes * BYTE_2_BIT_RATE) / bandwidth) + latency), count));
    }
    
    public static double getCloudDataMonetaryCost(long bytes, long count, double interactionCost){
        if (bytes == 0 || count == 0)
            return 0.0;   
        
        double dataCost = CostConversionSingleton.getInstance().interactionConvert(bytes, count) * interactionCost;
        
        return dataCost;
    }
    
    public static double getCloudCPUMonetaryCost(long bytes, long count, 
            double bandwidth, double handshakeCost, double liftingLoweringCost, double latency,   
            double executionCost, double executionScale,
            double interactionCost){
        
        if (bytes == 0 || count == 0)
            return 0.0;   
        
        double cpuIdleTime = getTransmissionTime(bytes, count, bandwidth, handshakeCost,
                liftingLoweringCost, latency);
        
        // The amount of money for deployment after normalization for a given host
        double cpuIdleCost = RateHelper.normalizeToNanoSec(cpuIdleTime, Constants.MILLISEC_NORMALIZATION_FACTOR);
        cpuIdleCost = RateHelper.normalizeFromNanoSec(cpuIdleCost, executionScale) * executionCost;
        
        return cpuIdleCost;
    }
}
