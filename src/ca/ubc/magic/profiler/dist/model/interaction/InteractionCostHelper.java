/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.model.HostPair;

/**
 *
 * @author nima
 */
public class InteractionCostHelper {
    
    public static InteractionCost mergeCosts(InteractionCost cost1, InteractionCost cost2){
        if (!cost1.getHostPair().equals(cost2.getHostPair()))
            throw new RuntimeException("InteractionCosts do not represent the same host distributions");
        
        return mergeCostsInternal(cost1.getHostPair(), cost1, cost2);
    }
    
    public static InteractionCost mergeCostsIgnoreHost(InteractionCost cost1, InteractionCost cost2){
        return mergeCostsInternal(null, cost1, cost2);
    }
    
    private static InteractionCost mergeCostsInternal(HostPair hp, InteractionCost cost1, InteractionCost cost2){
        return new InteractionCost(hp,
                (cost1.getH1toH2Cost() + cost2.getH1toH2Cost()) / 2.0 ,
                (cost1.getH2toH1Cost() + cost2.getH2toH1Cost()) / 2.0,
                Boolean.FALSE);
    }
    
}
