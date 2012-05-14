/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.simplex;

import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;

/**
 *
 * @author nima
 */
public class SimplexModelExtended4Cost extends SimplexModelExtended{
    
     public SimplexModelExtended4Cost(int size) {
         super(size);
     }
     
    @Override
    public LinearObjectiveFunction getObjectiveFunction(){
        int variablesSize = size + edgeList.size();
        double[] mergedWeight = new double[variablesSize];
        double   constantTerm = 0.0;
        for (int i = 0; i < size; i++) {
            mergedWeight[i] = nodeWeight[i][0] - nodeWeight[i][1]; 
            constantTerm += nodeWeight[i][1];
//            System.err.println(i+": " + nodeWeight[i][0] + ", " + nodeWeight[i][1] + " => " + mergedWeight[i]);
        }
       for (int i = size; i < variablesSize; i++) {
           int index = (i - size);
           Edge e = edgeList.get(index);
           mergedWeight[i]   = adjacencyMatrix[e.targetId][e.sourceId];
       }
       printArray("Cloud Merged weights: ", mergedWeight);
       return new LinearObjectiveFunction(mergedWeight, constantTerm);
    }        
    
}
