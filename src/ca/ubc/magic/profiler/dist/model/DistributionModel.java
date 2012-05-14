 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author nima
 */
public class DistributionModel {
    
    Map<String, Module> mModuleMap;
    HostModel    mHostModel;
    
    int mNumberOfPartitions = 0;    
    
    public DistributionModel(){
        mModuleMap = new HashMap<String, Module>();    
        mHostModel = null;
    }
    
    public DistributionModel(ModuleModel moduleModel, HostModel hostModel){
        this(moduleModel.getModuleMap(), hostModel);
    }
    
    public DistributionModel(Map moduleMap, HostModel hostModel){
        mModuleMap = deepClone(moduleMap);
        mHostModel = hostModel;
        mNumberOfPartitions = mHostModel.getNumberOfHosts();
    }
    
    public Map<String, Module> getModuleMap(){
        return mModuleMap;
    }          
    
    public HostModel getHostModel(){
        if (mHostModel == null)
            throw new RuntimeException("Host model is not initialized");
        return mHostModel;
    }
    
    public void setHostModel(HostModel hostModel){
        if (mHostModel.getNumberOfHosts() != mNumberOfPartitions)
            throw new RuntimeException("Mismatch between number of hosts "
                    + "and number of partitions");
        mHostModel = hostModel;
    }
        
    public void addModule(Module module){
        mModuleMap.put(module.getName(), module);
    }
    
    public void updateModulePartition(String partitionName, int partitionId){
        Module m = mModuleMap.get(partitionName);
        m.setPartitionId(partitionId);   
    }       
    
    public int getNumberOfPartitions(){
        return mNumberOfPartitions;
    }
    
    public void setNumberOfPartitions(int numberOfPartitions){
        mNumberOfPartitions = numberOfPartitions;
    }   
    
     private HashMap deepClone(Map map) {
        HashMap newone = (HashMap) ((HashMap)map).clone();
        Iterator it = newone.keySet().iterator();
        while (it.hasNext()) {
            Object newkey = it.next();
            Object deepobj = null, newobj = newone.get(newkey);
            if (newobj instanceof HashMap)
                deepobj = deepClone((HashMap)newobj);
            else if (newobj instanceof String)
                deepobj = (Object)new String((String)newobj);
            else if (newobj instanceof Vector)
                deepobj = ((Vector)newobj).clone();
            else if (newobj instanceof Module)
                deepobj = new Module((Module) newobj);
            newone.put(newkey, deepobj);
        }
        return newone;
    }
}
