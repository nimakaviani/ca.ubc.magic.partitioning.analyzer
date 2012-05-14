/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

import ca.ubc.magic.profiler.dist.model.platform.Capability;
import ca.ubc.magic.profiler.dist.model.platform.CostUnit;

/**
 *
 * @author nima
 */
public class UnitModel {
    
    protected Capability mCapability;
    protected CostUnit mCost;
    
    
    @Override
    public String toString(){
        return mCapability.toString() + " " + mCost.toString();
    }

    public Capability getCapability(){
        return mCapability;
    }

    public CostUnit getCost() {
        return mCost;
    };
}
