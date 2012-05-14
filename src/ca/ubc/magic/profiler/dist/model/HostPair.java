/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.interaction.InteractionCost;
import ca.ubc.magic.profiler.dist.model.interaction.InteractionData;

/**
 *
 * @author nima
 */
public class HostPair {    
        
    private Host mHost1;
    private Host mHost2;

    public HostPair (Host host1, Host host2){
        mHost1 = host1;
        mHost2 = host2;
    }
    
    public Host getHost1(){
        return mHost1;
    }
    
    public Host getHost2(){
        return mHost2;
    }
    
    public InteractionCost getInteractionCost(HostModel model, InteractionData iData){
        return model.getInteractionCostModel().getInteractionCost(model, this, iData);
    }

    @Override
    public boolean equals(Object obj){

        if (!(obj instanceof HostPair))
            return false;
        if (mHost1.equals(((HostPair)obj).mHost1) &&
                mHost2.equals(((HostPair)obj).mHost2))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.mHost1 != null ? this.mHost1.hashCode() : 0);
        hash = 97 * hash + (this.mHost2 != null ? this.mHost2.hashCode() : 0);
        return hash;
    }        

    @Override
    public String toString(){
        return "[" + mHost1.toString() + ":" + mHost2.toString() +"]";
    }
}
