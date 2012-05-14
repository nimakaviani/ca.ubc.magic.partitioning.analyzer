/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

import java.util.regex.Pattern;

/**
 *
 * @author nima
 */
public class CodeEntityPattern {
    
    private Pattern mCmpntPtr = null;
    private Pattern mClazzPtr = null;
    private Pattern mMethdPtr = null;
    private CodeEntity mEntity;
    
    CodeEntityPattern(CodeEntity entity){
        mEntity = entity;
        mCmpntPtr = Pattern.compile(entity.getComponent().getName());
        mClazzPtr = Pattern.compile(entity.getClazz().getName());
        mMethdPtr = Pattern.compile(entity.getMethod().getName());
    }
    
    private Pattern getComponentPattern(){
        if (mCmpntPtr == null)
                throw new RuntimeException("No pattern found for: " + mEntity.toString());
        return mCmpntPtr;
    }
    
    private Pattern getClazzPattern(){
         if (mClazzPtr == null)
                throw new RuntimeException("No pattern found for: " + mEntity.toString());
        return mClazzPtr;
    }
    
    private Pattern getMethodPattern(){
         if (mMethdPtr == null)
                throw new RuntimeException("No pattern found for: " + mEntity.getMethod().toString());
         if (mEntity.getTarget() != CodeUnitType.METHOD)
             throw new RuntimeException("Entity is not supposed to expse method: " + mEntity.toString());
        return mMethdPtr;
    }
    
    public boolean matches(String component, String clazz, String method){
        switch(mEntity.getTarget()){
            case COMPONENT:
                return getComponentPattern().matcher(component).matches();
            case CLASS:
                return getComponentPattern().matcher(component).matches() &&
                       getClazzPattern().matcher(clazz).matches();
            case METHOD:
                return getComponentPattern().matcher(component).matches() &&
                       getClazzPattern().matcher(clazz).matches() &&
                       getMethodPattern().matcher(method).matches();
            default:    
                throw new RuntimeException("Invalid pattern matching for entity: " +
                        mEntity.toString());
        }
    }
}
