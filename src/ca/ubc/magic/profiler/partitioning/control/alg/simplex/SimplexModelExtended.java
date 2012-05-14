/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.simplex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;

/**
 *
 * @author nima
 */
public class SimplexModelExtended extends SimplexModel {
    
    List<Edge>   edgeList;

    public SimplexModelExtended(int size) {
        super(size);
        edgeList = new ArrayList<Edge>();
    }
    
    @Override
    public void addEdge(String sourceNodeId, String targetNodeId, Double targetWeight){
        super.addEdge(sourceNodeId, targetNodeId, targetWeight);
        Integer sourceindex = nodeIndexMap.indexOf(sourceNodeId);
        Integer targetindex = nodeIndexMap.indexOf(targetNodeId);
        if (sourceindex == null || targetindex == null)
                throw new RuntimeException("source or target index is null");
        edgeList.add(new Edge(sourceindex, targetindex));
    }
    
    @Override
    public Collection<LinearConstraint> getConstraints(){
        ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        int indexer = 0, tmpIndexer = 0, sourceIndex = 0, targetIndex = 0;
        int variablesSize = size + edgeList.size();
        
        // the loop initializes the coefficines of the paired constraints where
        // it is required for two modules to be placed together. 
        for (String[] pair : pairSet){
            int p1Index = nodeIndexMap.indexOf(pair[0]);
            int p2Index = nodeIndexMap.indexOf(pair[1]);
            double[] coefficients = new double[variablesSize];            
            
            for (int j = 0; j < variablesSize; j++){
                if (j == p1Index)
                    coefficients[j] = 1;
                else if (j == p2Index)
                    coefficients[j] = -1;
                else 
                    coefficients[j] = 0;
                printArray("Coeffs " + indexer + " : ", coefficients);
                constraints.add(new LinearConstraint(coefficients, Relationship.EQ, 0));
            }
        }
        
        // the loop initializes the coefficients for the real variables
        for (int i =0 ; i < size; i++){
            double[] coefficients = new double[variablesSize];            
            for (int j = 0; j < variablesSize; j++){
                if (indexer == j)
                    coefficients[j] = 1;
                else 
                    coefficients[j] = 0;
            }
            printArray("Coeffs " + indexer + " : ", coefficients);
            
            String nodeId = nodeIndexMap.get(indexer);
            if (sourceSet.contains(nodeId))
                constraints.add(new LinearConstraint(coefficients, Relationship.EQ, 1));
            else if (targetSet.contains(nodeId))
                constraints.add(new LinearConstraint(coefficients, Relationship.EQ, 0));
            else{                    
                constraints.add(new LinearConstraint(coefficients, Relationship.GEQ, 0));
                constraints.add(new LinearConstraint(coefficients, Relationship.LEQ, 1));
            }
            indexer++;
        }
        
        // the loop initializes the coefficients for the guard values ensuring
        // generation of differential weights for the model whose values will
        // not be negative to alter the resutls of evaluations.
        for (int i = size; i < variablesSize; i++){
            double[] coefficients = new double[variablesSize];     
            tmpIndexer = (indexer - size);
            sourceIndex = edgeList.get(tmpIndexer).sourceId;
            targetIndex = edgeList.get(tmpIndexer).targetId;
            for (int j = 0; j < variablesSize; j++){
//                if ((indexer - size) % 2 == 1){
                    if (j == sourceIndex || j == indexer)
                        coefficients[j] = 1;
                    else if (j == targetIndex)
                        coefficients[j] = -1;
                    else 
                        coefficients[j] = 0;
//                }else{
//                    if (j == targetIndex || j == indexer)
//                        coefficients[j] = 1;
//                    else if (j == sourceIndex)
//                        coefficients[j] = -1;
//                    else 
//                        coefficients[j] = 0;
//                }
            }
            printArray("Coeffs " + indexer + " : ", coefficients);
            constraints.add(new LinearConstraint(coefficients, Relationship.GEQ, 0));
            constraints.add(new LinearConstraint(coefficients, Relationship.LEQ, 1));
            indexer++;
        }                
        return constraints;
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
           mergedWeight[i] = adjacencyMatrix[e.sourceId][e.targetId];
       }
       printArray("Extended Merged weights: ", mergedWeight);
       return new LinearObjectiveFunction(mergedWeight, constantTerm);
    }        
    
    protected class Edge {
        int sourceId;
        int targetId;

        Edge(int sId, int tId){
            sourceId = sId;
            targetId = tId;
        }
    }
}
