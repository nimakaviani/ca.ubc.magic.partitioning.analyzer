/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

/**
 *
 * @author nima
 */
public class ConstraintSwitches {
    
    private boolean mIsSyntheticNodeActivated = Boolean.FALSE;

    public boolean isSyntheticNodeActivated() {
        return mIsSyntheticNodeActivated;
    }

    public void setSyntheticNodeActivated(boolean syntheticNodeActivate) {
        this.mIsSyntheticNodeActivated = syntheticNodeActivate;
    }    
}
