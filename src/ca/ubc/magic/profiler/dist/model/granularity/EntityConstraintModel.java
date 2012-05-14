/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

import ca.ubc.magic.profiler.parser.JipFrame;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author nima
 */
public class EntityConstraintModel {
    
    private Set<CodeEntity> mMethodLevelSet;
    private Set<CodeEntity> mClassLevelSet;
    
    private Set<CodeEntity> mIgnoreSet;
    private Set<CodeEntity> mReplicableSet;
    private Set<CodeEntity> mNonReplicableSet;
    private List<CodeEntity> mRootEntityList;
    
    private ConstraintSwitches mConstraintSwitches;
    
    public EntityConstraintModel(){
        mMethodLevelSet = new HashSet<CodeEntity>();
        mClassLevelSet  = new HashSet<CodeEntity>();
        
        mIgnoreSet = new HashSet<CodeEntity>();
        mReplicableSet = new HashSet<CodeEntity>();
        mNonReplicableSet = new HashSet<CodeEntity>();
        mRootEntityList = new ArrayList<CodeEntity>();
        
        mConstraintSwitches = new ConstraintSwitches();
    }
    
    public List<CodeEntity> getRootEntityList(){
        return mRootEntityList;
    }
    
    
    public Set<CodeEntity> getExposeSet(CodeUnitType type){
        switch(type){
            case CLASS:
                return mClassLevelSet;
            case METHOD:
                return mMethodLevelSet;
            default:
                throw new RuntimeException("Unsupported CodeUnitTye");
        }
    }
    
    public Set<CodeEntity> getIgnoreSet(){
        return mIgnoreSet;
    }
    
    public Set<CodeEntity> getReplicableSet(){
        return mReplicableSet;
    }
    
    public Set<CodeEntity> getNonReplicableSet(){
        return mNonReplicableSet;
    }
    
    private boolean isMethodExposed(JipFrame frame){
        String cmpntName = frame.getMethod().getBundleName(); 
        String className = frame.getMethod().getClassName();
        String methdName = frame.getMethod().getMethodName();
        
        for (CodeEntity entity : mMethodLevelSet)
                if (entity.getEntityPattern().matches(cmpntName, className, methdName))
                    return true;
        return false;
    }
    
    private boolean isClassExposed(JipFrame frame){
        String cmpntName = frame.getMethod().getBundleName(); 
        String className = frame.getMethod().getClassName();
        for (CodeEntity entity : mClassLevelSet)
             if (entity.getEntityPattern().matches(cmpntName, className, null))
                    return true;
        return false;
    }
    
    public String getFrameName(JipFrame frame){
        if (isMethodExposed(frame))
            return frame.getMethod().getBundleName()+":"+frame.getMethod().getClassName()+":"+
                        frame.getMethod().getMethodName();
        else if (isClassExposed(frame))
            return frame.getMethod().getBundleName()+":"+frame.getMethod().getClassName();
        return frame.getMethod().getBundleName();
    }
    
    public CodeUnitType getFrameType(JipFrame frame){
        if (isMethodExposed(frame))
            return CodeUnitType.METHOD;
        else if (isClassExposed(frame))
            return CodeUnitType.CLASS;
        return CodeUnitType.COMPONENT;
    }
    
    public ConstraintSwitches getConstraintSwitches(){
        return mConstraintSwitches;
    }   
}
