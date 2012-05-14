/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.granularity;

/**
 *
 * @author nima
 */
public class CodeUnit {
    
    private String mName;
    private CodeUnitType mType;
    
    public CodeUnit(){
    }

    public void setName(String name){
        mName = name;
    }
    
    public String getName(){
        return mName;
    }
    
    public void setType(CodeUnitType type){
        mType = type;
    }
    
    public CodeUnitType getType(){
        return mType;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeUnit other = (CodeUnit) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.mName != null ? this.mName.hashCode() : 0);
        hash = 37 * hash + (this.mType != null ? this.mType.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString(){
        return mName + ":" + mType.getText();
    }
}
