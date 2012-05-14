/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.ubc.magic.profiler.partitioning.control.alg.simplex;

/**
 *
 * @author nima
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import java.util.Set;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;

public class SimplexModel {
	
	List<String> nodeIndexMap;
	double[][] nodeWeight;
	double[][] adjacencyMatrix;
	int size = 0;
        Set<String> sourceSet;
        Set<String> targetSet;
        Set<String[]> pairSet;
                	
	public SimplexModel(int size){
            this.size = size;
            nodeIndexMap  = new ArrayList<String>(size);
            nodeWeight = new double[size][2];
            adjacencyMatrix = new double[size][size];
            for (int i = 0; i < size; i++)
                adjacencyMatrix[i][i] = 0.0;  
            
            sourceSet = new HashSet<String>();
            targetSet = new HashSet<String>();
            pairSet = new HashSet<String[]>();
	}
	
	public void addNode(String nodeId, double sourceWeight, double targetWeight){
            if (nodeIndexMap.contains(nodeId))
                throw new IllegalArgumentException ("node id exists");
            nodeIndexMap.add(nodeId);
            int index = nodeIndexMap.indexOf(nodeId);
            nodeWeight[index][0] = sourceWeight;
            nodeWeight[index][1] = targetWeight;
	}
	
	public void addEdge(String sourceNodeId, String targetNodeId, Double weight){
            Integer sourceindex = nodeIndexMap.indexOf(sourceNodeId);
            Integer targetindex = nodeIndexMap.indexOf(targetNodeId);
            if (sourceindex == null || targetindex == null)
                    throw new RuntimeException("source or target index is null");
            adjacencyMatrix[sourceindex][targetindex] = weight;            
	}
	
	public Collection<LinearConstraint> getConstraints(){
            ArrayList<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
            int indexer = 0;
            for (int i = 0; i < size; i++){
                double[] coefficients1 = new double[size];                
                for (int j = 0; j < size; j++){
                    if (j != indexer)
                        coefficients1[j] = 0;
                    else
                        coefficients1[j] = 1;
                }
                
                String nodeId = nodeIndexMap.get(indexer);
                if (sourceSet.contains(nodeId))
                    constraints.add(new LinearConstraint(coefficients1, Relationship.EQ, 1));
                else if (targetSet.contains(nodeId))
                    constraints.add(new LinearConstraint(coefficients1, Relationship.EQ, 0));
                else{                    
                    constraints.add(new LinearConstraint(coefficients1, Relationship.GEQ, 0));
                    constraints.add(new LinearConstraint(coefficients1, Relationship.LEQ, 1));
                }
                
                indexer ++;
            }
            return constraints;
	}
	
	public LinearObjectiveFunction getObjectiveFunction(){
            double[] mergedWeight = new double[size];
            double   constantTerm = 0.0;
            for (int i = 0; i < size; i++) {
                mergedWeight[i] = nodeWeight[i][0] - nodeWeight[i][1]; 
                constantTerm += nodeWeight[i][1];
//                System.err.println(i+": " + nodeWeight[i][0] + ", " + nodeWeight[i][1] + " => " + mergedWeight[i]);
            }

            for (int i=0; i < size; i++){
                for (int j = 0; j < size; j++){
                    if (adjacencyMatrix[i][j] != 0.0){
                        System.err.println("["+i+","+j+"]: " + adjacencyMatrix[i][j]);
                        mergedWeight[i] += adjacencyMatrix[i][j];
                        mergedWeight[j] -= adjacencyMatrix[i][j];
                    }
                }
            }
            
            printArray("Coeffs", mergedWeight);
            
            System.err.println("constantTerm: " + constantTerm);
            return new LinearObjectiveFunction(mergedWeight, constantTerm);
	}
	
	public String getNode(int index){
            return nodeIndexMap.get(index);
	}
	
	public boolean hasEdge(int i, int j){
            if (adjacencyMatrix[i][j] != 0.0)
                    return true;
            return false;
	}
	
	public int getSize(){
            return size;
	}	
        
        protected void printArray(String title, double[] array){
//            System.out.print(title + ": ");
//            for (int i = 0; i < array.length; i++)
//                System.out.print(array[i] + " ");
//            System.out.println();
        }
        
        protected void pinToSource(String nodeId){
            sourceSet.add(nodeId);
        }

        protected void pinToTarget(String nodeId){
            targetSet.add(nodeId);
        }
        
        protected void pinTogether(String nodeId1, String nodeId2){
            pairSet.add(new String[]{nodeId1, nodeId2});
        }
}

