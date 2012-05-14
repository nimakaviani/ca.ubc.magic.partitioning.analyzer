/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.transform;

import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import java.util.Set;

/**
 *
 * @author nima
 */
public interface IFilter {
    
    public Set getFilterSet();
    
    public String getFilterName();
    
    public void setFilterName(String name);
    
    public String getFilterAsString();
    
    public void   setStringToFilter(ModuleModel moduleModel, HostModel hostModel, String stringFilter);
}
