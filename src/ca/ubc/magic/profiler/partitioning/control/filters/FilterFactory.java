/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.transform.IFilter;

/**
 *
 * @author nima
 */
public class FilterFactory {
    
    public static final String PLACEMENT_FILTER = "Host Placement Filter";
    
    public static IFilter getFilter(String filterName){
        if (filterName.toLowerCase().equals(FilterFactory.PLACEMENT_FILTER.toLowerCase()))
            return FilterHelper.setModuleFilter(null, null);
        else
            throw new RuntimeException("Filter name not found");
    }
    
}
