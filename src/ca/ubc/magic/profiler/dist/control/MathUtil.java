/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.dist.control;

import java.util.List;

/**
 *
 * @author nima
 */
public class MathUtil {
    
    public static Long avg(List<Long> items){
        Long total = sum(items);
        return new Double(Math.ceil(total / (items.size() * 1.0))).longValue();
    }
    
    public static Long sum(List<Long> items){
        Long total = 0L;
        for (Long item : items)
            total += item;
        return total;
    }
    
     public static <T extends Number> double max(List<T> items){
         double max = 0.0;
         for(int i=0; i < items.size(); i++)
             if (items.get(i).doubleValue() > max)
                 max = items.get(i).doubleValue();
         return max;
     }
    
    public static <T extends Number> double weightedAvg(List<T> items, List<Long> weights){
        if (items.size() != weights.size())
            throw new RuntimeException("Items and weights do not match");
        
        double total      = weightedSum(items, weights);
        double weightSum  = sum(weights);
        
        return Math.ceil(total / (weightSum * 1.0));
    }
    
    public static <T extends Number> double weightedSum(List<T> items, List<Long> weights){
        if (items.size() != weights.size())
            throw new RuntimeException("Items and weights do not match");
        
        double total      = 0.0;
       
        for (int i = 0; i < items.size(); i++){
            total += items.get(i).doubleValue() * weights.get(i);
        }
        
        return Math.ceil(total);
    }
}
