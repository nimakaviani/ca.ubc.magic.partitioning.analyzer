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
public class MemoryCapability extends Capability {

    public MemoryCapability() {
        super();
    }

    public MemoryCapability(Double value, String scale) {
        super(value, scale);
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
            throw new WrongScaleException(MemoryCapability.class);
        }
    }
    
}
