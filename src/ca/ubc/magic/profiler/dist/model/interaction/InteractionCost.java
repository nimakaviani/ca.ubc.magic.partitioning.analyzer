/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.model.HostPair;

/**
 *
 * @author nima
 */
public class InteractionCost {
    
    private Double mH1yoH2Cost = 0.0;
    private Double mH2toH1Cost = 0.0;
    
    private HostPair mHostPair;
    
    private boolean mRounded = Boolean.TRUE;
    
    public InteractionCost(HostPair hostPair, Double h1toH2Cost, Double h2toH1Cost, boolean rounded){
        mH1yoH2Cost = h1toH2Cost;
        mH2toH1Cost = h2toH1Cost;
        mHostPair = hostPair;
        mRounded = rounded;
    }
    
    public Double getH1toH2Cost(){
        if (mRounded)
            return getH1toH2CostRounded();
        return mH1yoH2Cost;
    }
    
    public Double getH1toH2CostRounded(){
        return Double.valueOf(Math.round(mH1yoH2Cost));
    }
    
    public Double getH2toH1Cost(){
        if (mRounded)
            return getH2toH1CostRounded();
        return mH2toH1Cost;
    }
    
    private Double getH2toH1CostRounded(){
        return Double.valueOf(Math.round(mH2toH1Cost));
    }
    
    public Double getTotalCost(){
        if (mRounded)
            return getTotalRoundedCost();
        return mH1yoH2Cost + mH2toH1Cost;
    }
    
    private Double getTotalRoundedCost(){
        return Double.valueOf(Math.round(mH1yoH2Cost + mH2toH1Cost));
    }
    
    public Double getAvgCost(){
        if (mRounded)
            return getAvgCostRounded();
        return (mH1yoH2Cost + mH2toH1Cost) / 2.0;
    }
    
    public Double getAvgCostRounded(){
        return Double.valueOf(Math.round((mH1yoH2Cost + mH2toH1Cost) / 2.0));
    }
    
    public HostPair getHostPair(){
        return mHostPair;
    }
    
    @Override
    public String toString(){
        return "[InteractionCost 1-2: " + mH1yoH2Cost + " 2-1: " + mH2toH1Cost + "]";
    }
}
