/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

/**
 *
 * @author nima
 */
public abstract class Capability extends Property {

    public Capability() {
        super();
    }

    public Capability(Double value, String scale) {
        super(value, scale);
    }
    
}
