/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.model.cost.conversion;

/**
 *
 * @author nima
 */
public class CostConversionSingleton implements ICostConversion{

    private static CostConversionSingleton mInstance = null;
    
    private static String mLock = "lock";
    
    private ICostConversion mCostConversion;
    
    private CostConversionSingleton(){
        mCostConversion = new LinearCostConversion();
    }
    
    public static CostConversionSingleton getInstance(){
        if (mInstance == null){
            synchronized(mLock){
                if (mInstance == null){
                    mInstance = new CostConversionSingleton();
                }
            }
        }
        return mInstance;
    }
    
    public double executionConvert(double value, double count) {
       return mCostConversion.executionConvert(value, count);
    }
    
    public double interactionConvert(double value, double count) {
       return mCostConversion.interactionConvert(value, count);
    }
}
