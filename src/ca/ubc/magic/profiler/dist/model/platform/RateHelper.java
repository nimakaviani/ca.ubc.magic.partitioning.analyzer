/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.platform;

/**
 *
 * @author nima
 */
public class RateHelper {
    
    public static double normalizeFromNanoSec(double nanoSeconds, double normalizeFactor){
        return nanoSeconds * normalizeFactor / 1.0E9;
    }
    
    public static double normalizeToNanoSec(double value, double normalizeFactor){
        return value * 1.0E9 / normalizeFactor;
    }
    
}
