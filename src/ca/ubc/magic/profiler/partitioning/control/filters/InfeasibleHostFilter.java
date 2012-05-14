/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.filters;

import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.ModuleModel;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 *
 * @author nima
 */
public class InfeasibleHostFilter implements IModuleFilter {
    public static final String HOST_DELIMITER = "::";
        
    protected Set<Module> mModuleSet;
    protected Host        mHost;
    protected String mName;

    public InfeasibleHostFilter(ModuleModel moduleModel, HostModel hostModel,
            String name, String[] moduleNames, int hostId){     
        
        // initializing the filter
        initFilter(moduleModel, hostModel, moduleNames, hostId);
        mName = name;
    }
    
    public boolean isFilterable(ModuleHost mh) {
        
        // finds the corresponding modules from the modulemodel and updates
        // their edge weights to infinite
        if (mh.getHost().equals(mHost)){
            Module m = mh.getModule();
            for (Module tmpM : mModuleSet){
                if (m.equals(tmpM)){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    
    @Override
    public double filter (ModuleHost mh){
        return Constants.INFINITE_WEIGHT;
    }
    
    public Set getFilterSet(){
        return mModuleSet;
    }
    
    public void setFilterName(String name){
        mName = name;
    }
    
    public String getFilterName(){
        return mName;
    }
    
    public String getFilterAsString() {
        String[] moduleNames = new String[mModuleSet.size()];
        int index = 0;
        for (Module m : mModuleSet){
            moduleNames[index++] = m.getName();
        }
        return (StringUtils.arrayToCommaDelimitedString(moduleNames) + 
                HOST_DELIMITER + mHost.getId()).replaceAll(",", ", ");
    }

    public void setStringToFilter(ModuleModel moduleModel, HostModel hostModel, String stringFilter) {
        stringFilter = stringFilter.replaceAll(", ", ",");
        String[] moduleNames = StringUtils.commaDelimitedListToStringArray(
                stringFilter.substring(0, stringFilter.indexOf(HOST_DELIMITER)));
        int hostId = Integer.valueOf(stringFilter.substring(
                stringFilter.indexOf(HOST_DELIMITER) + HOST_DELIMITER.length()));
        initFilter(moduleModel, hostModel, moduleNames, hostId);
    }
    
    protected void initFilter(ModuleModel moduleModel, HostModel hostModel, String[] moduleNames, int hostId){        
        if (hostModel != null && moduleModel != null){
           mModuleSet = new HashSet<Module>(); 
           for (String moduleName : moduleNames){
               moduleName = moduleName.trim();
               if (moduleModel.getModuleMap().get(moduleName) != null)
                    mModuleSet.add(new Module(moduleModel.getModuleMap().get(moduleName)));
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
