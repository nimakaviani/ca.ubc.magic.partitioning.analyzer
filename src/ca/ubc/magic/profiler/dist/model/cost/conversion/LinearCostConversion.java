/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.cost.conversion;

import ca.ubc.magic.profiler.dist.control.Constants;

/**
 *
 * @author nima
 */
public class LinearCostConversion implements ICostConversion {

    public double interactionConvert(double value, double count) {
        return Constants.LINEAR_COST_CONVERSION_FACTOR * value * count;
    }
    
    public double executionConvert(double value, double count) {
        return Constants.LINEAR_COST_CONVERSION_FACTOR * value * count;
    }
}
