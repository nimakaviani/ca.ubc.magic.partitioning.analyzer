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
public class SimpleMonetaryCostModel implements IInteractionCostModel {

    public InteractionCost getInteractionCost(HostModel model, HostPair hp, InteractionData iData) {
        
        double h1h2Cost = iData.getFromParentData() * model.getExchangeRateObj().getInteractionCostForHosts(
                hp.getHost1(), hp.getHost2()).getValue();
        
        double h2h1Cost = iData.getToParentData() * model.getExchangeRateObj().getInteractionCostForHosts(
                hp.getHost1(), hp.getHost2()).getValue();
        
        return new InteractionCost(hp, h1h2Cost, h2h1Cost, Boolean.FALSE);
        
    }
    
}
