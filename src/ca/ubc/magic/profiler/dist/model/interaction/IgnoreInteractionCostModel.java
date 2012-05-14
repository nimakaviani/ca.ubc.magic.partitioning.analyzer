/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.HostPair;
import ca.ubc.magic.profiler.dist.model.cost.conversion.CostConversionSingleton;

/**
 *
 * @author nima
 */
public class IgnoreInteractionCostModel implements IInteractionCostModel {

    public InteractionCost getInteractionCost(HostModel model, HostPair hp, InteractionData iData) {
        if (iData.isIgnoreRate()){
            return new InteractionCost(hp, 
                    CostConversionSingleton.getInstance().interactionConvert(iData.getFromParentData(), iData.getFromParentCount()),
                    CostConversionSingleton.getInstance().interactionConvert(iData.getToParentData(), iData.getToParentCount()),
                    Boolean.TRUE);
        }else {
         
            System.err.println("IgnoreRate is not set for the InteractionData");
            return new InteractionCost(hp, Double.valueOf(iData.getFromParentData()), 
                    Double.valueOf(iData.getToParentData()), Boolean.FALSE);
        }
    }
    
}
