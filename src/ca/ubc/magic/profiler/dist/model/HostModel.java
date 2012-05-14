/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.execution.IExecutionCostModel;
import ca.ubc.magic.profiler.dist.model.interaction.IInteractionCostModel;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionModel;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nima
 */
public class HostModel {
    
    Map<Integer, Host>  mHostMap = new HashMap<Integer, Host>();
    InteractionModel mER = new InteractionModel();  
    
    private IInteractionCostModel mInteractionCostModel = null;
    private IExecutionCostModel mExecutionCostModel = null;
    
    int mNumberOfHosts = 0;
    
    public Host getDefaultHost(){
        for (Host h : mHostMap.values()){
            if (h.getDefault())
                return h;
        }
        return null;
    }
    
    public void addHost(Host host){
        mHostMap.put(host.getId(), host);                
    }
    
    public Map<Integer, Host> getHostMap(){
        return mHostMap;
    }    
    
    public void setNumberOfHosts(int numberOfHosts) {
       mNumberOfHosts = numberOfHosts;
    }
    
    public int getNumberOfHosts(){
       return mNumberOfHosts;
    }
    
    public InteractionModel getExchangeRateObj(){
        return mER;
    }    
    
    public IInteractionCostModel getInteractionCostModel(){
        return mInteractionCostModel;
    }
    
    public void setInteractionCostModel(IInteractionCostModel interactionCostModel){
        mInteractionCostModel = interactionCostModel;
    }
        
    public IExecutionCostModel getExecutionCostModel(){
        return mExecutionCostModel;
    }
    
    public void setExecutionCostModel(IExecutionCostModel executionCostModel){
        mExecutionCostModel = executionCostModel;
    }

    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (Host h : mHostMap.values())
            builder.append(h.toString()).append("\n");
        
        builder.append(mER.toString());
        return builder.toString();
    }
}
