/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.parser;

import ca.ubc.magic.profiler.dist.control.Constants;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnitType;
import ca.ubc.magic.profiler.dist.model.granularity.CodeEntity;
import ca.ubc.magic.profiler.dist.model.granularity.CodeUnit;
import ca.ubc.magic.profiler.dist.model.granularity.ConstraintType;
import ca.ubc.magic.profiler.dist.model.granularity.EntityConstraintModel;

/**
 *
 * @author nima
 */
public class EntityConstraintHandler {
    
    
    private EntityConstraintModel mConstraintModel;
    
    private CodeEntity mEntity;
    private CodeUnit   mCodeUnit;
    
    private ConstraintType mConstraintType;
    
    public EntityConstraintHandler(){
         mConstraintModel = new EntityConstraintModel();
    }
    
    public EntityConstraintModel getConstraintModel(){
        return mConstraintModel;
    }
    
    void setConstraintType(String cTypeName){
        mConstraintType = ConstraintType.fromString(cTypeName);
    }
    
    void removeConstraintType(){
        mConstraintType = ConstraintType.fromString(Constants.NULL_STRING);
    }
    
    ConstraintType getConstraintType(){
        return mConstraintType;
    }
    
    public void startEntity(){
        mEntity = new CodeEntity();
    }
    
    public void startUnit(){
        mCodeUnit = new CodeUnit();
    }
    
    public void endUnit(String name, CodeUnitType type){
        mCodeUnit.setName(name);
        mCodeUnit.setType(type);
        switch(type){
            case COMPONENT:
                mEntity.setComponent(mCodeUnit);
                break;
            case CLASS:
                mEntity.setClass(mCodeUnit);
                break;
            case METHOD:
                mEntity.setMethod(mCodeUnit);
                break;
        }
    }
    
    public void startTarget(){
        
    }
    
    public void endTarget(String type){
        mEntity.setTarget(CodeUnitType.fromString(type));
    }
    
    public void endEntity(){
        switch (mConstraintType){
            case ROOT:
                setRootEntity();
                break;
            case EXPOSE:
                setExposeEntity();
                break;
            case IGNORE:
                setIgnoreEntity();
                break;
            case REPLICABLE:
                setReplicableEntity();
                break;
            case NON_REPLICABLE:
                setNonReplicableEntity();
                break;
            case NULL:
            default:
                throw new RuntimeException("Invalid entity");
        }
    }    
    
    private void setRootEntity(){
        mConstraintModel.getRootEntityList().add(mEntity);
    }
    
    private void setExposeEntity(){
        mConstraintModel.getExposeSet(mEntity.getTarget()).add(mEntity);
    }
    
    private void setIgnoreEntity(){
        mConstraintModel.getIgnoreSet().add(mEntity);
    }
    
    private void setReplicableEntity(){
        mConstraintModel.getReplicableSet().add(mEntity);
    }
    
    private void setNonReplicableEntity(){
        mConstraintModel.getNonReplicableSet().add(mEntity);
    }
}
