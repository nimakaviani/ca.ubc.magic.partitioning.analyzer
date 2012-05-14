package ca.ubc.magic.profiler.partitioning.control.alg.simplex;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;

import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;

import ca.ubc.magic.profiler.dist.model.Module;

public class LpSolvePartitionerExtended4Cost extends SimplexPartitionerExtended4Cost {
	LpSolve solver; 
	
	@Override
	public void doPartition() {
		try {
			LinearObjectiveFunction objectiveFunction = mSimplexModel.getObjectiveFunction();
			Collection<LinearConstraint> constraints = mSimplexModel.getConstraints();
			double[] coefficients = objectiveFunction.getCoefficients().toArray();
			int numVariables = coefficients.length;
			solver = LpSolve.makeLp(constraints.size(), numVariables);
			solver.setObjFn(coefficients);
			System.out.println("OBJECTIVE: " + Arrays.toString(coefficients));
			for(int i=1;i <= solver.getNcolumns();i++) {
				solver.setInt(i, true);
			}
			Iterator<LinearConstraint> it = constraints.iterator();
			while(it.hasNext()) {
				LinearConstraint lc = it.next();
				double[] lcArray = lc.getCoefficients().toArray();
				solver.addConstraint(lcArray, apacheToLpSolve(lc.getRelationship()), lc.getValue());
				System.out.println("CONSTRAINT: " + Arrays.toString(lcArray) + " " + lc.getRelationship().name() + " " + lc.getValue());
			}
			
			solver.solve();
			double[] solution = solver.getPtrVariables();
			int i = 0;            
			System.out.println("SOLUTION: " + Arrays.toString(solver.getPtrVariables()));
			
			Module m = mModuleModel.getModuleMap().get(mSimplexModel.getNode(0));
			m.setPartitionId(2 - (new Double(0.0d)).intValue());
			for (;i < solution.length-1; i++){
				 if (i >= mSize-1)
	                  break;
				double current = solution[i];
				m = mModuleModel.getModuleMap().get(mSimplexModel.getNode(i+1));
				m.setPartitionId(2 - (new Double(current)).intValue());                    
			}
			
		}catch(Exception e){
			throw new RuntimeException(e.getMessage());
		}
	}
	 
	 @Override
	 public String getSolution() {
	       try {
			return Arrays.toString(solver.getPtrVariables());
		} catch (LpSolveException e) {
			throw new RuntimeException(e);
		}
	 }
	 
	 public static int apacheToLpSolve(Relationship r) {
		 if(r.equals(r.EQ)) {
			 return 3;
		 } else if(r.equals(r.GEQ)) {
			 return 2;
		 } else if(r.equals(r.LEQ)) {
			 return 1;
		 } else {
			 throw new IllegalArgumentException();
		 }
	 }
}
