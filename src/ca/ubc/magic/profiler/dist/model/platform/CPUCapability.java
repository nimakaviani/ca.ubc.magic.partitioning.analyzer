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
public class CPUCapability extends Capability {

    public CPUCapability() {
        super();
    }

    public CPUCapability(Double value, String scale) {
        super(value, scale);
        setValue(value);
        setScale(scale);
    }

    @Override
    public final void setScale(String scale) {
        if (scale.equalsIgnoreCase(GHZ)) {
            mScale = 1.0E9;
        } else if (scale.equalsIgnoreCase(MHZ)) {
            mScale = 1000000.0;
        } else {
            throw new WrongScaleException(CPUCapability.class);
        }
    }
    
}
