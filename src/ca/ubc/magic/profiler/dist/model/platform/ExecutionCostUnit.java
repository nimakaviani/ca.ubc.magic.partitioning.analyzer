/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

import ca.ubc.magic.profiler.dist.model.WrongScaleException;

/**
 *
 * @author nima
 */
public class ExecutionCostUnit extends CostUnit {

    public ExecutionCostUnit() {
        super();
    }

    public ExecutionCostUnit(Double value, Double unit, String scale) {
        super(value, unit, scale);
        setValue(value);
        setScale(scale);
    }

    @Override
    public final void setScale(String scale) {
        if (scale.equalsIgnoreCase(SECOND)) {
            mScale = 1.0;
        } else if (scale.equalsIgnoreCase(MILLISECOND)) {
            mScale = 1000.0;
        } else if (scale.equalsIgnoreCase(MICROSECOND)) {
            mScale = 1000000.0;
        } else if (scale.equalsIgnoreCase(NANOSECOND)) {
            mScale = 1.0E9;
        } else {
            throw new WrongScaleException(ExecutionCostUnit.class);
        }
    }
    
}
