/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.interaction;

import ca.ubc.magic.profiler.dist.control.MathUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nima
 */
public class InteractionData {
    
    List<Long> mFromParentData;
    List<Long>  mToParentData;
    List<Long>  mFromParentCount;
    List<Long>  mToParentCount;
    
    boolean mIgnoreRate = Boolean.FALSE;
    
    public InteractionData(){
        this(0L, 0L, 0L, 0L);
    }
    
    public InteractionData(Long fromParentData, Long toParentData, Long fromCount, Long toCount){
        
        mFromParentData = new ArrayList<Long>();
        mToParentData = new ArrayList<Long>();
        mFromParentCount = new ArrayList<Long>();
        mToParentCount = new ArrayList<Long>();
        
        mFromParentData.add(fromParentData);
        mToParentData.add(toParentData);
        mFromParentCount.add(fromCount);
        mToParentCount.add(toCount);
    }

    public Long getFromParentCount() {
        return (long) MathUtil.sum(mFromParentCount);
    }
    
    public Long getToParentCount(){
        return (long) MathUtil.sum(mToParentCount);
    }

    public Long getFromParentData() {
        return (long) MathUtil.weightedAvg(mFromParentData, mFromParentCount);
    }
    
    public Long getToParentData(){
        return (long) MathUtil.weightedAvg(mToParentData, mToParentCount);
    }
    
    public Long getTotalData(){
        return getToParentData() + getFromParentData();
    }
    
    public Long getTotalCount(){
        return getToParentCount() + getFromParentCount();
    }

    public void addInteraction(Long fpdata, Long tpdata, Long fpcount, Long tpcount){
        mFromParentData.add(fpdata);
        mToParentData.add(tpdata);
        mFromParentCount.add(fpcount);
        mToParentCount.add(tpcount);
    }
   
    public void setIgnoreRate(boolean ignoreRate){
        mIgnoreRate = ignoreRate;
    }
    
    public boolean isIgnoreRate(){
        return mIgnoreRate;
    }
    
    @Override
    public String toString(){
        return "fp: " + mFromParentData + " - tp: " + mToParentData +
                " - fc: " + mFromParentCount + " - tc: " + mToParentCount;
    }
}
