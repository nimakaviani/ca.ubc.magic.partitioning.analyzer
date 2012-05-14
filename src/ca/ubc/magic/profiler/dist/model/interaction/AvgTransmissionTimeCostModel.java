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
public class AvgTransmissionTimeCostModel implements IInteractionCostModel {

    public InteractionCost getInteractionCost(HostModel model, HostPair hp, InteractionData iData) {
        double h1h2Cost = InteractionRate.getTransmissionTime(
              iData.getFromParentData(), iData.getFromParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost1(), hp.getHost2()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost1(), hp.getHost2()).getValue()); 

        double h2h1Cost = InteractionRate.getTransmissionTime(
              iData.getToParentData(), iData.getToParentCount(), 
              model.getExchangeRateObj().getInteractionRateForHosts(hp.getHost2(), hp.getHost1()).getValue(),
              model.getExchangeRateObj().getHandShakeCost(), model.getExchangeRateObj().getLiftingLoweringCost(),
              model.getExchangeRateObj().getInteractionLatencyForHosts(hp.getHost2(), hp.getHost1()).getValue()); 

        return new InteractionCost(hp, h1h2Cost, h2h1Cost, Boolean.FALSE);
    }
    
}
