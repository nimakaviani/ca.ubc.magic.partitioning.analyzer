/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

import ca.ubc.magic.profiler.dist.model.UnitModel;
import ca.ubc.magic.profiler.dist.model.WrongScaleException;

/**
 *
 * @author nima
 */
public class LatencyUnit extends Property {
    UnitModel objectUnit;

    public LatencyUnit(){
        super();
    }
    
    public LatencyUnit(Double value, String scale) {
        super(value, scale);
        setValue(value);
        setScale(scale);
    }

    /**
     * Scale for the latency unit will be always returned in milliseconds
     * in order to make the numbers closer to what you would normally 
     * see in a real network. We modify all the numbers to match the millisecond
     * in the entire model.
     * 
     * @param scale 
     */
    @Override
    public final void setScale(String scale) {
        if (scale.equalsIgnoreCase(SECOND)) {
            mScale = 1000.0;
        } else if (scale.equalsIgnoreCase(MILLISECOND)) {
            mScale = 1.0;
        } else if (scale.equalsIgnoreCase(MICROSECOND)) {
            mScale = 1.0E-3;
        } else if (scale.equalsIgnoreCase(NANOSECOND)) {
            mScale = 1.0E-6;
        } else {
            throw new WrongScaleException(LatencyUnit.class);
        }
    }
}
