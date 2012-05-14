/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.DistributionModel;
import ca.ubc.magic.profiler.dist.model.Module;

/**
 *
 * @author nima
 */
public class DistHandler {
    
    DistributionModel mDistributionModel = new DistributionModel();   
    
    public void startModules(String partitions){
        mDistributionModel.setNumberOfPartitions(Integer.parseInt(partitions));
    }
    
    public void endModules(){
        
    }
    
    public void startModule(String name, String partitionId){
        Module m = new Module(name, Integer.valueOf(partitionId));
        mDistributionModel.addModule(m);
    }
    
    public void endModule(){
        
    }        
}
