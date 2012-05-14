/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.platform.CPUCapability;
import ca.ubc.magic.profiler.dist.model.platform.ExecutionCostUnit;
import ca.ubc.magic.profiler.dist.model.platform.StorageCostUnit;
import ca.ubc.magic.profiler.dist.model.platform.MemoryCapability;
import ca.ubc.magic.profiler.dist.model.platform.Capability;
import ca.ubc.magic.profiler.dist.model.platform.CostUnit;

/**
 *
 * @author nima
 */
public class Host {
    
    int    mId;    
    CPU    mCpu;
    Memory mMem;
    boolean mDefault = false;
       
    
    public Host(int id, boolean defaultHost){
        mId  = id;      
        mDefault = defaultHost;
    }
            
    public void setCpuInfo(Capability capability, CostUnit cost){
       
        mCpu = new CPU(capability, cost);
        
    }
    
    public void setMemoryInfo(Capability capability, CostUnit cost){      
        
        mMem = new Memory(capability, cost);
    }
    
    public void setDefault(boolean def){
        mDefault = def;
    }
    
    public boolean getDefault(){
        return mDefault;
    }
    
    public int getId(){
        return mId;
    }         
    
    public CPU getCpu(){
        return mCpu;
    }
    
    public Memory getMemory(){
        return mMem;
    }
    
    @Override
    public String toString(){
        return mId + " :: CPU[" + mCpu.toString() +"] Memory[" + mMem.toString() + "]";
    }        
    
    @Override
    public boolean equals(Object obj){        
        if (!(obj instanceof Host))
            return false;
        Host h = (Host) obj;
        if (mId == h.getId() && mCpu.equals(h.getCpu()) &&
                mMem.equals(h.getMemory()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.mId;
        hash = 97 * hash + (this.mCpu != null ? this.mCpu.hashCode() : 0);
        hash = 97 * hash + (this.mMem != null ? this.mMem.hashCode() : 0);
        return hash;
    }
    
    
    protected abstract class HostUnit extends UnitModel {
        
        public HostUnit(double capabilityValue, String capabilityScale, 
                double costValue, double costUnit, String costScale){
            
        }
        
        public HostUnit(Capability capability, CostUnit cost){
             mCapability = capability;
             mCost = cost;
        }
    }
     
    public class CPU extends HostUnit {

        public CPU(double capabilityValue, String capabilityScale, 
                double costValue, double costUnit, String costScale){
            super(capabilityValue, capabilityScale, costValue, costUnit, costScale);
            mCapability = new CPUCapability(capabilityValue, capabilityScale);
            mCost = new ExecutionCostUnit(costValue, costUnit, costScale);
        }
        
        public CPU(Capability capability, CostUnit cost){
            super(capability, cost);
        }
    }
    
    public class Memory extends HostUnit{

        public Memory(double capabilityValue, String capabilityScale, 
                double costValue, double costUnit, String costScale){
            super(capabilityValue, capabilityScale, costValue, costUnit, costScale);
            mCapability = new MemoryCapability(capabilityValue, capabilityScale);
            mCost = new StorageCostUnit(costValue, costUnit, costScale, this);
        }
        
        public Memory(Capability capability, CostUnit cost){
            super(capability, cost);
        }
    }   
}
