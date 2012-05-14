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
public class DataExchangeCapability extends Capability {

    public DataExchangeCapability() {
        super();
    }

    public DataExchangeCapability(Double value, String scale) {
        super(value, scale);
        setValue(value);
        setScale(scale);
    }

    /**
     * The scale for data transfer capability is returned in milliseconds, meaning
     * that a given amount of data can be transferred in one millisecond rather
     * than in one second
     * @param scale 
     */
    @Override
    public final void setScale(String scale) {
        if (scale.equalsIgnoreCase(GB)) {
            mScale = 1.0E6;
        } else if (scale.equalsIgnoreCase(MB)) {
            mScale = 1000.0;
        } else if (scale.equalsIgnoreCase(KB)){
            mScale = 1.0;
        } else if (scale.equalsIgnoreCase(B)){
            mScale = 1.0E-3;
        }else {
            throw new WrongScaleException(DataExchangeCapability.class);
        }
    }
    
}
