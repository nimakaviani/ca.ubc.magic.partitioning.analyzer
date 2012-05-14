/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

import java.util.Arrays;

/**
 *
 * @author nima
 */
public class CodeEntity {
    
    private CodeUnit[] mCodeUnits = null;
    private CodeUnitType mType;
    private CodeEntityPattern mPattern = null;
    
    public CodeEntity(){
        mCodeUnits = new CodeUnit[3];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeEntity other = (CodeEntity) obj;
        if (!Arrays.deepEquals(this.mCodeUnits, other.mCodeUnits)) {
            return false;
        }
        if (this.mType != other.mType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        for (CodeUnit unit : mCodeUnits)
            hash += 37 * unit.hashCode();
        return hash;
    }
    
    public void setComponent(CodeUnit component){
        mCodeUnits[0] = component;
    }
    
    public CodeUnit getComponent(){
        return mCodeUnits[0];
    }
    
    public void setClass(CodeUnit clazz){
        mCodeUnits[1] = clazz;
    }
    
    public CodeUnit getClazz(){
        return mCodeUnits[1];
    }
    
    public void setMethod(CodeUnit method){
        mCodeUnits[2] = method;
    }
    
    public CodeUnit getMethod(){
        return mCodeUnits[2];
    }
    
    public void setTarget(CodeUnitType targetType){
        mType = targetType;
    }
    
    public CodeUnitType getTarget(){
        return mType;
    }
    
    public CodeEntityPattern getEntityPattern(){
        if (mCodeUnits == null)
            throw new RuntimeException("CodeEntity is not initalized and"
                    + "cannot have a pattern");
        if (mPattern == null){
            synchronized(this){
                if (mPattern == null){
                    mPattern = new CodeEntityPattern(this);
                }
            }
        }   
        return mPattern;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (CodeUnit unit : mCodeUnits)
            builder.append(unit.toString()).append(":");
        builder.append(mType.getText());
        return builder.toString();
    }
}
