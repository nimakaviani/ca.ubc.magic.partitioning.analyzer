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
public interface IInteractionCostModel {
    
    public InteractionCost getInteractionCost(HostModel model, HostPair hp, InteractionData iData);
}
