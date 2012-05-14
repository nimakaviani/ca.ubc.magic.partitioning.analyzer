/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.model.Host;
import ca.ubc.magic.profiler.dist.model.HostModel;
import ca.ubc.magic.profiler.dist.model.platform.CPUCapability;
import ca.ubc.magic.profiler.dist.model.platform.Capability;
import ca.ubc.magic.profiler.dist.model.platform.CostUnit;
import ca.ubc.magic.profiler.dist.model.platform.DataExchangeCapability;
import ca.ubc.magic.profiler.dist.model.platform.DataExchangeCostUnit;
import ca.ubc.magic.profiler.dist.model.platform.ExecutionCostUnit;
import ca.ubc.magic.profiler.dist.model.platform.LatencyUnit;
import ca.ubc.magic.profiler.dist.model.platform.MemoryCapability;
import ca.ubc.magic.profiler.dist.model.platform.StorageCostUnit;

/**
 *
 * @author nima
 */
public class HostHandler {
    
    private int mCurrentPartitionId = -1;
    private Host mCurrentHost = null;
    private String[] mAttr = new String[2];
    private boolean isDefaultHostSet = false;
    
    private Capability  mCapability;
    private CostUnit    mCost;
    private LatencyUnit mLatency;
    
    HostModel mHostModel = new HostModel();
    
    public void startHosts(String hosts) throws Exception {
        mHostModel.setNumberOfHosts(Integer.parseInt(hosts));
    }
    
    public void startHost(String id, String defaultHost) throws Exception {       
        if (mCurrentPartitionId != -1)
            throw new Exception("Incorrect XML file for distribution.");        
        boolean defHost = false;
        if (defaultHost != null && isDefaultHostSet)
            throw new Exception("Default host is already set.");
        else if (defaultHost != null && defaultHost.equals("true")){
            defHost = true;
            isDefaultHostSet = true;
        }
        mCurrentPartitionId = Integer.parseInt(id);
        mCurrentHost = new Host(mCurrentPartitionId, defHost);
    }
    
    public void startCPU(){
       mCapability = new CPUCapability();
       mCost = new ExecutionCostUnit();
    }
    
    public void endCPU(){
        mCurrentHost.setCpuInfo(mCapability, mCost);
    }
    
    public void startMemory(){
       mCapability = new MemoryCapability();
       mCost = new StorageCostUnit();
    }
    
    public void endMemory(){
         mCurrentHost.setMemoryInfo(mCapability, mCost);
    }
    
    public void startExchangeRate(String from, String to){
        mAttr[0] = from;
        mAttr[1] = to;
        
        mCapability = new DataExchangeCapability();
        mCost = new DataExchangeCostUnit();
        mLatency = new LatencyUnit();
    }
    
    public void endExchangeRate(){
        mHostModel.getExchangeRateObj().addInteractionRate(
                mHostModel.getHostMap().get(Integer.valueOf(mAttr[0])), 
                mHostModel.getHostMap().get(Integer.valueOf(mAttr[1])), 
                mCapability, mCost, mLatency);
    }
    
    public void startCapability(String scale){
        mCapability.setScale(scale);
    }
    
    public void endCapability(String value){
        mCapability.setValue(Double.valueOf(value));
    }
    
    public void startCost(String unit, String scale){
        mCost.setScale(scale);
        mCost.setUnit(Double.valueOf(unit));
    }
    
    public void endCost(String value){
        mCost.setValue(Double.valueOf(value));
    }
    
    public void startLatency(String scale){
        mLatency.setScale(scale);
    }
    
    public void endLatency(String value){
        mLatency.setValue(Double.valueOf(value));
    }
    
    public void endHost(){
        mHostModel.addHost(mCurrentHost);
        mCurrentPartitionId = -1;
        mCurrentHost = null;
    }    
}
