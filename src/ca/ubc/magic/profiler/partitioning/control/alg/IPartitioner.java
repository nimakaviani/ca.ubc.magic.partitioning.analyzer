/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.transform.IFilter;
import java.util.List;

/**
 *
 * @author nima
 */
public interface IPartitioner {
        
    public void partition();
    
    public void init(ModuleModel mModel, HostModel hModel);        
    
    public void init(ModuleModel mModel, HostModel hModel, List predefinedModuleHostPlacement);
    
    public void addFilter(IFilter mFilter);
    
    public void removeFilter(IFilter mFilter);
    
    public String getSolution();
}
