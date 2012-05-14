/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model;

/**
 *
 * @author nima
 */
public class WrongScaleException extends RuntimeException {
    
    public WrongScaleException(String msg){
        super(msg);
    }    
    
    public WrongScaleException (Class clazz){
        this("Incorrect scale for " + clazz.getCanonicalName());
    }
}
