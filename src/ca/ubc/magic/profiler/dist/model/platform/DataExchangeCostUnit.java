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
public class DataExchangeCostUnit extends CostUnit {

    public DataExchangeCostUnit() {
        super();
    }

    public DataExchangeCostUnit(Double value, Double unit, String scale) {
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
        } else if (scale.equalsIgnoreCase(B)){
            mScale = 1.0;
        }else {
            throw new WrongScaleException(DataExchangeCostUnit.class);
        }
    }
    
}
