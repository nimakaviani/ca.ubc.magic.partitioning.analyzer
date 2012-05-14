/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

import ca.ubc.magic.profiler.dist.model.UnitModel;

/**
 *
 * @author nima
 */
public abstract class Property {
    public static final String GB = "GB";
    public static final String GHZ = "GHz";
    public static final String MB = "MB";
    public static final String KB  = "KB";
    public static final String B  = "B";
    public static final String MHZ = "MHz";
    public static final String MICROSECOND = "microsecond";
    public static final String MILLISECOND = "millisecond";
    public static final String NANOSECOND = "nanosecond";
    public static final String SECOND = "second";
    protected Double mScale;
    protected Double mValue;
    UnitModel outer;

    public Property() {
        mValue = 0.0;
        mScale = null;
    }

    public Property(Double value, String scale) {
        mValue = value;
        mScale = getScale();
    }

    public Double getScale() {
        return mScale;
    }

    public abstract void setScale(String scale);

    public Double getValue() {
        return mValue * getScale();
    }

    public void setValue(Double value) {
        this.mValue = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Property other = (Property) obj;
        if ((this.mScale == null) ? (other.mScale != null) : !this.mScale.equals(other.mScale)) {
            return false;
        }
        if (this.mValue != other.mValue && (this.mValue == null || !this.mValue.equals(other.mValue))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    @Override
    public String toString(){
        return mValue +" " + mScale;
    }
    
}
