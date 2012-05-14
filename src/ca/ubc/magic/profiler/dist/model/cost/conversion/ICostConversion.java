/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.cost.conversion;

/**
 *
 * @author nima
 */
public interface ICostConversion {
    
    public double interactionConvert(double value, double count);
    
    public double executionConvert(double value, double count);
    
}
