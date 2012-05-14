/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;

/**
 *
 * @author nima
 */
public class CloudMonetaryCostModel implements IInteractionCostModel {

    public InteractionCost getInteractionCost(HostModel model, HostPair hp, InteractionData iData) {
        
        /* Measuring cost for data going from h2 to h1 */
        
        // data transfer cost based on the nuber of transferred bytes
        Double h2h1Cost = InteractionRate.getCloudDataMonetaryCost(
              iData.getToParentData(), iData.getToParentCount(), 
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost2(), hp.getHost1()).getValue()); 
                
        // cost of having h2 waiting during the time of data transfer
        h2h1Cost += InteractionRate.getCloudCPUMonetaryCost(
              iData.getToParentData(), iData.getToParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost2(), hp.getHost1()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost2(), hp.getHost1()).getValue(),             
              hp.getHost2().getCpu().getCost().getValue(), hp.getHost2().getCpu().getCost().getScale(),
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost2(), hp.getHost1()).getValue()); 
        
        // cost of having h1 waiting during the time of data transfer
        h2h1Cost += InteractionRate.getCloudCPUMonetaryCost(
              iData.getToParentData(), iData.getToParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost2(), hp.getHost1()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost2(), hp.getHost1()).getValue(),             
              hp.getHost1().getCpu().getCost().getValue(), hp.getHost1().getCpu().getCost().getScale(),
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost2(), hp.getHost1()).getValue()); 
        
        /* Measuring cost for data going from h1 to h2 */
        
        // data transfer cost based on the number of transferred data bytes
        Double h1h2Cost = InteractionRate.getCloudDataMonetaryCost(
              iData.getFromParentData(), iData.getFromParentCount(), 
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost1(), hp.getHost2()).getValue());
        
        // cost of having h2 waiting during the time of data transfer
        h1h2Cost += InteractionRate.getCloudCPUMonetaryCost(
              iData.getFromParentData(), iData.getFromParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost1(), hp.getHost2()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost1(), hp.getHost2()).getValue(),             
              hp.getHost2().getCpu().getCost().getValue(), hp.getHost2().getCpu().getCost().getScale(),
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost1(), hp.getHost2()).getValue());
        
        // cost of having h1 waiting during the time of data transfer
        h1h2Cost += InteractionRate.getCloudCPUMonetaryCost(
              iData.getFromParentData(), iData.getFromParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost1(), hp.getHost2()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost1(), hp.getHost2()).getValue(),             
              hp.getHost1().getCpu().getCost().getValue(), hp.getHost1().getCpu().getCost().getScale(),
              model.getExchangeRateObj().getInteractionCostForHosts(hp.getHost1(), hp.getHost2()).getValue());

        return new InteractionCost(hp, h1h2Cost, h2h1Cost, Boolean.FALSE);
    }
    
}
