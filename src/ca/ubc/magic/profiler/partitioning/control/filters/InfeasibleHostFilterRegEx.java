/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import java.util.HashSet;

/**
 *
 * @author nima
 */
public class InfeasibleHostFilterRegEx extends InfeasibleHostFilter implements IModuleFilter {
    
    
    public InfeasibleHostFilterRegEx(ModuleModel moduleModel, HostModel hostModel,
            String name, String[] moduleNames, int hostId){     
        super(moduleModel, hostModel, name, moduleNames, hostId);
    }
    
    @Override
    public boolean isFilterable(ModuleHost mh) {
        
        // finds the corresponding modules from the modulemodel and updates
        // their edge weights to infinite
        if (mh.getHost().equals(mHost)){
            Module m = mh.getModule();
            for (Module tmpM : mModuleSet){
                if (m.getName().contains(tmpM.getName())){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    @Override
    public double filter(ModuleHost mh){
        return Constants.INFINITE_WEIGHT;
    }
    
    @Override
    protected void initFilter(ModuleModel moduleModel, HostModel hostModel, String[] moduleNames, int hostId){        
        if (hostModel != null && moduleModel != null){
           mModuleSet = new HashSet<Module>(); 
           for (String moduleName : moduleNames){
               moduleName = moduleName.trim();
               for (Module m : moduleModel.getModuleMap().values())
                   if (m.getName().contains(moduleName))
                        mModuleSet.add(new Module(m));
           }
           mHost = hostModel.getHostMap().get(hostId);
           
           // when applying the filter, first sets the partition Id for all
           // the modules stored in the filter to the partition Id of the host
           // these partitions need to reside on.
           for (Module m : mModuleSet){
               if (m == null)
                   throw new RuntimeException("ModuleSet for the filter contains"
                           + "a null element");
               m.setPartitionId(mHost.getId());
           }
        }else
            throw new RuntimeException("No proper setting of the module model or the host models");
    }
    
}
