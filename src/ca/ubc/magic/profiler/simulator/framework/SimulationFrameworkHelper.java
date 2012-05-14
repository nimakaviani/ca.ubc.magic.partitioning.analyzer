/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.simulator.framework;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.Module;
import ca.ubc.magic.profiler.dist.model.ModuleHost;
import ca.ubc.magic.profiler.dist.transform.IModuleFilter;
import java.util.Map;

/**
 *
 * @author nima
 */
public class SimulationFrameworkHelper {

    // The name of the randomly generated algorithms for possible
    // placement of components across hosts.
    public static final String ALGORITHM_RANDOM = "Random";
    
    public static String getTemplateSignature(SimulationUnit template){
        StringBuilder strBuilder = new StringBuilder();
        for (String key :  template.getDistModel().getModuleMap().keySet())
            strBuilder.append("0");
        return strBuilder.toString();
    }
    public static String getSignature(SimulationUnit target, SimulationUnit template){
        
        Map<String, Module> moduleMap = target.getDistModel().getModuleMap();
        Map<String, Module> templateMap = template.getDistModel().getModuleMap();
        
        StringBuilder strBuilder = new StringBuilder();
        for (String key : templateMap.keySet())
            strBuilder.append(moduleMap.get(key).getPartitionId().toString());
              
        return strBuilder.toString();
    }
    
    public static String getNextSignature(String sourceSignature, int base){
        StringBuilder str = new StringBuilder();
        int length = sourceSignature.length();
        int carryForward = 1;
        for (int i = length - 1; i >=0; i--){
            Integer val = Character.getNumericValue(sourceSignature.charAt(i)) +
                    carryForward;
            carryForward = val / base;
            str.append(String.valueOf(val % base));            
        }
        return str.reverse().toString();
    }
    
    public static String getFilteredSignature(String sourceSignature, SimulationUnit templateUnit, IModuleFilter filter){
        
        StringBuilder sig = new StringBuilder();
        Map<String, Module> templateMap = templateUnit.getDistModel().getModuleMap();
        HostModel hostModel = templateUnit.getDistModel().getHostModel();
        
        int counter = 0;
        boolean found = Boolean.FALSE;
        for (Module m : templateMap.values()){            
            for (Host h : hostModel.getHostMap().values()){
                ModuleHost tmpModuleHost = new ModuleHost(new Module(m), h);                           
                filter.filter(tmpModuleHost);
                if (tmpModuleHost.getCost() == Constants.INFINITE_WEIGHT){
                    sig.append(String.valueOf(h.getId() - 1));                
                    found = Boolean.TRUE;
                    break;
                }
            }
            if (!found)
                sig.append(Character.getNumericValue(sourceSignature.charAt(counter)));
            
            found = Boolean.FALSE;
            counter++;
        }
        return sig.toString();
    }
    
    public static SimulationUnit getUnitFromSig(String signature, SimulationUnit templateUnit, boolean increment){
        SimulationUnit unit = new SimulationUnit(templateUnit);
        unit.setName("rand"+String.valueOf(signature.hashCode()));
        unit.setAlgorithmName(ALGORITHM_RANDOM);
        unit.setSignature(signature);
        
        Map<String, Module> moduleMap = unit.getDistModel().getModuleMap();
        Map<String, Module> templateMap = templateUnit.getDistModel().getModuleMap();
        
        int counter = 0;
        for (String key : templateMap.keySet()){
            Module m = moduleMap.get(key);
            if (signature.charAt(counter) != Constants.DONT_CARE_CHAR)
                m.setPartitionId(Character.getNumericValue(signature.charAt(counter)) + ((increment) ? 1 : 0));
            else 
                m.setPartitionId(Constants.INVALID_PARTITION_ID);
            counter++;
        }
        return unit;
    }
    
}
