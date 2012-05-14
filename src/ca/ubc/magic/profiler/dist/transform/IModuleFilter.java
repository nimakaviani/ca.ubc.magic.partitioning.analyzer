/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.ModuleHost;

/**
 *
 * @author nima
 */
public interface IModuleFilter extends IFilter {
    
    public boolean isFilterable(ModuleHost mh);
    
    public double  filter (ModuleHost mh);
    
}
