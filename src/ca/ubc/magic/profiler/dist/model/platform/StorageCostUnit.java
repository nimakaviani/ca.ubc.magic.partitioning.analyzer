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
public class StorageCostUnit extends CostUnit {

    public StorageCostUnit() {
        super();
    }

    public StorageCostUnit(Double value, Double unit, String scale, UnitModel outer) {
        super(value, unit, scale);
        setValue(value);
        setScale(scale);
    }

    @Override
    public final void setScale(String scale) {
        if (scale.equalsIgnoreCase(GB)) {
            mScale = 1.0E9;
        } else if (scale.equalsIgnoreCase(MB)) {
            mScale = 1000000.0;
        } else {
            throw new WrongScaleException(StorageCostUnit.class);
        }
    }
    
}
