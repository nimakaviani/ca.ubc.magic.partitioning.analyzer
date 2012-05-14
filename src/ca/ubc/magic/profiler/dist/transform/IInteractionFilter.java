/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.ModulePairHostPair;

/**
 *
 * @author nima
 */
public interface IInteractionFilter extends IFilter {
    
    public boolean isFilterable(ModulePairHostPair mph);
    
    public double filter(ModulePairHostPair mph);
    
}
