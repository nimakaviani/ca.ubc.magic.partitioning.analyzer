/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

/**
 *
 * @author nima
 */
 public class ModulePair {
    Module mM1;
    Module mM2;

    public ModulePair(Module m1, Module m2){
        if (m1.equals(m2))
            throw new RuntimeException("Same modules can't be paired.");
        mM1 = m1;
        mM2 = m2;
    }
    
    public Module[] getModules(){
        return new Module[]{mM1, mM2};
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof ModulePair))
            return false;
        ModulePair p = (ModulePair) obj;
        if ((p.mM1.equals(mM1) && p.mM2.equals(mM2)) || 
                (p.mM2.equals(mM1) && p.mM1.equals(mM2)))
            return true;                
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.mM1 != null && this.mM2 != null ? this.mM1.hashCode() + this.mM2.hashCode() : 0);
//        hash = 89 * hash + (this.mM2 != null ? this.mM2.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString(){
        return mM1.getName() + "<->" + mM2.getName();
    }
}