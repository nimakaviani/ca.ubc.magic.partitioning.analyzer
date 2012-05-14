/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;

/**
 *
 * This is a wrapper class indicating that always
 * module 1 is on host1 and module2 is on host 2
 * 
 * @author nima
 */
public class ModulePairHostPair {
    
    private ModulePair mModulePair;
    private HostPair mHostPair;
    
    private InteractionCost mCost;
    
    public ModulePairHostPair(ModulePair mp, HostPair hp){
        mModulePair = mp;
        mHostPair = hp;
    }
    
    public ModulePair getModulePair(){
        return mModulePair;
    }
    
    public HostPair getHostPair(){
        return mHostPair;
    }        
    
    public InteractionCost getCost(){
        return mCost;
    }
    
    public void setCost(InteractionCost cost){
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
        final ModulePairHostPair other = (ModulePairHostPair) obj;
        if (this.mModulePair != other.mModulePair && 
                (this.mModulePair == null || !this.mModulePair.equals(other.mModulePair))) {
            return false;
        }
        if (this.mHostPair != other.mHostPair && 
                (this.mHostPair == null || !this.mHostPair.equals(other.mHostPair))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.mModulePair != null ? this.mModulePair.hashCode() : 0);
        hash = 83 * hash + (this.mHostPair != null ? this.mHostPair.hashCode() : 0);
        return hash;
    }        
    
    @Override
    public String toString(){
        return "Modules[" + mModulePair.toString() + "]" + "Hosts[" + mHostPair.toString() + "] :: " + mCost.toString();
    }
}
