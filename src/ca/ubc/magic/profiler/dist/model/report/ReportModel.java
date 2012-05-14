/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.report;

import ca.ubc.magic.profiler.dist.model.Host;
import java.util.Collection;

/**
 *
 * @author nima
 */
public class ReportModel {
    
    CostModel mCost;
    UnitModel mUnit;
    Collection<Host> mHosts;
    private boolean mFinalized = false;
    
    public ReportModel( Collection<Host> hostCollection){
        mCost = new CostModel();
        mUnit = new UnitModel();
        mHosts = hostCollection;
    }
    
    public CostModel getCostModel(){
        return mCost;
    }
    
    public UnitModel getUnitModel(){
        return mUnit;
    }
    
    public Collection<Host> getHostModel(){
        return mHosts;
    }
    
    public void setFinalized(boolean finalized) {
        this.mFinalized = finalized;
    }

    public boolean isFinalized() {
        return mFinalized;
    }
}
