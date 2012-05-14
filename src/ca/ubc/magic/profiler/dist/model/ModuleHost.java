/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.control.Constants;

/**
 *
 * @author nima
 */
public class ModuleHost {
    Module mModule;
    Host mHost;
    
    private double mCost;
    
    public ModuleHost(Module m, Host h){
        mModule = new Module(m);
        mModule.setPartitionId(h.getId());
        mHost = h;
    }
    
    public Module getModule(){
        return mModule;
    }
    
    public  Host getHost(){
        return mHost;
    }
    
    public double setExecutionCost(HostModel hostModel, Host baseHost){
        
        if (mModule.isIgnoreRate())
            return mModule.getExecutionCost();
        
        if (mModule.getExecutionCost() == Constants.INFINITE_WEIGHT) 
            mCost = Constants.INFINITE_WEIGHT;
        
        else {
            mCost = hostModel.getExecutionCostModel().getExecutionCost(
                    mHost, baseHost, mModule.getExecutionCost());
        }
        return mCost;
    }
    
    public double getCost(){
        return mCost;
    }
    
    public void setCost(double cost){
        mCost = cost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModuleHost other = (ModuleHost) obj;
        if (this.mModule != other.mModule && 
                (this.mModule == null || !this.mModule.equals(other.mModule))) {
            return false;
        }
        if (this.mHost != other.mHost && 
                (this.mHost == null || !this.mHost.equals(other.mHost))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }        
    
    @Override 
    public String toString(){
        return "M[" + mModule.toString() +"] - H[" + mHost.toString() +"] :: Cost(" + mCost +")";
    }
}
