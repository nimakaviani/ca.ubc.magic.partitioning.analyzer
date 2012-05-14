/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

/**
 *
 * @author nima
 */
public abstract class CostUnit extends Property {
    protected Double mUnit;

    public CostUnit() {
        super();
    }

    public CostUnit(Double value, Double unit, String scale) {
        super(value, scale);
        mUnit = unit;
    }
    
    public Double getUnit() {
        return mUnit * getScale();
    }

    public void setUnit(Double unit) {
        this.mUnit = unit;
    }

    @Override
    public Double getValue() {
        return mValue / (mUnit * getScale());
    }
    
    @Override
    public String toString(){
        return "$" + mValue + " per " + mUnit +" " + mScale;
    }
    
}
